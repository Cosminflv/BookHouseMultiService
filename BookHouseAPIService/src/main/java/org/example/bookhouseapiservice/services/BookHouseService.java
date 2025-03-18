package org.example.bookhouseapiservice.services;

import jakarta.transaction.Transactional;
import org.example.bookhouseapiservice.models.BookEntity;
import org.example.bookhouseapiservice.models.BookHouseEntity;
import org.example.bookhouseapiservice.models.UserEntity;
import org.example.bookhouseapiservice.repos.BookHouseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookHouseService {
    private final BookHouseRepository bookHouseRepository;

    public BookHouseService(BookHouseRepository bookHouseRepository) {
        this.bookHouseRepository = bookHouseRepository;
    }

    @Transactional
    public List<BookEntity> getAllBooksFromBookHouse(Long bookHouseId) {
        BookHouseEntity bookHouse = bookHouseRepository.findById(bookHouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BookHouse id: " + bookHouseId));
        return bookHouse.getBooks();
    }
    @Transactional
    public BookHouseEntity addBookHouse(BookHouseEntity bookHouse) {
        return bookHouseRepository.save(bookHouse);
    }

    @Transactional
    public BookHouseEntity addUserToBookHouse(Long bookHouseId, UserEntity user) {
        BookHouseEntity bookHouse = bookHouseRepository.findById(bookHouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BookHouse id: " + bookHouseId));

        // Set the bidirectional relationship
        user.setBookHouseEntity(bookHouse);

        // Initialize the list if it's null
        if (bookHouse.getUsers() == null) {
            bookHouse.setUsers(new ArrayList<>());
        }
        bookHouse.getUsers().add(user);

        return bookHouseRepository.save(bookHouse);
    }

    @Transactional
    public BookHouseEntity addBookToBookHouse(Long bookHouseId, BookEntity book) {
        BookHouseEntity bookHouse = bookHouseRepository.findById(bookHouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BookHouse id: " + bookHouseId));

        // Set the bidirectional relationship
        book.setBookHouseEntity(bookHouse);

        // Initialize the list if it's null
        if (bookHouse.getBooks() == null) {
            bookHouse.setBooks(new ArrayList<>());
        }
        bookHouse.getBooks().add(book);

        return bookHouseRepository.save(bookHouse);
    }
}
