package com.jinho.randb.domain.post.domain;

import com.jinho.randb.domain.post.dto.PostDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_content", length = 1000)
    private String postContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
