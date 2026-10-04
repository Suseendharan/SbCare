package com.FinCore.SbFin.Controller;

import com.FinCore.SbFin.DTO.AccountResponseDTO;
import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService1) {
        this.accountService = accountService1;
    }

    @PostMapping("/{id}")
    public ResponseEntity<AccountResponseDTO>  createAccount (@PathVariable Long id) {
        AccountResponseDTO account = accountService.CreateAccount(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PatchMapping("/{id}/deposit")
    public ResponseEntity<AccountResponseDTO>  deposit(@PathVariable Long id, @RequestBody BigDecimal amount) {
        AccountResponseDTO account = accountService.depositAmount(id , amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponseDTO>  withdraw(@PathVariable Long id, @RequestBody BigDecimal amount) {
        AccountResponseDTO account = accountService.withDrawAmount(id , amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }
}
