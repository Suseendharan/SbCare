package com.FinCore.SbFin;

import com.FinCore.SbFin.DTO.TransactionRequestDTO;
import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Repository.AccountRepository;
import com.FinCore.SbFin.Repository.TransactionRepository;
import com.FinCore.SbFin.Services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
class SbCareApplicationTests {

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

	}

}
