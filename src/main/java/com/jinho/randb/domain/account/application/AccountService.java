package com.jinho.randb.domain.account.application;


import com.jinho.randb.domain.account.dto.AccountDto;

public interface AccountService {

    void saveDto(AccountDto accountDto);

    AccountDto findByLoginId(String loginId);

    void deleteAccount(Long accountId);
}
