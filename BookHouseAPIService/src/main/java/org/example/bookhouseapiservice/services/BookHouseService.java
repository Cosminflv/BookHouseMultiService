package org.example.bookhouseapiservice.services;

import jakarta.transaction.Transactional;
import org.example.bookhouseapiservice.dtos.BookHouseBookDTO;
import org.example.bookhouseapiservice.dtos.BookHouseDTO;
import org.example.bookhouseapiservice.dtos.BookHouseUserDTO;
import org.example.bookhouseapiservice.models.BookEntity;
import org.example.bookhouseapiservice.models.BookHouseEntity;
import org.example.bookhouseapiservice.models.BorrowedBookEntity;
import org.example.bookhouseapiservice.models.UserEntity;
import org.example.bookhouseapiservice.repos.BookHouseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookHouseService {
    private final BookHouseRepository bookHouseRepository;

    public BookHouseService(BookHouseRepository bookHouseRepository) {
        this.bookHouseRepository = bookHouseRepository;
    }

    @Transactional
    public List<BookHouseBookDTO> getAllBooksFromBookHouse(Long bookHouseId) {
        BookHouseEntity bookHouse = bookHouseRepository.findById(bookHouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid BookHouse id: " + bookHouseId));

        List<BookHouseBookDTO> bookDTOs = bookHouse.getBooks().stream()
                .map(bookEntity -> {
                    BookHouseBookDTO bookDTO = new BookHouseBookDTO();
                    bookDTO.setBookHouseEntityId(bookEntity.getId());
                    bookDTO.setTitle(bookEntity.getTitle());
                    bookDTO.setAuthor(bookEntity.getAuthor());
                    bookDTO.setIsbn(bookEntity.getIsbn());
                    bookDTO.setTotalStock(bookEntity.getTotalStock());
                    bookDTO.setId(bookEntity.getId());
                    return bookDTO;
                }).toList();


        return bookDTOs;
    }

    public Optional<BookHouseEntity> getBookHouseById(Long id) {
        return bookHouseRepository.findById(id);
    }

    public BookHouseDTO getBookHouseDTOById(Long bookHouseId) {
        BookHouseEntity entity = bookHouseRepository.findById(bookHouseId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "BookHouse not found"));

        // Map entity to DTO (example using manual mapping)
        BookHouseDTO dto = new BookHouseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocation(entity.getLocation());

        // Map users (Entity → UserDTO)
        List<BookHouseUserDTO> userDTOs = entity.getUsers().stream()
                .map(user -> {
                    List<Long> subscriptionIds = new ArrayList<>();
                    List<Long> borrowedBookIds = new ArrayList<>();
                    subscriptionIds.add(user.getBookHouseEntity().getId());
                    for(BorrowedBookEntity borrowedBookEntity : user.getBorrowedBooks()){
                        borrowedBookIds.add(borrowedBookEntity.getId());
                    }
                    BookHouseUserDTO userDTO = new BookHouseUserDTO();
                    userDTO.setId(user.getId());
                    userDTO.setUsername(user.getUsername());
                    userDTO.setBookHouseSubscriptionIds(subscriptionIds);
                    userDTO.setBorrowedBooksIds(borrowedBookIds);
                    return userDTO;
                })
                .collect(Collectors.toList());
        dto.setUsers(userDTOs);

        // Map books similarly (Entity → BookDTO)
        List<BookHouseBookDTO> bookDTOs = entity.getBooks().stream()
                .map(book -> {
                    BookHouseBookDTO bookDTO = new BookHouseBookDTO();
                    bookDTO.setId(book.getId());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setAuthor(book.getAuthor());
                    bookDTO.setIsbn(book.getIsbn());
                    bookDTO.setTotalStock(book.getTotalStock());
                    bookDTO.setBookHouseEntityId(book.getBookHouseEntity().getId());
                    return bookDTO;
                })
                .collect(Collectors.toList());
        dto.setBooks(bookDTOs);

        return dto;
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

    @Transactional
    public Optional<BookEntity> getBook(Long bookHouseId, Long bookId) {
        Optional<BookHouseEntity> foundBookHouse = bookHouseRepository.findById(bookHouseId);

        return foundBookHouse.flatMap(bookHouseEntity -> bookHouseEntity
                .getBooks()
                .stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst());
    }

    @Transactional
    public void decreaseBookStock(Long bookHouseId, Long bookId) {
        Optional<BookHouseEntity> foundBookHouse = bookHouseRepository.findById(bookHouseId);

        if (foundBookHouse.isEmpty()) return;

        BookHouseEntity bookHouse = foundBookHouse.get();

        Optional<BookEntity> bookOptional = bookHouse.getBooks().stream()
                .filter(book -> book.getId().equals(bookId))
                .findFirst();

        if (bookOptional.isPresent()) {
            BookEntity book = bookOptional.get();

            if (book.getTotalStock() > 0) {
                book.setTotalStock(book.getTotalStock() - 1);
                bookHouseRepository.save(bookHouse); // Save the changes
            } else {
                // Stock is already zero, cannot decrease further
            }
        }

    }
}
