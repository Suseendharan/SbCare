package com.FinCore.SbFin.RespositoryTesting;

import com.FinCore.SbFin.Entity.*;
import com.FinCore.SbFin.Repository.AccountRepository;
import com.FinCore.SbFin.Repository.TransactionRepository;
import com.FinCore.SbFin.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findByTransactionReferenceTest() {

        Transaction transaction = new Transaction();

        transaction.setTransactionReference("TXN-001");
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        Optional<Transaction> op =
                transactionRepository.findByTransactionReference("TXN-001");

        assertTrue(op.isPresent());

        assertEquals(
                "TXN-001",
                op.get().getTransactionReference()
        );
    }


    @Test
    public void findByTransactionReferenceTestNotFound() {

        Optional<Transaction> op =
                transactionRepository.findByTransactionReference(
                        "INVALID-REFERENCE"
                );

        assertTrue(op.isEmpty());
    }


    @Test
    public void findByFromAccountIdOrToAccountIdTest() {

        // Create users
        User user1 = new User();
        user1.setName("John");
        user1.setEmail("john@gmail.com");
        user1.setPassword("12345");

        User user2 = new User();
        user2.setName("Alex");
        user2.setEmail("alex@gmail.com");
        user2.setPassword("12345");

        userRepository.save(user1);
        userRepository.save(user2);


        // Create accounts
        Account account1 = new Account();
        account1.setUser(user1);
        account1.setBalance(BigDecimal.valueOf(10000));
        account1.setStatus("Active");
        account1.setCreatedAt(LocalDateTime.now());

        Account account2 = new Account();
        account2.setUser(user2);
        account2.setBalance(BigDecimal.valueOf(5000));
        account2.setStatus("Active");
        account2.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account1);
        accountRepository.save(account2);


        // Transaction 1: account1 -> account2
        Transaction transaction1 = new Transaction();

        transaction1.setTransactionReference("TXN-001");
        transaction1.setFromAccount(account1);
        transaction1.setToAccount(account2);
        transaction1.setAmount(BigDecimal.valueOf(1000));
        transaction1.setStatus(TransactionStatus.SUCCESS);
        transaction1.setType(TransactionType.TRANSFER);
        transaction1.setCreatedAt(LocalDateTime.now());


        // Transaction 2: account2 -> account1
        Transaction transaction2 = new Transaction();

        transaction2.setTransactionReference("TXN-002");
        transaction2.setFromAccount(account2);
        transaction2.setToAccount(account1);
        transaction2.setAmount(BigDecimal.valueOf(500));
        transaction2.setStatus(TransactionStatus.SUCCESS);
        transaction2.setType(TransactionType.TRANSFER);
        transaction2.setCreatedAt(LocalDateTime.now());


        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);


        // Search transactions involving account1
        List<Transaction> transactions =
                transactionRepository.findByFromAccountIdOrToAccountId(
                        account1.getId(),
                        account1.getId()
                );


        assertEquals(2, transactions.size());
    }


    @Test
    public void findByFromAccountIdOrToAccountIdNotFound() {

        // Use an account ID that has no transactions
        List<Transaction> transactions =
                transactionRepository.findByFromAccountIdOrToAccountId(
                        99999L,
                        99999L
                );

        assertTrue(transactions.isEmpty());
    }

}
