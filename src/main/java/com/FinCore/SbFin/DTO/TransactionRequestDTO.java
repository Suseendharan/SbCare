package com.FinCore.SbFin.DTO;

import com.FinCore.SbFin.Entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {


    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;
}
