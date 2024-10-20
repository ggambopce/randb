package com.jinho.randb.domain.account.domain;

import com.jinho.randb.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    private String roles;

    LocalDate join_date;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Post> posts = new ArrayList<>();


}
