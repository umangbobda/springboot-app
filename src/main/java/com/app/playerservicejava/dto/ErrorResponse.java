package com.app.playerservicejava.dto;

import lombok.Getter;
import lombok.ToString;

@Getter @ToString //setter won't work on private fields so @Data is not needed
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final long timestamp;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
}
