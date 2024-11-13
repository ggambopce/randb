package com.jinho.randb.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jinho.randb.domain.account.domain.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(nullable = true,hidden = true)
    Long id;

    @NotEmpty(message = "이름을 입력주세요")
    @Pattern(regexp = "^[가-힣]+.{1,}$",message = "이름을 정확이 입력해주세요")
    @Schema(description = "사용자 실명",example = "홍길동")
    String username;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Schema(description = "비밀번호",example = "1234")
    String password;

    @NotEmpty(message = "별명을 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,}$",message = "사용할수 없는 별명입니다.")
    @Schema(description = "사용자의 별명",example = "나만냉")
    String nickname;

    @NotEmpty(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$", message = "올바른 아이디를 입력해주세요")
    @Schema(description = "로그인 아이디",example = "exampleId")
    String loginId;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.(com|net)$", message = "올바른 이메일 형식이어야 합니다.")
    @Schema(description = "이메일",example = "test@naver.com")
    String email;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String roles;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate join_date;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String login_type;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean verified;

    public Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .roles(accountDto.getRoles())
                .join_date(accountDto.getJoin_date())
                .build();
    }

    private AccountDto(Long accountId, String loginId, String email, String username, String nickname, LocalDate join_date) {
        this.id = accountId;
        this.loginId = loginId;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.join_date = join_date;
    }

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .loginId(account.getLoginId())
                .username(account.getUsername())
                .nickname(account.getNickname())
                .password(account.getPassword())
                .email(account.getEmail())
                .roles(account.getRoles())
                .login_type(account.getLogin_type())
                .build();
    }

    public static AccountDto of(Long accountId, String loginId, String email, String username, String nickname, LocalDate join_date) {
        return new AccountDto(accountId, loginId, email, username, nickname, join_date);
    }

}
