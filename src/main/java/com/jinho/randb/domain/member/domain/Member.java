package com.jinho.randb.domain.member.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {

    @Id@GeneratedValue
    @Column(name = "member_id")
    Long id;

    @Column(unique = true)
    String email;

    @Column(unique = true)
    String nickName;

    String password;

    private boolean emailVerified;

    private String emailCheckToken;

    LocalDate joined_at;



    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Post> posts = new ArrayList<>();
}
