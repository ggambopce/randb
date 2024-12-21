package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.account.dao.AccountRepository;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.profile.dao.ProfileRepository;
import com.jinho.randb.domain.profile.domain.Profile;
import com.jinho.randb.domain.profile.dto.user.UserAddRequest;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchDataException;
import com.jinho.randb.global.exception.ex.nosuch.NoSuchErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    /**
     * 새로운 프로필 생성
     * @param userAddRequest - 프로필 생성 요청 DTO
     * @param accountId - 계정 ID
     */
    @Override
    public void save(UserAddRequest userAddRequest, Long accountId) {

        // Account 조회
        Account account = getAccount(accountId);

        // DTO -> Entity 변환
        Profile profile = userAddRequest.toEntity(account);

        // 프로필 저장
        profileRepository.save(profile);

    }

    /* 사용자 정보 조회 메서드*/
    private Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new NoSuchDataException(NoSuchErrorType.NO_SUCH_ACCOUNT));
    }
}
