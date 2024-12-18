package com.jinho.randb.domain.account.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 필드명 상태값 상수로 정의
    private static final String DUPLICATE_EMAIL = "duplicatrEmail";
    private static final String EMAIL = "email";
    private static final String USE_LOGIN_ID = "useLoginId";
    private static final String PASSWORD_RE = "passwordRe";
    private static final String LOGIN_ID = "loginId";
    private static final String CODE = "code";
    private static final String DUPLICATE_PASSWORD = "duplicate_password";
    private static final String IS_LOGIN_VALID = "isLoginValid";
    private static final String USE_EMAIL = "useEmail";
    private static final String NORMAL_LOGIN_TYPE = "normal";

    @Override
    public void joinAccount(AccountDto accountDto) {

        accountDto.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account entity = AccountDto.toEntity(accountDto); // accountDto -> account 변환
        accountRepository.save(entity);
    }

    @Override
    public boolean ValidationOfSignUp(AccountDto accountDto) {
        return false;
    }

    @Override
    public Map<String, Boolean> LoginIdValid(String loginId) {
        return null;
    }

    @Override
    public Map<String, Boolean> emailValid(String email) {
        return null;
    }

    @Override
    public void nickNameValid(String nickName) {

    }

    @Override
    public Map<String, String> ValidationErrorMessage(AccountDto accountDto) {
        return null;
    }
}
