package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponse {

    private PostDto post;

}
