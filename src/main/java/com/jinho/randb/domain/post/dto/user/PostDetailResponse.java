package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDetailResponse {

    private PostDto post;

    public PostDetailResponse(PostDto postDto) {
        this.post = postDto;
    }

    public  PostDetailResponse of(PostDto postDto){
        return new PostDetailResponse(postDto);
    }
}
