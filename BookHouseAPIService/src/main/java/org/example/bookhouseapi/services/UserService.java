package org.example.bookhouseapi.services;

import jakarta.transaction.Transactional;
import org.example.bookhouseapi.models.UserEntity;
import org.example.bookhouseapi.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }
}
