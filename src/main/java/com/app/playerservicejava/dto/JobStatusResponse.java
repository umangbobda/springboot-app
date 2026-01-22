package com.app.playerservicejava.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * this is response for polling api
 */
@Data
public class JobStatusResponse {

    private Long jobId;
    private String status;
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

