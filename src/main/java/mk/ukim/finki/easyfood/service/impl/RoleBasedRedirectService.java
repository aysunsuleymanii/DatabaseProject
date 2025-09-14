package mk.ukim.finki.easyfood.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.easyfood.config.RoleConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RoleBasedRedirectService implements AuthenticationSuccessHandler {

    private final RoleConfiguration roleConfiguration;
    private final RoleManagerService roleManagerService;

    public RoleBasedRedirectService(RoleConfiguration roleConfiguration, RoleManagerService roleManagerService) {
        this.roleConfiguration = roleConfiguration;
        this.roleManagerService = roleManagerService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = determineRedirectUrl(authentication);
        response.sendRedirect(redirectUrl);
    }

    public void handleSuccessfulLogin(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException {
        onAuthenticationSuccess(request, response, authentication);
    }

    private String determineRedirectUrl(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return roleManagerService.getBestDashboardUrl(roles);
    }

}
