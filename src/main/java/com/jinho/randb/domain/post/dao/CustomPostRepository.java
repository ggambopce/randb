package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.dto.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CustomPostRepository {

    Slice<PostDto> getPost(String postTitle, Long lastPostId, Pageable pageable);
}
