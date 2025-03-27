package org.example.bookhousegui.config;

import org.example.bookhousegui.dtos.LoginResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RestTemplate restTemplate;

    public SecurityConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .successHandler(authenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authenticationProvider(customAuthProvider())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException {
                HttpSession session = request.getSession();

                // Retrieve authentication details
                Object details = authentication.getDetails();
                if (details instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> authDetails = (Map<String, Object>) details;

                    String token = (String) authDetails.get("token");
                    Long userId = (Long) authDetails.get("userId");

                    // Store token and userId in session
                    session.setAttribute("authToken", token);
                    session.setAttribute("userId", userId);
                }

                // Store username separately
                session.setAttribute("username", authentication.getName());

                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    public AuthenticationProvider customAuthProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                try {
                    LoginResponse response = restTemplate.postForObject(
                            "http://localhost:8080/login",
                            new org.example.bookhousegui.dtos.LoginRequest(username, password),
                            LoginResponse.class
                    );

                    if (response != null && response.getToken() != null) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                username,
                                password,
                                Collections.singletonList(new SimpleGrantedAuthority("USER"))
                        );
                        Map<String, Object> details = new HashMap<>();
                        details.put("token", response.getToken());
                        details.put("userId", response.getUserId());
                        auth.setDetails(details); // Store JWT token in details
                        return auth;
                    }
                    throw new RuntimeException("Invalid credentials");
                } catch (Exception e) {
                    throw new RuntimeException("Authentication failed: " + e.getMessage());
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }
}