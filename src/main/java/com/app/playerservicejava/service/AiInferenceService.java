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
public class AiInferenceService { // Renamed: no longer "Async" service

    private final AiJobRepository jobRepo;
    private final PlayerService playerService;
    private final ChatClientService chatClientService;
    private final JobStatusService statusService;

    // Remove @Async. The SQS poller provides the concurrency.
    @Transactional
    public void processJob(Long jobId) {
        AiJob job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found: " + jobId));

        // 1 IDEMPOTENCY CHECK
        if (job.getStatus() == JobStatus.COMPLETED || job.getStatus() == JobStatus.FAILED) {
            log.info("Job {} already running or completed. Skipping AI call.", jobId);
            return;
        }

        statusService.updateStatus(jobId, JobStatus.RUNNING); //update the status
        // 2. Execute Logic
        String result;
        if (job.getRequestType() == RequestType.PLAYER_ANALYSIS) {
            List<String> playerIds = Arrays.stream(job.getInputText().split(","))
                    .map(String::trim).toList();
            result = playerService.getInsightsforPlayers(playerIds);
        } else {
            result = chatClientService.chatWithPrompt(job.getInputText());
        }

        // 3. Complete here or in worker is also fine
        statusService.markAsCompleted(jobId, result);
    }
}