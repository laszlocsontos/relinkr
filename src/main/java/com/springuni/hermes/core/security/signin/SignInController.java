package com.springuni.hermes.core.security.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignInController {

    static final String LOGIN_VIEW = "pages/login";

    @GetMapping("/login/oauth2")
    public String loginPage() {
        return LOGIN_VIEW;
    }

}
