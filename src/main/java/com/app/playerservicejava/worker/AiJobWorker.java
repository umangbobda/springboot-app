package com.app.playerservicejava.worker;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.service.AiInferenceService;
import com.app.playerservicejava.service.JobStatus;
import com.app.playerservicejava.service.JobStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiJobWorker {

    private final SqsClient sqsClient;
    private final AiInferenceService aiService;
    private final JobStatusService statusService;
    private static final int MAX_SQS_RETRIES = 3;

    @Value("${aws.sqs.queue.url}")
    private String queueUrl;

    @Scheduled(fixedDelay = 3000)
    public void pollQueue() {
        var request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(5) // Long polling is more efficient
                .attributeNamesWithStrings("ApproximateReceiveCount") // Needed to track retries
                .build();

        var messages = sqsClient.receiveMessage(request).messages();

        for (var msg : messages) {
            Long jobId = Long.parseLong(msg.body());
            // Check how many times SQS has delivered this specific message
            String rawCount = msg.attributes().get(MessageSystemAttributeName.APPROXIMATE_RECEIVE_COUNT);
            int receiveCount = (rawCount != null) ? Integer.parseInt(rawCount) : 1;

            try {
                log.info("Processing job {}  messageId={} (SQS Attempt {})", jobId,msg.messageId(), receiveCount);

                // Execute synchronously relative to the poller
                aiService.processJob(jobId);

                // SUCCESS: Delete from SQS
                sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(msg.receiptHandle())
                        .build());

            } catch (Exception e) {
                if (receiveCount >= MAX_SQS_RETRIES) {
                    log.error("Job {} failed after {} SQS attempts. Marking as FAILED.", jobId, MAX_SQS_RETRIES);
                    statusService.markAsFailed(jobId, e.getMessage());

                    // Delete from SQS so it doesn't loop forever (or it goes to your AWS DLQ)
                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(msg.receiptHandle())
                            .build());
                } else {
                    log.warn("Job {} failed attempt {}. Letting SQS retry.", jobId, receiveCount);
                    //statusService.updateStatus(jobId, JobStatus.PENDING);
                    // Do NOT delete. Do NOT mark as FAILED yet.
                    // SQS visibility timeout will expire and it will try again.
                }
                // IMPORTANT: If we don't delete and don't catch, SQS retries automatically
                // after the Visibility Timeout expires.
            }
        }
    }
}