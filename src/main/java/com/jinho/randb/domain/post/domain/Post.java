package com.jinho.randb.domain.post.domain;

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

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public void update(String postTitle, String postContent){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.updated_at = LocalDateTime.now().withNano(0).withSecond(0);
    }

}
