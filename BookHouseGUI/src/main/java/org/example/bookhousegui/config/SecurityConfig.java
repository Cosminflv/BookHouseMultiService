package org.example.bookhousegui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Allow all users to trigger logout
        http
                // Configure authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to the login page and static resources without authentication
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )
                // Configure form login
                .formLogin(form -> form
                        // Set the login page URL
                        .loginPage("/login")
                        // Define the default success URL after a successful login
                        .defaultSuccessUrl("/dashboard", true)
                        // Allow everyone to see the login page
                        .permitAll()
                )
                // Configure logout
                .logout(LogoutConfigurer::permitAll
                )
                // Disable CSRF for simplicity (adjust as needed for your application)
                .csrf(csrf -> csrf.disable())
                // Use default HTTP Basic authentication (optional, remove if not needed)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
