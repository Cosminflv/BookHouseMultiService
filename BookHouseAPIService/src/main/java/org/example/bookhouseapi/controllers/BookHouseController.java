package org.example.bookhouseapi.controllers;

import org.example.bookhouseapi.dtos.AddBookRequest;
import org.example.bookhouseapi.models.BookEntity;
import org.example.bookhouseapi.models.BookHouseEntity;
import org.example.bookhouseapi.services.BookHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/bookhouse")
public class BookHouseController {
    private final BookHouseService bookHouseService;

    @Autowired
    public BookHouseController(BookHouseService bookHouseService) {
        this.bookHouseService = bookHouseService;
    }

    // GET

    @GetMapping("/getBooks")
    public List<BookEntity> getBooks(@RequestBody Long bookHouseId) {
        try{
            return bookHouseService.getAllBooksFromBookHouse(bookHouseId);
        } catch (HttpClientErrorException e) {
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
}
