package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.dto.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class MainPagePostResponse {

    private List<PostDto> post;

    private  MainPagePostResponse(List<PostDto> list) {
        this.post = list;
    }

    public static MainPagePostResponse of(List<PostDto> list){
        return new MainPagePostResponse(list);
    }
}
