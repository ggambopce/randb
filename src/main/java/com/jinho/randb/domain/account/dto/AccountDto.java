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
    @Schema(description = "사용자 실명",example = "홍길동")
    String username;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Schema(description = "비밀번호",example = "1234")
    String password;

    @Schema(description = "ROLE_USER",example = "ROLE_USER")
    String roles;

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDate join_date;

    public Account toEntity(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId())
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .roles(accountDto.getRoles())
                .join_date(accountDto.getJoin_date())
                .build();
    }

    private AccountDto(Long accountId, String username, String roles, LocalDate join_date) {
        this.id = accountId;
        this.username = username;
        this.roles = roles;
        this.join_date = join_date;
    }

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRoles()).build();
    }

    public static AccountDto of(Long accountId, String username, String roles, LocalDate join_date) {
        return new AccountDto(accountId, username, roles, join_date);
    }

}
