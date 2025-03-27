package org.example.bookhouseapiservice.services;

import jakarta.transaction.Transactional;
import org.example.bookhouseapiservice.models.BorrowedBookEntity;
import org.example.bookhouseapiservice.models.UserEntity;
import org.example.bookhouseapiservice.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public Optional<UserEntity> getUser(Long userId) { return userRepository.findById(userId); }

    @Transactional
    public List<BorrowedBookEntity> getBorrowedBooks(Long userId) {
        Optional<UserEntity> foundUser = userRepository.findById(userId);

        if (foundUser.isPresent()) {
            List<BorrowedBookEntity> borrowedBooks = foundUser.get().getBorrowedBooks();
            return borrowedBooks;
        }

    return List.of();
    }
}
