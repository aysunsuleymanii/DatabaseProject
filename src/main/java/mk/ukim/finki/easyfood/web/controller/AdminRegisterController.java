package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.enumerations.ROLE;
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Controller
@RequestMapping("/admin/register")
public class AdminRegisterController {
    private final UserService userService;

    public AdminRegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAdminRegisterPage(@RequestParam(value = "error", required = false) String error,
                                       @RequestParam(value = "success", required = false) String success,
                                       Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", success);
        }

        model.addAttribute("roles", Arrays.asList(ROLE.values()));

        return "admin_register";
    }

    @PostMapping
    public String adminRegister(@RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam String password,
                                @RequestParam String repeatedPassword,
                                @RequestParam ROLE role) {
        try {
            this.userService.registerUserWithRole(fullName, email, phoneNumber, password, repeatedPassword, role);

            String successMsg = URLEncoder.encode("Successfully registered " +
                            role.toString().toLowerCase().replace("_", " ") + ": " + fullName,
                    StandardCharsets.UTF_8);
            return "redirect:/admin/register?success=" + successMsg;
        } catch (RuntimeException ex) {
            String errorMsg = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/admin/register?error=" + errorMsg;
        }
    }
}