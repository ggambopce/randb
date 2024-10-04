package com.jinho.randb.domain.post.dto.user;

import com.jinho.randb.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Boolean nextPage;

    private List<PostDto> postDtoList;


}
