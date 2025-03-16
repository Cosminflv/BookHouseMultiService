package org.example.bookhouseauthservice.dtos;

import lombok.Getter;

@Getter
public class LoginRequest {
    private final String username;
    private final String password;

    // Getters and Setters
    public LoginRequest(String username, String password) { this.username = username; this.password = password; }

}
