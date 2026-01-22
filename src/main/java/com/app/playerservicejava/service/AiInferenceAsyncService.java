package com.app.playerservicejava.service;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import com.app.playerservicejava.service.chat.ChatClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor //lombok to creare noarg constructor to initialize all the services
@Slf4j //lombok - no need to create logger line , directly use log.error()..etcc
public class AiInferenceAsyncService {

    private final AiJobRepository jobRepo;
    private final PlayerService playerService;
    private final ChatClientService chatClientService;

    @Async("aiExecutor")
    public void processJob(Long jobId) {

        AiJob job = jobRepo.findById(jobId).orElseThrow();

        try {
            job.setStatus(JobStatus.RUNNING);
            jobRepo.save(job);
            String result;

            if (job.getRequestType() == RequestType.PLAYER_ANALYSIS) {

                List<String> playerIds =
                        Arrays.stream(job.getInputText().split(","))
                                .map(String::trim)
                                .toList();

                result = playerService.getInsightsforPlayers(playerIds);

            } else {
                result = chatClientService.chatWithPrompt(job.getInputText());
            }

            job.setStatus(JobStatus.COMPLETED);
            job.setResultText(result);

        } catch (Exception e) {
            log.error("AI job failed {}", jobId, e);
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
        }

        // save final status/result
        jobRepo.save(job);
    }



}

