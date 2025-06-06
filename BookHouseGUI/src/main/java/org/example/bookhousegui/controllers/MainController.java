package org.example.bookhousegui.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.bookhousegui.dtos.BookHouseDTO;
import org.example.bookhousegui.dtos.UserEntityDTO;
import org.example.bookhousegui.models.BookHouseEntity;
import org.example.bookhousegui.models.BorrowedBookEntity;
import org.example.bookhousegui.models.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.example.bookhousegui.utils.Converters.convertBookHouseToEntity;
import static org.example.bookhousegui.utils.Converters.convertUserToEntity;


@Controller
public class MainController {

    final RestTemplate restTemplate;

    @Value("${api.base.url}")
    private String apiServiceUrl;

    public MainController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Check if user is authenticated
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }

        // Retrieve token and userId from session
        String token = (String) session.getAttribute("authToken");
        Long userId = (Long) session.getAttribute("userId"); // Fix: Retrieve userId properly

        // Create headers for the outgoing request and add the Authorization header
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }

        long bookHouseId = 1; // Example ID

        BookHouseDTO bookHouseDto = restTemplate.exchange(
                apiServiceUrl + "/bookhouse/getBookHouse?bookHouseId={bookHouseId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                BookHouseDTO.class, // Use the DTO class here
                bookHouseId
        ).getBody();

        // Convert DTO to your GUI's BookHouseEntity (see Step 3)
        BookHouseEntity bookHouse = convertBookHouseToEntity(bookHouseDto);

        model.addAttribute("bookHouse", bookHouse);

        UserEntityDTO userDTO = restTemplate.exchange(
                apiServiceUrl + "/users/user?userId={userId}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<UserEntityDTO>() {},
                userId
        ).getBody();

        UserEntity userEntity = convertUserToEntity(userDTO);
        Long userIdRetrieved = userEntity.getId();

        List<BorrowedBookEntity> borrowedBooks = restTemplate.exchange(
                apiServiceUrl + "/users/borrowedBooks?userId={userIdRetrieved}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<BorrowedBookEntity>>() {},
                userIdRetrieved
        ).getBody();

        userEntity.setBorrowedBooks(borrowedBooks);

        model.addAttribute("bookHouse", bookHouse);
        model.addAttribute("currentUser", userEntity);

        return "dashboard";
    }

    @PostMapping("/borrowBook")
    public String borrowBook(@RequestParam("bookId") Long bookId, Model model, HttpSession session) {
        // Check if user is authenticated
        if (session.getAttribute("authToken") == null) {
            return "redirect:/login";
        }

        // Retrieve token and userId from session
        String token = (String) session.getAttribute("authToken");
        Long userId = (Long) session.getAttribute("userId");

        // Create headers and add Authorization
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }

        // Call the API service to borrow the book using POST request
        restTemplate.exchange(
                apiServiceUrl + "/bookhouse/borrowBook?userId={userId}&bookId={bookId}",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                Void.class,
                userId, bookId
        );

        // Optionally, add any messages to the model here (e.g., success message)

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}