package org.example.bookhouseauthservice;

import lombok.extern.slf4j.Slf4j;
import org.example.bookhouseauthservice.config.JwtUtils;
import org.example.bookhouseauthservice.dtos.AddUserRequest;
import org.example.bookhouseauthservice.dtos.LoginRequest;
import org.example.bookhouseauthservice.dtos.LoginResponse;
import org.example.bookhouseauthservice.models.UserEntity;
import org.example.bookhouseauthservice.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
public class UserAuthController  {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/addUser")
    public UserEntity addUser(@RequestBody AddUserRequest request) {

        log.info("Request password: " + request.getPassword());
        log.info("Request user: " + request.getUsername());
        log.info("Request user type: " + request.getUserType());

        System.out.println("Request password: " + request.getPassword());
        System.out.println("Request user: " + request.getUsername());
        System.out.println("Request user type: " + request.getUserType());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        userEntity.setUserType(request.getUserType());

        userRepo.save(userEntity);
        return userEntity;
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

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        List<UserEntity> foundUsers =  userRepo.findAll();
        return foundUsers;
    }
}
