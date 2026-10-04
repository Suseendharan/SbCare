package com.FinCore.SbFin.ServerTesting;

import com.FinCore.SbFin.DTO.AccountResponseDTO;
import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Entity.User;
import com.FinCore.SbFin.Exception.AccountNotFoundException;
import com.FinCore.SbFin.Exception.InactiveAccountException;
import com.FinCore.SbFin.Exception.InsufficientBalanceException;
import com.FinCore.SbFin.Exception.UserNotFoundException;
import com.FinCore.SbFin.Repository.AccountRepository;
import com.FinCore.SbFin.Repository.UserRepository;
import com.FinCore.SbFin.Services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountSuccess() {

        User user = new User();
        user.setId(1L);
        user.setName("user");

        Account account = new Account();
        account.setUser(user);
        account.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountResponseDTO resultAccount = accountService.CreateAccount(1L);

        assertEquals(user.getId(), resultAccount.getUserId());

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccountFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> accountService.CreateAccount(1L));

        verify(accountRepository,never()).save(any(Account.class));
    }

    @Test
    void depositSuccessfully(){
        Account account = new Account();
        account.setId(2L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setStatus("Active");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        accountService.depositAmount(2L,BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(400), account.getBalance());

        verify(accountRepository).save(any(Account.class));

        verify(accountRepository).findById(2L);
//        verify(accountRepository).save(account);


    }

    @Test
    void depositFail(){
        Account account = new Account();
        account.setId(2L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setStatus(" ");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        assertThrows(InactiveAccountException.class, () -> accountService.depositAmount(2L,BigDecimal.valueOf(200)));

        verify(accountRepository,never()).save(any(Account.class));

    }

    @Test
    void withdrawSuccessfully(){
        Account account = new Account();
        account.setId(2L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setStatus("Active");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        accountService.withDrawAmount(2L,BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), account.getBalance());

        verify(accountRepository).save(any(Account.class));

        verify(accountRepository).findById(2L);

    }

    @Test
    void withdrawInsufficientBalance(){
        Account account = new Account();
        account.setId(2L);
        account.setBalance(BigDecimal.valueOf(200));
        account.setStatus("Active");

        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> accountService.withDrawAmount(2L,BigDecimal.valueOf(300)));

        verify(accountRepository,never()).save(any(Account.class));

        verify(accountRepository).findById(2L);

    }

    @Test
    void withdrawFailAccountNotFound(){

        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.withDrawAmount(2L,BigDecimal.valueOf(300)));

        verify(accountRepository,never()).save(any(Account.class));

        verify(accountRepository).findById(2L);

    }
}
