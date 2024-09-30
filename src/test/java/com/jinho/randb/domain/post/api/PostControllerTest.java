package com.jinho.randb.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinho.randb.domain.post.application.PostServiceImpl;
import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import com.jinho.randb.domain.post.dto.user.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @MockBean
    private PostServiceImpl postService;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("사용자가 게시글 등록 API 테스트 ")
    void postAdd() throws Exception {

        // Given
        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setPostTitle("Test Title");
        userAddRequest.setPostContent("Test Content");

        // When
        Mockito.doNothing().when(postService).save(any(UserAddRequest.class));

        // Then
        mockMvc.perform(post("/api/user/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userAddRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("작성 성공"));
    }

    @Test
    @DisplayName("토론 게시글 목록 조회 테스트")
    void findAllPost() throws Exception {
        // Given
        Mockito.when(postService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("조회성공"));
    }

    @Test
    @DisplayName("토론 게시글 상세 조회 테스트")
    void findPost() throws Exception {
        // Given
        Post post = new Post(); // 생성자와 필드를 설정해야 함
        Mockito.when(postService.findById(anyLong())).thenReturn(Optional.of(post));

        // When & Then
        mockMvc.perform(get("/api/user/posts/{post-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("조회성공"));
    }

    @Test
    @DisplayName("토론 게시글 삭제 테스트")
    void deletePost() throws Exception {
        // Given
        Mockito.doNothing().when(postService).delete(anyLong());

        // When & Then
        mockMvc.perform(delete("/api/user/posts/{post-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("게시글 삭제 성공"));
    }

    @Test
    @DisplayName("토론 게시글 수정 테스트")
    void updatePost() throws Exception {
        // Given
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setPostTitle("수정된 제목");
        updateRequest.setPostContent("수정된 내용");

        // 서비스 레이어의 update 메서드를 모킹
        Mockito.doNothing().when(postService).update(anyLong(), any(UserUpdateRequest.class));

        // When & Then
        mockMvc.perform(post("/api/user/update/posts/{post-id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("토론글 수정 성공"));
    }
}