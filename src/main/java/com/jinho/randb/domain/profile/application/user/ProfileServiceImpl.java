package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.profile.dto.user.UserAddRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService{


    @Override
    public void save(UserAddRequest userAddRequest) {


    }
}
