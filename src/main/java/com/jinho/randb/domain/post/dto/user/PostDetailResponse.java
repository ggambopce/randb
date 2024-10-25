package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDetailResponse {

    private PostDto post;
    private boolean isAuthor; // isAuthor 필드 추가

    public PostDetailResponse(PostDto postDto) {
        this.post = postDto;
    }

    public static PostDetailResponse of(PostDto postDto){
        return new PostDetailResponse(postDto);
    }


}
