package com.jinho.randb.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.member.dto.MemberDto;
import com.jinho.randb.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {

    private Long id;

    private String postTitle;

    private String postContent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostDto(Long id, String loginId, String postTitle, String postContent, LocalDateTime createdAt) {
        this.id = id;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.createdAt = createdAt;
    }

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
