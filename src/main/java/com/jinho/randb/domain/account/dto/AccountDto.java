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
@Schema(name="사용자 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Schema(nullable = true,hidden = true)
    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String passwordRe;

    private String loginId;

    private String email;

    @JsonIgnore
    private String roles;

    @JsonIgnore
    private LocalDate join_date;

    @JsonIgnore
    private String login_type;

    @JsonIgnore
    private boolean verified;

    @JsonIgnore
    private Integer code;

    public Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .loginId(accountDto.getLoginId())
                .password(accountDto.getPassword())
                .email(accountDto.getEmail())
                .join_date(LocalDate.now())
                .nickName(accountDto.getNickname())
                .username(accountDto.getUsername())
                .join_date(accountDto.getJoin_date())
                .roles("ROLE_USER")
                .login_type("normal")
                .verified(true)
                .build();
    }

    private AccountDto(Long accountId, String loginId, String email, String username, LocalDate join_date) {
        this.id = accountId;
        this.loginId = loginId;
        this.email = email;
        this.username = username;
        this.join_date = join_date;
    }

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .loginId(account.getLoginId())
                .username(account.getUsername())
                .password(account.getPassword())
                .email(account.getEmail())
                .roles(account.getRoles())
                .login_type(account.getLogin_type())
                .build();
    }

    public static AccountDto of(Long accountId, String loginId, String email, String username, LocalDate join_date) {
        return new AccountDto(accountId, loginId, email, username, join_date);
    }

}
