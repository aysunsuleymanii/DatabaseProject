package mk.ukim.finki.easyfood.web.controller;

import mk.ukim.finki.easyfood.config.RoleConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class DashboardController {

    private final RoleConfiguration roleConfiguration;

    public DashboardController(RoleConfiguration roleConfiguration) {
        this.roleConfiguration = roleConfiguration;
    }

    @GetMapping("/dashboard/{role}")
    public String dashboard(@PathVariable String role, Authentication authentication, Model model) {
        // Verify user has the required role
        boolean hasRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_" + role.toUpperCase()));

        if (!hasRole) {
            return "redirect:/access-denied";
        }

        // Get role configuration
        RoleConfiguration.RoleConfig config = roleConfiguration.getConfigs().get(role.toUpperCase());
        if (config == null) {
            return "redirect:/home";
        }

        // Add role info to model
        model.addAttribute("role", role.toUpperCase());
        model.addAttribute("displayName", config.getDisplayName());
        model.addAttribute("allowedPaths", config.getAllowedPaths());

        // Return dynamic template based on role
        return String.format("dashboard/%s", role.toLowerCase());
    }

    // Catch-all dashboard endpoint that auto-detects role
    @GetMapping("/dashboard")
    public String autoDashboard(Authentication authentication) {
        Optional<String> primaryRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .filter(role -> roleConfiguration.getConfigs().containsKey(role))
                .findFirst();

        if (primaryRole.isPresent()) {
            return "redirect:/dashboard/" + primaryRole.get().toLowerCase();
        }

        return "redirect:/home";
    }
}
