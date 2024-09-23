package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public void save(UserAddRequest userAddPostDto) {
        Post post = Post.builder()
                .postTitle(userAddPostDto.getPostTitle())
                .postContent(userAddPostDto.getPostContent())
                .createdAt(LocalDateTime.now())
                .build();
        postRepository.save(post);
    }

}
