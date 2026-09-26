package com.FinCore.SbFin.Services;

import com.FinCore.SbFin.DTO.TransactionRequestDTO;
import com.FinCore.SbFin.DTO.TransactionResponseDTO;
import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Entity.Transaction;
import com.FinCore.SbFin.Entity.TransactionStatus;
import com.FinCore.SbFin.Entity.TransactionType;
import com.FinCore.SbFin.Exception.AccountNotFoundException;
import com.FinCore.SbFin.Exception.InactiveAccountException;
import com.FinCore.SbFin.Exception.InsufficientBalanceException;
import com.FinCore.SbFin.Exception.SameAccountTransferException;
import com.FinCore.SbFin.Repository.AccountRepository;
import com.FinCore.SbFin.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private final ObjectMapper objectMapper;


    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.objectMapper = objectMapper;
    }


    @Transactional
    public TransactionResponseDTO transfer(TransactionRequestDTO transactionDTO)  {

        if(transactionDTO.getFromAccountId().equals(transactionDTO.getToAccountId())) {
            throw new SameAccountTransferException("Cannot transfer to the same account");
        }

        Transaction transaction = new Transaction();

        Optional<Account> opfromAccount =   accountRepository.findById(transactionDTO.getFromAccountId());
        Optional<Account> optoAccount =   accountRepository.findById(transactionDTO.getToAccountId());

        if(!(opfromAccount.isPresent() && optoAccount.isPresent())) {
            throw new AccountNotFoundException("Account not found");
        }

        Account fromAccount = opfromAccount.get();
        Account toAccount = optoAccount.get();

        if(!(fromAccount.getStatus().equals("Active") && toAccount.getStatus().equals("Active"))){

            throw new InactiveAccountException("Account status not active");
        }

        if(fromAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(transactionDTO.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transactionDTO.getAmount()));
        transaction.setFromAccount(fromAccount);
        transaction.setTransactionReference("TXN-" + UUID.randomUUID());
        transaction.setToAccount(toAccount);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setType(TransactionType.TRANSFER);

        transaction = transactionRepository.save(transaction);

        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setTransactionReference(transaction.getTransactionReference());
        dto.setAmount(transaction.getAmount());
        dto.setFromAccountId(transaction.getFromAccount().getId());
        dto.setToAccountId(transaction.getToAccount().getId());
        dto.setStatus(transaction.getStatus());
        dto.setType(transaction.getType());
        dto.setCreatedAt(transaction.getCreatedAt());

        return dto;

    }

    public List<TransactionResponseDTO> transactionHistory(Long accountId) {

        // First check whether the account exists
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        // If account exists, find its transactions
        List<Transaction> transactions =
                transactionRepository.findByFromAccountIdOrToAccountId(
                        accountId,
                        accountId
                );

        // Convert entities to response DTOs
        List<TransactionResponseDTO> transactionResponseDTOs = new ArrayList<>();

        for (Transaction transaction : transactions) {

            TransactionResponseDTO dto = new TransactionResponseDTO();

            dto.setTransactionReference(transaction.getTransactionReference());
            dto.setAmount(transaction.getAmount());
            dto.setFromAccountId(transaction.getFromAccount().getId());
            dto.setToAccountId(transaction.getToAccount().getId());
            dto.setStatus(transaction.getStatus());
            dto.setType(transaction.getType());
            dto.setCreatedAt(transaction.getCreatedAt());

            transactionResponseDTOs.add(dto);
        }

        return transactionResponseDTOs;
    }

        public TransactionResponseDTO getTransactionByTransactionReference(String transactionReference) {
            Optional<Transaction> opTransaction = transactionRepository.findByTransactionReference(transactionReference);

            if(opTransaction.isEmpty()){
                throw new AccountNotFoundException("Transaction not found");
            }

            Transaction transaction = opTransaction.get();

            TransactionResponseDTO dto = new TransactionResponseDTO();

            dto.setTransactionReference(transaction.getTransactionReference());
            dto.setAmount(transaction.getAmount());
            dto.setFromAccountId(transaction.getFromAccount().getId());
            dto.setToAccountId(transaction.getToAccount().getId());
            dto.setStatus(transaction.getStatus());
            dto.setType(transaction.getType());
            dto.setCreatedAt(transaction.getCreatedAt());

            return dto;

        }
}
