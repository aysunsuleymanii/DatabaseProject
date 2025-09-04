package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.model.AppUser;
import mk.ukim.finki.easyfood.model.DeliveryMan; // You need to import this
import mk.ukim.finki.easyfood.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

@Controller
public class PostLoginController {

    private final UserService userService;

    public PostLoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/post-login")
    public String postLoginRedirect(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Your logic for admin
            return "redirect:/admin";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELIVERY_MAN"))) {
            // Use the specific findByEmail for DeliveryMan
            DeliveryMan deliveryMan = userService.findDeliveryManByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Delivery man not found."));
            Long userId = deliveryMan.getId();
            return "redirect:/DeliveryMan/" + userId;
        } else {
            // Your logic for other roles or regular users
            AppUser user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
            return "redirect:/home";
        }
    }
}