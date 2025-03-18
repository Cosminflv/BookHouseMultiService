package org.example.bookhouseapiservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookhouseapiservice.dtos.LoginRequest;
import org.example.bookhouseapiservice.dtos.LoginResponse;
import org.example.bookhouseapiservice.models.UserEntity;
import org.example.bookhouseapiservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RestTemplate restTemplate;
    //private static final String AUTH_SERVICE_URL = "http://localhost:8080";

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Autowired
    public UserController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public UserEntity addUser(@RequestBody UserEntity user, HttpServletRequest request) {
        // Extract the token from the incoming request header
        String token = request.getHeader("Authorization");

        // Create headers for the outgoing request and add the Authorization header
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", token);
        }

        // Create the HttpEntity including both the user data and headers
        HttpEntity<UserEntity> entity = new HttpEntity<>(user, headers);

        // Define the URL of the downstream service
        String url = authServiceUrl + "/addUser";
        try {

            UserEntity userResponse = restTemplate.postForObject(url, entity, UserEntity.class);

            userService.saveUser(userResponse);

            return userResponse;
        } catch(HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage());
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String url = authServiceUrl + "/login";
        try {
            return restTemplate.postForObject(url, request, LoginResponse.class);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }
}
