package com.jinho.randb.global.jwt.dto;

import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.Data;

@Data
public class AccountInfoResponse {

    private Long id;
    private String loginId;
    private String nickname;
    private String loginType;
    private String roles;

    private AccountInfoResponse(Long id, String loginId, String nickname, String loginType, String roles) {
        this.id = id;
        this.loginId = loginId;
        this.nickname = nickname;
        this.loginType = loginType;
        this.roles = roles;
    }

    public static AccountInfoResponse of(AccountDto accountDto){
        return new AccountInfoResponse(accountDto.getId(), accountDto.getLoginId(), accountDto.getNickname(), accountDto.getLogin_type(), accountDto.getRoles());
    }
}
