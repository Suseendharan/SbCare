package com.FinCore.SbFin.Services;

import com.FinCore.SbFin.DTO.AccountResponseDTO;
import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Exception.*;
import com.FinCore.SbFin.Repository.AccountRepository;
import com.FinCore.SbFin.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponseDTO CreateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setStatus("Active");

        accountRepository.save(account);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setCreatedAt(account.getCreatedAt());
        accountResponseDTO.setStatus(account.getStatus());
        accountResponseDTO.setUserId(account.getUser().getId());

        return accountResponseDTO;

    }

    public AccountResponseDTO depositAmount(Long id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getStatus().equals("Active")) {
            throw new InactiveAccountException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountLessThanZeroException("Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setCreatedAt(account.getCreatedAt());
        accountResponseDTO.setStatus(account.getStatus());
        accountResponseDTO.setUserId(account.getUser().getId());

        return accountResponseDTO;
    }

    public AccountResponseDTO withDrawAmount(Long id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getStatus().equals("Active")) {
            throw new InactiveAccountException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountLessThanZeroException("Withdrawal amount must be greater than zero");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setId(account.getId());
        accountResponseDTO.setBalance(account.getBalance());
        accountResponseDTO.setCreatedAt(account.getCreatedAt());
        accountResponseDTO.setStatus(account.getStatus());
        accountResponseDTO.setUserId(account.getUser().getId());

        return accountResponseDTO;
    }
}