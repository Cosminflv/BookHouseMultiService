package org.example.bookhousegui.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookhousegui.dtos.LoginRequest;
import org.example.bookhousegui.dtos.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

        @Value("${auth.service.url}")
        private String authServiceUrl;

    // Display the login form
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // Thymeleaf template: login.html
    }

    // Process the login form submission
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute("loginRequest") LoginRequest loginRequest,
                              Model model,
                              HttpServletRequest request) {
        String url = authServiceUrl + "/login";
        try {
            // Forward the login request to the API service
            LoginResponse loginResponse = restTemplate.postForObject(url, loginRequest, LoginResponse.class);

            // Optionally, store the token in the session for subsequent requests
            assert loginResponse != null;
            request.getSession().setAttribute("authToken", loginResponse.getToken());

            // Redirect to a secured page or home page after successful login
            return "redirect:/dashboard";
        } catch (HttpClientErrorException e) {
            // If login fails, add an error attribute to display the message in the view
            model.addAttribute("error", "Login failed: " + e.getResponseBodyAsString());
            return "login"; // Return back to the login page
        }
    }
}
