package com.jinho.randb.global.security.api;

import com.jinho.randb.domain.account.dto.AccountContext;
import com.jinho.randb.global.security.details.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping(value="/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            // AccountContext 사용
            AccountContext accountContext = (AccountContext) authentication.getPrincipal();
            String username = accountContext.getUsername();  // 로그인된 사용자의 이름 가져오기
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "Guest");
        }
        return "/rest/main";
    }

    @GetMapping(value="/user")
    public String user() {
        return "/user";
    }

    @GetMapping(value="/manager")
    public String manager() {
        return "/manager";
    }

    @GetMapping(value="/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping(value="/api")
    public String restDashboard() {
        return "rest/main";
    }
}
