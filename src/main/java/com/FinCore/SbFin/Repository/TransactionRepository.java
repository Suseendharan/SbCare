package com.FinCore.SbFin.Repository;

import com.FinCore.SbFin.Entity.Transaction;
import com.FinCore.SbFin.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


        List<Transaction> findByFromAccountIdOrToAccountId(
                Long fromAccountId,
                Long toAccountId
        );

//        Transaction findByTransactionReference(String  transactionReference);

        Optional<Transaction> findByTransactionReference(String transactionReference);


}
