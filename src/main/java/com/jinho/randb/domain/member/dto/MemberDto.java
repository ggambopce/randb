package com.jinho.randb.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {

    Long id;

    String nickName;
    String password;
    String passwordRe;
    String email;
    String loginID;
    LocalDate joinDate;



}
