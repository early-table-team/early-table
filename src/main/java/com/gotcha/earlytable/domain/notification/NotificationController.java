package com.gotcha.earlytable.domain.notification;

import com.gotcha.earlytable.global.annotation.CheckUserAuth;
import com.gotcha.earlytable.global.enums.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class NotificationController {

    @CheckUserAuth(requiredAuthorities = {Auth.USER})
    @GetMapping("/")
    public String home() {
        return "index.html";
    }
}
