package com.jinho.randb.domain.post.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAddRequest {

    @NotEmpty(message = "제목을 입력해주세요")
    private String postTitle;

    @NotBlank(message = "내용을 입력해주세요")
    private String postContent;

}
