package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.dto.user.UserAddRequest;

public interface PostService {
    void save(UserAddRequest userAddPostDto);
}
