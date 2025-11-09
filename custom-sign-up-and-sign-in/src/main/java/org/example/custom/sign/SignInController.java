package org.example.custom.sign;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class SignInController {

    @GetMapping("/signin")
    String view() {
        return "signin";
    }
}
