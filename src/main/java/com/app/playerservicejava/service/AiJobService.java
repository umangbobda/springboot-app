package com.app.playerservicejava.service;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiJobService {

    private final AiJobRepository jobRepo;
    private final AiInferenceAsyncService asyncService;

    public Long createPlayerInsightsJob(List<String> playerIds) {

        AiJob job = new AiJob();
        job.setRequestType(RequestType.PLAYER_ANALYSIS);
        job.setInputText(String.join(",", playerIds));
        job.setStatus(JobStatus.PENDING);

        jobRepo.save(job);
        asyncService.processJob(job.getId());

        return job.getId();
    }

    public Long createFreeTextJob(String text) {

        AiJob job = new AiJob();
        job.setRequestType(RequestType.FREE_TEXT);
        job.setInputText(text);
        job.setStatus(JobStatus.PENDING);

        jobRepo.save(job);
        asyncService.processJob(job.getId());

        return job.getId();
    }
}

