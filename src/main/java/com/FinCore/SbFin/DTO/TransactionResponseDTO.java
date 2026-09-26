package com.FinCore.SbFin.DTO;

import com.FinCore.SbFin.Entity.TransactionStatus;
import com.FinCore.SbFin.Entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private String transactionReference;

    private BigDecimal amount;

    private Long fromAccountId;

    private Long toAccountId;

    private TransactionStatus status;

    private TransactionType type;

    private LocalDateTime createdAt;
}
