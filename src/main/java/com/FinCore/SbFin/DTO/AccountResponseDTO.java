package com.FinCore.SbFin.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponseDTO {

    private Long id;
    private Long userId;
    private BigDecimal balance;
    private String status;
    private LocalDateTime createdAt;
}
