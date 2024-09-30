package com.jinho.randb.domain.post.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

    @NotEmpty(message = "변경할 제목을 입력해주세요")
    private String postTitle;

    @NotBlank(message = "변경할 내용을 입력해주세요")
    private String postContent;

}
