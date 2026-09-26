package com.FinCore.SbFin.Services;

import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Entity.User;
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

    public Account CreateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setStatus("Active");

        return accountRepository.save(account);

    }

//    public Account depositAmount(Long id, BigDecimal amount) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        if(account.getStatus().equals("Active")) {
//           account.setBalance(account.getBalance().add(amount));
//        }
//
//        return accountRepository.save(account);
//
//    }

    public Account depositAmount(Long id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getStatus().equals("Active")) {
            throw new RuntimeException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

//    public Account withDrawAmount(Long id, BigDecimal amount) {
//        Account account = accountRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        if (account.getStatus().equals("Active")) {
//            account.setBalance(account.getBalance().subtract(amount));
//        }
//        return accountRepository.save(account);
//
//    }

    public Account withDrawAmount(Long id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getStatus().equals("Active")) {
            throw new RuntimeException("Account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        return accountRepository.save(account);
    }
}