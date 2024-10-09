package com.jinho.randb.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    public PostDto toDto() {
        return new PostDto(id, postTitle, postContent);
    }
    // 정적 팩토리 메서드
    public static PostDto from(Long id, String postTitle, String postContent){
        return new PostDto(id, postTitle, postContent);
    }

    public static PostDto of(Post post){
        return new PostDto(post.getId(), post.getPostTitle(), post.getPostContent());
    }



}
