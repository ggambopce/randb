package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.dao.PostRepository;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock PostRepository postRepository;

    @InjectMocks PostServiceImpl postService;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .postTitle("Test Title")
                .postContent("Test Content")
                .created_at(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    void save() {
        UserAddRequest request = new UserAddRequest("New Title", "New Content");
        Post newPost = Post.builder()
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent())
                .created_at(LocalDateTime.now())
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(newPost);

        postService.save(request);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    void findById() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Optional<Post> foundPost = postService.findById(1L);

        assertTrue(foundPost.isPresent());
        assertEquals(post.getPostTitle(), foundPost.get().getPostTitle());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("게시글 조회 실패 테스트")
    void findById_fail() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Post> foundPost = postService.findById(1L);

        assertFalse(foundPost.isPresent());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("모든 게시글 조회 테스트")
    void findAll() {
        List<Post> posts = Arrays.asList(post);
        when(postRepository.findAll()).thenReturn(posts);

        List<Post> allPosts = postService.findAll();

        assertEquals(1, allPosts.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void delete() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(1L);

        postService.delete(1L);

        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트")
    void delete_fail() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> postService.delete(1L));

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("게시글 업데이트 테스트")
    void update() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Updated Title", "Updated Content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        postService.update(1L, updateRequest);

        assertEquals(updateRequest.getPostTitle(), post.getPostTitle());
        assertEquals(updateRequest.getPostContent(), post.getPostContent());
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 업데이트 실패 테스트")
    void update_fail() {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Updated Title", "Updated Content");

        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> postService.update(1L, updateRequest));

        verify(postRepository, times(1)).findById(1L);
    }
}