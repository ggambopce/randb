package com.jinho.randb.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinho.randb.domain.member.dao.MemberRepository;
import com.jinho.randb.domain.post.application.PostServiceImpl;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("작성 성공"));
    }

    @Test
    void findAllPost() {
    }

    @Test
    void findPost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void updatePost() {
    }
}