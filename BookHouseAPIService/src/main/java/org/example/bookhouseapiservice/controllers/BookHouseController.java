package org.example.bookhouseapiservice.controllers;

import org.example.bookhouseapiservice.dtos.AddBookRequest;
import org.example.bookhouseapiservice.models.BookEntity;
import org.example.bookhouseapiservice.models.BookHouseEntity;
import org.example.bookhouseapiservice.services.BookHouseService;
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
    public List<BookEntity> getBooks(@RequestParam Long bookHouseId) {
        try{
            return bookHouseService.getAllBooksFromBookHouse(bookHouseId);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    @GetMapping("/getBookHouse")
    public BookHouseEntity getBookHouse(@RequestParam Long bookHouseId) {
        try{
            BookHouseEntity bookHouse = bookHouseService.getBookHouseById(bookHouseId);
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
}
