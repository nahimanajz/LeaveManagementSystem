package com.leave.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Error response details")
@Data
public class ErrorResponse {
    @Schema(description = "Error message", example = "User not found")
    private String message;

    @Schema(description = "Error code", example = "USER_NOT_FOUND")
    private String errorCode;

    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
} 