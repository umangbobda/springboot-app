package com.app.playerservicejava.service;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.repository.AiJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobStatusService {
    private final AiJobRepository jobRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatus(Long jobId, JobStatus status) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(status);
        job.setUpdatedAt(LocalDateTime.now());
        jobRepo.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsCompleted(Long jobId, String result) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.COMPLETED);
        job.setUpdatedAt(LocalDateTime.now());
        job.setResultText(result);
        jobRepo.save(job);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsFailed(Long jobId, String errorMessage) {
        AiJob job = jobRepo.findById(jobId).orElseThrow();
        job.setStatus(JobStatus.FAILED);
        job.setUpdatedAt(LocalDateTime.now());
        job.setErrorMessage(errorMessage);
        jobRepo.save(job);
    }
}
