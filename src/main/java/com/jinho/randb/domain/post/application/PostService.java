package com.jinho.randb.domain.post.application;

import com.jinho.randb.domain.post.domain.Post;
import com.jinho.randb.domain.post.domain.PostStatistics;
import com.jinho.randb.domain.post.domain.PostType;
import com.jinho.randb.domain.post.dto.PostStatisticsResponseDto;
import com.jinho.randb.domain.post.dto.user.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void save(UserAddRequest userAddPostDto);

    PostResponse postPage(Long postId, Pageable pageable);

    Optional<Post> findById(Long id);

    PostDetailResponse getPostDetail(Long postId);

    List<Post> findAll();

    MainPagePostResponse mainPagePost();

    void delete(Long postId);

    void update(Long postId, UserUpdateRequest userUpdatePostDto);

    void updatePostType(Long postId, PostType newType);


    PostStatistics completePost(Long postId);

    PostStatisticsResponseDto getPostStatistics(Long id);
}
