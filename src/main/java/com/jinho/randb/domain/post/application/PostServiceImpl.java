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

    /**
     * 게시글의모든 데이터를 무한 페이징 최신순으로 내림차순
     * @param pageable
     * @return
     */
    @Override
    public PostResponse postPage(Long postId, Pageable pageable) {

        Slice<PostDto> allPost = postRepository.getAllPost(postId, pageable);

        return new PostResponse(allPost.hasNext(), allPost.getContent());
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
