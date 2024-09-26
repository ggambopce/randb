package com.jinho.randb.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    private String postTitle;

    private String postContent;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
