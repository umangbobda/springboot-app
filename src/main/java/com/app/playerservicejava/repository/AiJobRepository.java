package com.app.playerservicejava.repository;

import com.app.playerservicejava.model.AiJob;
import com.app.playerservicejava.service.JobStatus;
import com.app.playerservicejava.service.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AiJobRepository extends JpaRepository<AiJob, Long> {

    // Find jobs by status (PENDING, RUNNING, COMPLETED, FAILED)
    List<AiJob> findByStatus(JobStatus status);

    // Optional: find by request type
    List<AiJob> findByRequestType(RequestType requestType);

    // Optional: find jobs ordered by creation time
    List<AiJob> findAllByOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query("UPDATE AiJob j SET j.retryCount = j.retryCount + 1 WHERE j.id = :jobId")
    void incrementRetryCount(@Param("jobId") Long jobId);

    @Query("SELECT j.retryCount FROM AiJob j WHERE j.id = :jobId")
    int getRetryCount(@Param("jobId") Long jobId);
}


