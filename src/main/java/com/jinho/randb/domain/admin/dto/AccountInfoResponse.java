package com.jinho.randb.domain.admin.dto;

import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountInfoResponse {

    private List<AccountDto> accountIndos;
    private Boolean nextPage;
}
