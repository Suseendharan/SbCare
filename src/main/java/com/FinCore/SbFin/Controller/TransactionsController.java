package com.FinCore.SbFin.Controller;

import com.FinCore.SbFin.DTO.TransactionRequestDTO;
import com.FinCore.SbFin.DTO.TransactionResponseDTO;
import com.FinCore.SbFin.Entity.Transaction;
import com.FinCore.SbFin.Exception.InsufficientBalanceException;
import com.FinCore.SbFin.Exception.SameAccountTransferException;
import com.FinCore.SbFin.Services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionsController {

    private final TransactionService transactionService;
    public TransactionsController(TransactionService transactionService1) {
        this.transactionService = transactionService1;
    }

    @PostMapping("/transfer")
    public TransactionResponseDTO doTransaction(@Valid @RequestBody TransactionRequestDTO transactionDTO) {
       return  transactionService.transfer(transactionDTO);
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponseDTO> getAccountTransactions(@PathVariable Long accountId) {
       return transactionService.transactionHistory(accountId);
    }

    @GetMapping("/{transactionReference}")
    public TransactionResponseDTO getTransactionByReference(@PathVariable String transactionReference) {
        return transactionService.getTransactionByTransactionReference(transactionReference);
    }
}
