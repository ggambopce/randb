package com.jinho.randb.domain.account.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import com.jinho.randb.global.exception.ex.InvalidIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

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

    /**
     * 회원가입시 아이디 중복검사 및 아이디 조건체크(대소문자 구문)
     * @param loginId 회원가입시 정보
     * @return  아이디가 이미 사용 중이거나 조건이 불충분하면 false를 반환하고, 모두 만족시 true를 반환합니다
     */
    @Override
    public Map<String, Boolean> LoginIdValid(String loginId) {
        if (isLoginIdDuplicated(loginId) || accountRepository.findByCaseSensitiveLoginId(loginId) != null) {
            throw new InvalidIdException("사용할 수 없는 아이디입니다.");
        }
        return Map.of(USE_LOGIN_ID, true);
    }
    /**
     * 회원가입시 닉네임이 유효한지 확인합니다.
     * @param nickname 회원가입시 사용할 닉네임
     * @return 닉네임이 유효할 경우 true, 그렇지 않을 경우 false
     */
    @Override
    public void nickNameValid(String nickname) {
        boolean isNicknameValid = Pattern.compile("^[a-zA-Z0-9가-힣]{4,12}$").matcher(nickname).matches();
        if (!isNicknameValid || accountRepository.existsByNickname(nickname)) {
            throw new InvalidIdException("사용 불가능한 닉네임입니다.");
        }
    }


    /**
     * 이메일주소가 올바른 주소인지 확인
     * @param email 회원가입시의 이메일정보
     * @return  올바른이메일 주소시 true, 아닐시 false
     */
    @Override
    public Map<String, Boolean> emailValid(String email) {
        Map<String, Boolean> result = new LinkedHashMap<>();

        boolean isValidFormat =  Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.(com|net)$").matcher(email).matches();
        boolean isDuplicateEmail = isValidFormat && !isDuplicateEmail(email);
        boolean canUseEmail = isValidFormat;

        result.put(DUPLICATE_EMAIL, isDuplicateEmail);
        result.put(USE_EMAIL, canUseEmail);

        return result;
    }

    @Override
    public Map<String, String> ValidationErrorMessage(AccountDto accountDto) {
        return null;
    }



    /**
     * 주어진 회원 정보를 통해 비밀번호 중복여부를 체크하고, 결과를 반환합니다.
     * @param password 첫번쨰 비밀번호
     * @param passwordRe 다시 입력한 비밀번호
     * @return 비밀번호 일치여부를 Map<String,Boolean>타입으로 반환
     */
    public Map<String, Boolean> duplicatePassword(String password,String passwordRe) {
        Map<String, Boolean> result = new LinkedHashMap<>();

        result.put(DUPLICATE_PASSWORD, password.equals(passwordRe));
        return result;
    }

    /**
     * 이메일을 중복검사하는 메서드
     * 일반 사용자의 이메일의 대해서만 검사한다.
     */
    private boolean isDuplicateEmail(String email) {
        return accountRepository.findByEmail(email).stream()
                .anyMatch(account ->NORMAL_LOGIN_TYPE.equals(account.getLoginType()));
    }

    /**
     * 아이디 조건검사 중복검사
     */
    private boolean isLoginIdDuplicated(String loginId) {
        return  accountRepository.existsByLoginId(loginId);
    }
}
}
