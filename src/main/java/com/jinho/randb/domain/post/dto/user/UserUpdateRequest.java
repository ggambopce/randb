package com.jinho.randb.domain.post.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

    @Schema(description = "수정할 토론글 제목", example = "토론 제목 수정!")
    @NotEmpty(message = "변경할 제목을 입력해주세요")
    private String postTitle;

    @Schema(description = "토론글 내용", example = "토론 내용 수정!")
    @NotBlank(message = "변경할 내용을 입력해주세요")
    private String postContent;

}
