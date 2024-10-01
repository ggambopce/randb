package com.jinho.randb.domain.post.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {

    @NotEmpty(message = "토론 제목을 입력해주세요")
    private String postTitle;

    @NotBlank(message = "토론 내용을 입력해주세요")
    private String postContent;


}
