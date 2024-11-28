package com.example.agilizapp.api.auth.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime dateTime;
    private int httpStatus;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}