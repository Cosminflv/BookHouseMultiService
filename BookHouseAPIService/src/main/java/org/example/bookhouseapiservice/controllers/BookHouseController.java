package org.example.bookhouseapiservice.controllers;

import org.example.bookhouseapiservice.dtos.AddBookRequest;
import org.example.bookhouseapiservice.dtos.BookHouseBookDTO;
import org.example.bookhouseapiservice.dtos.BookHouseDTO;
import org.example.bookhouseapiservice.models.BookHouseEntity;
import org.example.bookhouseapiservice.models.BorrowedBookEntity;
import org.example.bookhouseapiservice.services.BookHouseService;
import org.example.bookhouseapiservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookhouse")
public class BookHouseController {
    private final BookHouseService bookHouseService;
    private final UserService userService;

    @Autowired
    public BookHouseController(BookHouseService bookHouseService, UserService userService) {
        this.bookHouseService = bookHouseService;
        this.userService = userService;
    }

    // GET

    @GetMapping("/getBooks")
    public List<BookHouseBookDTO> getBooks(@RequestParam Long bookHouseId) {
        try{
            return bookHouseService.getAllBooksFromBookHouse(bookHouseId);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @GetMapping("/getBookHouse")
    public BookHouseDTO getBookHouse(@RequestParam Long bookHouseId) {
        try{
            BookHouseDTO bookHouse = bookHouseService.getBookHouseDTOById(bookHouseId);
            return bookHouse;
        } catch(HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    // POST

    @PostMapping("/addBookhouse")
    public BookHouseEntity addBookhouse(@RequestBody BookHouseEntity bookHouse) {
        try{
            return bookHouseService.addBookHouse(bookHouse);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @PostMapping("/addBook")
    public BookHouseEntity addBook(@RequestBody AddBookRequest request) {
        try{
            return bookHouseService.addBookToBookHouse(request.getBookHouseId(), request.getBook());
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @PostMapping("/borrowBook")
    public Optional<BorrowedBookEntity> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
        return userService.getUser(userId).flatMap(user ->
                bookHouseService.getBook(user.getBookHouseEntity().getId(), bookId).map(book -> {
                    BorrowedBookEntity borrowedBook = new BorrowedBookEntity();
                    borrowedBook.setUser(user);
                    borrowedBook.setBook(book);
                    borrowedBook.setReturned(false);
                    borrowedBook.setBorrowedAt(LocalDateTime.now());
                    borrowedBook.setReturnDate(LocalDateTime.now().plusMonths(1));
                    user.getBorrowedBooks().add(borrowedBook);
                    bookHouseService.decreaseBookStock(user.getBookHouseEntity().getId(), bookId);
                    return borrowedBook;
                })
        );
    }
}
