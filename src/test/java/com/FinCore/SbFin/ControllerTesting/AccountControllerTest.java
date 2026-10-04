package com.FinCore.SbFin.ControllerTesting;

import com.FinCore.SbFin.Controller.AccountController;
import com.FinCore.SbFin.DTO.AccountResponseDTO;
import com.FinCore.SbFin.Exception.AccountNotFoundException;
import com.FinCore.SbFin.Services.AccountService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @MockitoBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAccountSuccess() throws Exception {

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(1L);
        response.setUserId(1L);
        response.setBalance(BigDecimal.ZERO);
        response.setStatus("Active");

        when(accountService.CreateAccount(1L))
                .thenReturn(response);

        mockMvc.perform(
                        post("/account/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.userId").value(1))
        .andExpect(jsonPath("$.balance").value(BigDecimal.ZERO))
        .andExpect(jsonPath("$.status").value("Active"));

        verify(accountService).CreateAccount(1L);

    }

    @Test
    void createAccountFailure() throws Exception {
        when(accountService.CreateAccount(1L))
                .thenThrow(new AccountNotFoundException("Account Not Found"));

        mockMvc.perform(
                post("/account/1")
        )
                .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Account Not Found"))
        .andExpect(jsonPath("$.error").value("NOT_FOUND"));

        verify(accountService).CreateAccount(1L);

    }

    @Test
    void accountDepositSuccess() throws Exception {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(1L);
        response.setUserId(1L);
        response.setBalance(BigDecimal.ZERO);
        response.setStatus("Active");


        when(accountService.depositAmount(1L, BigDecimal.valueOf(1000)))
                .thenReturn(response);

        mockMvc.perform(
                patch("/account/1/deposit").content("1000").contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void accountDepositFailure() throws Exception {

        when(accountService.depositAmount(1L, BigDecimal.valueOf(1000))).thenThrow(new AccountNotFoundException("Account Not Found"));

        mockMvc.perform(
                        patch("/account/1/deposit").content("1000").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(accountService).depositAmount(1L, BigDecimal.valueOf(1000));

    }

    @Test
    void accountWithdrawSuccess() throws Exception {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(1L);
        response.setUserId(1L);
        response.setBalance(BigDecimal.ZERO);
        response.setStatus("Active");


        when(accountService.withDrawAmount(1L, BigDecimal.valueOf(1000)))
                .thenReturn(response);

        mockMvc.perform(
                        patch("/account/1/withdraw").content("1000").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void accountWithdrawFailure() throws Exception {

        when(accountService.withDrawAmount(1L, BigDecimal.valueOf(1000))).thenThrow(new AccountNotFoundException("Account Not Found"));

        mockMvc.perform(
                        patch("/account/1/withdraw").content("1000").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(accountService).withDrawAmount(1L, BigDecimal.valueOf(1000));

    }

}