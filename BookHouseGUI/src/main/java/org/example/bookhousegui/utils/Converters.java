package org.example.bookhousegui.utils;

import org.example.bookhousegui.dtos.BookHouseDTO;
import org.example.bookhousegui.dtos.UserEntityDTO;
import org.example.bookhousegui.models.BookEntity;
import org.example.bookhousegui.models.BookHouseEntity;
import org.example.bookhousegui.models.UserEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    private Converters() {
    }

    public static BookHouseEntity convertBookHouseToEntity(BookHouseDTO dto) {
        BookHouseEntity entity = new BookHouseEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());

        // Map users (DTO to Entity)
        List<UserEntity> users = dto.getUsers().stream()
                .map(userDto -> {
                    UserEntity user = new UserEntity();
                    user.setId(userDto.getId());
                    user.setUsername(userDto.getUsername());
                    user.setUserType(userDto.getUserType());
                     // TODO: FETCH BorrowedBooks
                    user.setBookHouseEntityId(entity.getId());
                    return user;
                })
                .collect(Collectors.toList());
        entity.setUsers(users);

        // Map books similarly
        List<BookEntity> books = dto.getBooks().stream()
                .map(bookDto -> {
                    BookEntity book = new BookEntity();
                    book.setId(bookDto.getId());
                    book.setTitle(bookDto.getTitle());
                    book.setAuthor(bookDto.getAuthor());
                    book.setTotalStock(bookDto.getTotalStock());
                    book.setBookHouseEntity(entity);
                    return book;
                })
                .collect(Collectors.toList());
        entity.setBooks(books);

        return entity;
    }

    public static UserEntity convertUserToEntity(UserEntityDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setUserType(dto.getUserType());
        entity.setBookHouseEntityId(dto.getBookHouseId());
        // TODO: Fetch Borrowed Books

        return entity;
    }
}
