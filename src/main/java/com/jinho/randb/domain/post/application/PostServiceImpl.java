package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    @Override
    public void save(UserAddRequest userAddRequest) {

        Post post = Post.builder()
                .postTitle(userAddRequest.getPostTitle())
                .postContent(userAddRequest.getPostContent())
                .created_at(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }
}
