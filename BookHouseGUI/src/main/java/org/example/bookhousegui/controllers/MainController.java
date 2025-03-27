package org.example.bookhousegui.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.bookhousegui.models.BookHouseEntity;
import org.example.bookhousegui.models.UserEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class MainController {

    final RestTemplate restTemplate;

    public MainController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Check if user is authenticated
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }

        String token = session.getAttribute("authToken").toString();

        // Create headers for the outgoing request and add the Authorization header
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }

        long userId = 1;
        long bookHouseId = 1; // Example ID

        BookHouseEntity bookHouse = restTemplate.exchange(
                "http://localhost:8081/bookhouse/getBookHouse?bookHouseId={bookHouseId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<BookHouseEntity>() {},
                bookHouseId
        ).getBody();

        UserEntity user = restTemplate.exchange(
                "http://localhost:8081/bookhouse/getUser?userId={userId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<UserEntity>() {
                },
                userId
        ).getBody();

        model.addAttribute("bookHouse", bookHouse);
        model.addAttribute("currentUser", user);

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}