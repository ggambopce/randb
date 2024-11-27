package com.jinho.randb.domain.account.application;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class AccountServiceImpl implements AccountService{

    private static String LOGIN_TYPE = "normal";

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveDto(AccountDto accountDto) {

        Account account = Account.builder()
                .loginId(accountDto.getLoginId())
                .password(passwordEncoder.encode(accountDto.getPassword()))
                .username(accountDto.getUsername())
                .login_type(LOGIN_TYPE)
                .email(accountDto.getEmail())
                .join_date(LocalDate.now())
                .roles("ROLE_USER")
                .verified(true)
                .build();

        accountRepository.save(account);
    }
}
