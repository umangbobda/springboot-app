package com.app.playerservicejava.service;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobStatusService {
    private final AiJobRepository jobRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatus(Long jobId, JobStatus status) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(status);
        jobRepo.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsCompleted(Long jobId, String result) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.COMPLETED);
        job.setResultText(result);
        jobRepo.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsFailed(Long jobId, String errorMessage) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.FAILED);
        job.setErrorMessage(errorMessage);
        jobRepo.save(job);
    }
}
