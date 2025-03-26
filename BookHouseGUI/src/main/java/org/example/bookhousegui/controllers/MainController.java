package org.example.bookhousegui.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Check if user is authenticated
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }

        // Add dashboard data to model
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("stats", Map.of(
                "totalBooks", 142,
                "activeUsers", 234,
                "pendingOrders", 9,
                "availableCopies", 24
        ));

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}