package com.jinho.randb.domain.account.api;


import com.jinho.randb.domain.account.application.AccountService;
import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto){

        accountService.signup(accountDto);

        return "redirect:/";
    }
}