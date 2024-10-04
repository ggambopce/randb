package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.PostDto;
import com.jinho.randb.domain.post.dto.user.PostResponse;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.jinho.randb.domain.post.domain.QPost.post;

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
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }


    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public PostResponse searchPostsByPostTitle(String postTitle, Long lastPostId, Pageable pageable) {

        Slice<PostDto> Post = postRepository.getPost(postTitle, lastPostId, pageable);
        return new PostResponse(post.getPostContent(), post.hesNext());
    }

    @Override
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을수 없습니다."));
        postRepository.deleteById(post.getId());

    }

    @Override
    public void update(Long postId, UserUpdateRequest userUpdatePostDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        post.update(userUpdatePostDto.getPostTitle(), userUpdatePostDto.getPostContent());

        postRepository.save(post);

    }


}
