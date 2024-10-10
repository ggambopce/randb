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

    @ManyToOne
    @JoinColumn(name = "member_id")  // 외래키 설정
    private Member member;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(String postTitle, String postContent){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.updatedAt = LocalDateTime.now().withNano(0).withSecond(0);
    }

}
