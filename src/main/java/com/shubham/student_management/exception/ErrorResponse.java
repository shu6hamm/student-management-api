package com.shubham.student_management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

// The JSON shape every error will use
@JsonInclude(JsonInclude.Include.NON_NULL)   // hide fields that are null
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> validationErrors;

    public ErrorResponse(int status, String error, String message,
                         Map<String, String> validationErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public Map<String, String> getValidationErrors() { return validationErrors; }
}