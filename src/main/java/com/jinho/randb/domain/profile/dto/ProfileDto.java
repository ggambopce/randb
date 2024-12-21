package com.jinho.randb.domain.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.profile.domain.Gender;
import com.jinho.randb.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "프로필 DTO")
public class ProfileDto {

    private Long id;

    private Gender gender;

    private LocalDate age;

    private String bio;

    private String instagramUrl;

    private String blogUrl;

    private String youtubeUrl;

    private LocalDate createdAt;

    private LocalDateTime updatedAt;


}
