package com.jinho.randb.domain.post.dao;

import com.jinho.randb.domain.post.dto.PostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomPostRepository {
    Slice<PostDto> getAllPost(Long postId, Pageable pageable);
}
