package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage() {
        return "registe_customer";
    }

    @PostMapping
    public String register(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String phoneNumber,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword) {
        try {
            this.userService.register(fullName, email, phoneNumber, password, repeatedPassword);
            return "redirect:/home";
        } catch (RuntimeException ex) {
            String errorMsg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/register?error=" + errorMsg;
        }
    }
}


