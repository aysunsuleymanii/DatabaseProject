package mk.ukim.finki.easyfood.config;

import mk.ukim.finki.easyfood.service.impl.UserDetailsServiceImpl;
import mk.ukim.finki.easyfood.service.impl.RoleBasedRedirectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RoleConfiguration roleConfiguration;
    private final RoleBasedRedirectService redirectService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          PasswordEncoder passwordEncoder,
                          RoleConfiguration roleConfiguration,
                          RoleBasedRedirectService redirectService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.roleConfiguration = roleConfiguration;
        this.redirectService = redirectService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(this::configureAuthorizeRequests)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(redirectService::handleSuccessfulLogin)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }

    private void configureAuthorizeRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        // Public endpoints
        authz.requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**", "/error", "/home", "/")
                .permitAll();

        roleConfiguration.getConfigs().forEach((role, config) -> {
            config.getAllowedPaths().forEach(path -> {
                authz.requestMatchers(path).hasRole(role);
            });
        });

        // Catch all
        authz.anyRequest().authenticated();
    }
}