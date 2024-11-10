package com.jinho.randb.domain.account.dao;

import com.jinho.randb.domain.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long>,CustomAccountRepository {
    Account findByLoginId(String loginId);

    Account findByUsername(String username);

    long countAllBy();

}
