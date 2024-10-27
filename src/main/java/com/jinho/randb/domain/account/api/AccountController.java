package com.jinho.randb.domain.account.api;


import com.jinho.randb.domain.account.application.AccountService;
import com.jinho.randb.domain.account.domain.Account;
import com.jinho.randb.domain.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public String signup(AccountDto accountDto){

        Account account = accountDto.toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        accountService.signup(accountDto);

        return "redirect:/main";
    }



}