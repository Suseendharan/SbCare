package com.FinCore.SbFin.ServerTesting;

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
import com.FinCore.SbFin.Services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransationServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

   @Test
    void transferSuccess() {

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(5000));
        fromAccount.setStatus("Active");

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(1000));
        toAccount.setStatus("Active");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        TransactionResponseDTO result = transactionService.transfer(dto);

        assertEquals(BigDecimal.valueOf(3000), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(3000), toAccount.getBalance());
        assertEquals(BigDecimal.valueOf(2000), result.getAmount());
        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        assertEquals(TransactionType.TRANSFER, result.getType());
        assertNotNull(result.getTransactionReference());

//          Verify that save() was actually called on transactionRepository with a Transaction.
        verify(transactionRepository).save(any(Transaction.class));

    }

    @Test
    void transferInfucciantBalance(){

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setStatus("Active");

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(1000));
        toAccount.setStatus("Active");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));


        InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.transfer(dto)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class)); //Mockito, check that this mock was never called in the following way.



    }

    @Test
    void transferToSameAccount() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(1L);
        dto.setAmount(BigDecimal.valueOf(2000));

        SameAccountTransferException exception = assertThrows(SameAccountTransferException.class,
                () -> transactionService.transfer(dto));

        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).findById(anyLong());

    }

    @Test
    void transferAccountNotFound() {

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setStatus("Active");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.empty());


        assertThrows(AccountNotFoundException.class,
                () -> transactionService.transfer(dto));

        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void senderInactive(){

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setStatus(" ");

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(1000));
        toAccount.setStatus("Active");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));


        assertThrows(
                InactiveAccountException.class,
                () -> transactionService.transfer(dto)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));

    }

    @Test
    void receiverInactive(){

        Account fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000));
        fromAccount.setStatus("Active");

        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(1000));
        toAccount.setStatus("");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(2000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(toAccount));


        assertThrows(
                InactiveAccountException.class,
                () -> transactionService.transfer(dto)
        );

        verify(transactionRepository, never())
                .save(any(Transaction.class));

    }


}