package org.example.custom.sign;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class SignUpController {

    private final UserAccountService userAccountService;

    SignUpController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/signup")
    String view(Model model) {
        model.addAttribute("signUpForm", new SignUpForm("", ""));
        return "signup";
    }

    @PostMapping("/signup")
    String submit(@Valid @ModelAttribute("signUpForm") SignUpForm form,
                  BindingResult bindingResult,
                  HttpServletRequest request,
                  Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        try {
            UserAccount saved = userAccountService.register(form.email(), form.password());
            login(saved, request);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("email", "duplicate", ex.getMessage());
            return "signup";
        }
        return "redirect:/home";
    }

    private void login(UserAccount saved, HttpServletRequest request) {
        UserAccountDetails principal = new UserAccountDetails(saved);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
