package com.FinCore.SbFin.Repository;

import com.FinCore.SbFin.Entity.Account;
import com.FinCore.SbFin.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


}
