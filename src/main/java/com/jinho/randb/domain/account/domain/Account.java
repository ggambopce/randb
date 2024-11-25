package com.jinho.randb.domain.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jinho.randb.domain.opinion.domain.Opinion;
import com.jinho.randb.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Column(unique = true)
    String loginId;
    String email;
    String nickname;
    @JsonIgnore
    @Column(nullable = false, columnDefinition = "varchar(255) default 'ROLE_USER'")
    String roles;
    String login_type;

    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    LocalDate join_date;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean verified;


    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Post> posts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    List<Opinion> opinions = new ArrayList<>();

    public List<String> getRoleList() {
        if (this.roles != null && this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
