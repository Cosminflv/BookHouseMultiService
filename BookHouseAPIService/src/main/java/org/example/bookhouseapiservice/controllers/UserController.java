package org.example.bookhouseapiservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.bookhouseapiservice.dtos.AddUserRequest;
import org.example.bookhouseapiservice.dtos.BorrowedBookEntityDTO;
import org.example.bookhouseapiservice.models.BorrowedBookEntity;
import org.example.bookhouseapiservice.models.UserEntity;
import org.example.bookhouseapiservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.example.bookhouseapiservice.utils.Converters.convertBorrowedBookToDTO;

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

    @GetMapping("/borrowedBooks")
    public List<BorrowedBookEntityDTO> getBorrowedBooks(@RequestParam Long userId) {
        List<BorrowedBookEntity> borrowedBooks = userService.getBorrowedBooks(userId);

        List<BorrowedBookEntityDTO> borrowedBooksDTO = new ArrayList<>();

        for(BorrowedBookEntity entity : borrowedBooks) {
            BorrowedBookEntityDTO dto = convertBorrowedBookToDTO(entity);
            borrowedBooksDTO.add(dto);
        }

        return borrowedBooksDTO;
    }

        @PostMapping("/addUser")
        public UserEntity addUser(@RequestBody AddUserRequest req, HttpServletRequest request) {
            // Extract the token from the incoming request header
            String token = request.getHeader("Authorization");

            // Create headers for the outgoing request and add the Authorization header
            HttpHeaders headers = new HttpHeaders();
            if (token != null && !token.isEmpty()) {
                headers.set("Authorization", token);
            }

            // Create the HttpEntity including both the user data and headers
            HttpEntity<AddUserRequest> entity = new HttpEntity<>(req, headers);

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

    @GetMapping("/users")
    public List<UserEntity> getAllUsers(HttpServletRequest request) {
        String url = authServiceUrl + "/users";

        // Extract the token from the incoming request header
        String token = request.getHeader("Authorization");

        // Create headers for the outgoing request and add the Authorization header
        HttpHeaders headers = new HttpHeaders();
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", token);
        }

        try {
            ResponseEntity<List<UserEntity>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<UserEntity>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @GetMapping("/user")
    public UserEntity getUser(@RequestParam Long userId) {
        return userService.getUser(userId);
    }
}
