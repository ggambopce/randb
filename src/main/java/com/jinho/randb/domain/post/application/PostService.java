package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.PostDetailResponse;
import com.jinho.randb.domain.post.dto.user.PostResponse;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(UserAddRequest userAddRequest);

    Optional<Post> findById(Long id);

    List<Post> findAll();

    void delete(Long postId);

    void update(Long postId, UserUpdateRequest userUpdatePostDto);



}
