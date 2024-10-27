package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class PostResponse {

    private Boolean nextPage;
    private List<PostDto> posts;

    public PostResponse(boolean nextPage, List<PostDto> posts) {
        this.nextPage = nextPage;
        this.posts = posts;
    }
}
