package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.profile.dto.user.UserAddRequest;

public interface ProfileService {

    void save(UserAddRequest userAddRequest);
}
