package com.FinCore.SbFin.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {

    private String message;

    private String error;

    private Integer status;

    private LocalDateTime timestamp;

    public ErrorResponseDTO(String error, String message, int status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}
