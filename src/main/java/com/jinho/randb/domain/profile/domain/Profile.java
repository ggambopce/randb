package com.jinho.randb.domain.profile.domain;

import com.jinho.randb.domain.account.domain.Account;
import io.micrometer.core.annotation.Counted;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "gender")
    private Gender gender; // 성별

    @Column(name = "age")
    private LocalDate age; // 만 나이

    @Column(name = "bio", length = 500)
    private String bio; // 자기소개

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acocunt_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Account account;

    @Column(name = "instagram_url")
    private String instagramUrl; // 인스타그램 URL

    @Column(name = "blog_url")
    private String blogUrl; // 블로그 URL

    @Column(name = "youtube_url")
    private String youtubeUrl; // 유튜브 URL
}
