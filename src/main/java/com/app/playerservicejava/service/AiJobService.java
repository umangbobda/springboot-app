package com.app.playerservicejava.service;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiJobService {

    private final AiJobRepository jobRepo;
    private final AiInferenceService aiInferenceServiceService;
    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue.url}")
    private String queueUrl;

    public Long createPlayerInsightsJob_spring(List<String> playerIds) {

        AiJob job = new AiJob();
        job.setRequestType(RequestType.PLAYER_ANALYSIS);
        job.setInputText(String.join(",", playerIds));
        job.setStatus(JobStatus.PENDING);
        job.setRetryCount(0);

        jobRepo.save(job);
        aiInferenceServiceService.processJob(job.getId());

        return job.getId();
    }

    public Long createPlayerInsightsJob(List<String> playerIds) {

        AiJob job = AiJob.builder()
                .requestType(RequestType.PLAYER_ANALYSIS)
                .inputText(String.join(",", playerIds))
                .status(JobStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .retryCount(0)
                .build();
        jobRepo.save(job);

        // Publish jobId to SQS
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(String.valueOf(job.getId()))
                .build());

        return job.getId();
    }


    public Long createFreeTextJob_spring(String text) {

        AiJob job = new AiJob();
        job.setRequestType(RequestType.FREE_TEXT);
        job.setInputText(text);
        job.setStatus(JobStatus.PENDING);
        job.setRetryCount(0);


        jobRepo.save(job);
        aiInferenceServiceService.processJob(job.getId());

        return job.getId();
    }

    public Long createFreeTextJob(String text) {

        AiJob job = AiJob.builder()
                .requestType(RequestType.FREE_TEXT)
                .inputText(text)
                .status(JobStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .retryCount(0)
                .build();
        jobRepo.save(job);

        // Publish jobId to SQS
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(String.valueOf(job.getId()))
                .build());

        return job.getId();
    }
}

