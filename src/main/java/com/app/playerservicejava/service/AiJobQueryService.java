package com.app.playerservicejava.service;

import com.app.playerservicejava.dto.JobStatusResponse;
import com.app.playerservicejava.exception.JobNotFoundException;
import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiJobQueryService {

    private final AiJobRepository jobRepo;

    public JobStatusResponse getJobStatus(Long jobId) {

        AiJob job = jobRepo.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found "+jobId));

        JobStatusResponse response = new JobStatusResponse();
        response.setJobId(job.getId());
        response.setStatus(job.getStatus().name());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        if (job.getStatus() == JobStatus.COMPLETED) {
            response.setResult(job.getResultText());
        }

        if (job.getStatus() == JobStatus.FAILED) {
            response.setErrorMessage(job.getErrorMessage());
        }

        return response;
    }
}

