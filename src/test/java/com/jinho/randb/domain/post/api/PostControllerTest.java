package com.jinho.randb.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinho.randb.domain.post.application.PostServiceImpl;
import com.jinho.randb.domain.post.dto.user.UserAddRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @MockBean
    private PostServiceImpl postService;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("게시글 작성 API 테스트")
    void save_postAPI() throws Exception {
        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setPostTitle("제목");
        userAddRequest.setPostContent("컨텐트");

        String requestJson = objectMapper.writeValueAsString(userAddRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/posts")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 확인
                .andExpect(jsonPath("$.success").value(true)) // JSON 응답의 'success' 필드 값이 true인지 확인
                .andExpect(jsonPath("$.message").value("작성 성공")); // JSON 응답의 'message' 필드 값이 "작성 성공"인지 확인

    }
}