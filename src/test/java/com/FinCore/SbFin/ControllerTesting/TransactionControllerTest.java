package com.FinCore.SbFin.ControllerTesting;

import com.FinCore.SbFin.Controller.TransactionsController;
import com.FinCore.SbFin.DTO.TransactionRequestDTO;
import com.FinCore.SbFin.DTO.TransactionResponseDTO;
import com.FinCore.SbFin.Exception.AccountNotFoundException;
import com.FinCore.SbFin.Services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionsController.class)
public class TransactionControllerTest {


    @MockitoBean
    TransactionService transactionService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void doTransactionSuccess() throws Exception{
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(1000));


        when(transactionService.transfer(any(TransactionRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(
                post("/transaction/transfer").content("""
        {
            "amount": 1000,
            "fromAccountId": 1,
            "toAccountId": 2
        }
        """)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000));

        verify(transactionService).transfer(any(TransactionRequestDTO.class));

    }

    @Test
    void doTransactionFail() throws Exception{
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(BigDecimal.valueOf(1000));


        when(transactionService.transfer(any(TransactionRequestDTO.class))).thenThrow(new AccountNotFoundException("Account Not Found"));

        mockMvc.perform(
                post("/transaction/transfer").content("""
        {
            "amount": 1000,
            "fromAccountId": 1,
            "toAccountId": 2
        }
        """)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Account Not Found"));

        verify(transactionService).transfer(any(TransactionRequestDTO.class));

    }

}
