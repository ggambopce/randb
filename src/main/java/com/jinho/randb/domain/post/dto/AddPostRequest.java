package com.jinho.randb.domain.post.dto;

import com.jinho.randb.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddPostRequest {

    private Long id;
    private String postTitle;
    private String postContent;

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .postTitle(postTitle)
                .postContent(postContent)
                .build();
    }
}


