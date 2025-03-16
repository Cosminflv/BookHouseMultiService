package org.example.bookhouseauthservice;

import org.example.bookhouseauthservice.config.JwtUtils;
import org.example.bookhouseauthservice.dtos.LoginRequest;
import org.example.bookhouseauthservice.dtos.LoginResponse;
import org.example.bookhouseauthservice.models.UserEntity;
import org.example.bookhouseauthservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserAuthController  {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/addUser")
    public UserEntity addUser(@RequestBody UserEntity user) {
        userRepo.save(user);
        return user;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        UserEntity foundUser = userRepo.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (foundUser == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }
        String token = jwtUtils.generateToken(foundUser.getUsername());
        return new LoginResponse(token);
    }
}
