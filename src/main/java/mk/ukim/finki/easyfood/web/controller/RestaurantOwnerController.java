package mk.ukim.finki.easyfood.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestaurantOwnerController {

    @GetMapping("/ownerLogin")
    public String getOwnerLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/ownerPage")
    public String getOwnerDashboard(Model model) {
        return "restaurant_owner_dashboard";
    }
}
