package com.jinho.randb.domain.profile.application.user;

import com.jinho.randb.domain.profile.dto.request.UserAddRequest;
import com.jinho.randb.domain.profile.dto.request.UserUpdateRequest;
import com.jinho.randb.domain.profile.dto.response.ProfileDetailResponse;

public interface ProfileService {

    void save(UserAddRequest userAddRequest, Long accountId);

    void update(Long profileId, Long accountId, UserUpdateRequest userUpdateRequest);

    ProfileDetailResponse detailProfile(Long profileId);
}
