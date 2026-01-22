package com.app.playerservicejava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobResponse {
    private Long jobId;
    private String status;
    private String pollUrl;
}

