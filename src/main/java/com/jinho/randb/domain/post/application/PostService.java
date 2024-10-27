package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(UserAddRequest userAddPostDto);

    PostResponse postPage(Long postId, Pageable pageable);

    Optional<Post> findById(Long id);

    PostDetailResponse getPostDetail(Long postId);

    List<Post> findAll();

    MainPagePostResponse mainPagePost();

    void delete(String username, Long postId);

    void update(Long postId, UserUpdateRequest userUpdatePostDto, String username);



}
