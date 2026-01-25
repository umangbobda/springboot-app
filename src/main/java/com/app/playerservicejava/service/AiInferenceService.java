package com.app.playerservicejava.service;

import com.app.playerservicejava.exception.JobNotFoundException;
import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import com.app.playerservicejava.service.chat.ChatClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor //lombok to creare noarg constructor to initialize all the services
@Slf4j //lombok - no need to create logger line , directly use log.error()..etcc
public class AiInferenceService {

    private final AiJobRepository jobRepo;
    private final PlayerService playerService;
    private final ChatClientService chatClientService;
    private final JobStatusService statusService;

    // Main entry called by pollQueue / SQS worker
    public void processJob(Long jobId) {
        // 1 Fetch job quickly in a short transaction
        AiJob job = fetchJob(jobId);

        // 2 Idempotency check
        if (job.getStatus() == JobStatus.COMPLETED || job.getStatus() == JobStatus.FAILED) {
            log.info("Job {} already running or completed. Skipping AI call.", jobId);
            return;
        }

        // 3 Mark as RUNNING in a short transaction
        updateStatusRunning(jobId);

        // 4 Execute the AI or Player logic OUTSIDE transaction
        String result;
        try {
            if (job.getRequestType() == RequestType.PLAYER_ANALYSIS) {
                List<String> playerIds = Arrays.stream(job.getInputText().split(","))
                        .map(String::trim).toList();
                result = playerService.getInsightsforPlayers(playerIds); // external call
            } else {
                result = chatClientService.chatWithPrompt(job.getInputText()); // HTTP call
            }
        } catch (Exception e) {
            log.error("AI call failed for job {}: {}", jobId, e.getMessage(), e);
            statusService.markAsFailed(jobId, e.getMessage());
            return;
        }

        // 5 Save the result in a short transaction
        markJobCompleted(jobId, result);
    }

    // ---------------- DB helper methods ----------------

    @Transactional
    protected AiJob fetchJob(Long jobId) {
        return jobRepo.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found while processing job: " + jobId));
    }

    @Transactional
    protected void updateStatusRunning(Long jobId) {
        statusService.updateStatus(jobId, JobStatus.RUNNING);
    }

    @Transactional
    protected void markJobCompleted(Long jobId, String result) {
        statusService.markAsCompleted(jobId, result);
    }
}
