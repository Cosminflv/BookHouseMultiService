package org.example.bookhousegui.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class BookHouseEntity {

    private Long id;

    private String name;
    private String location;

    private List<UserEntity> users;

    private List<BookEntity> books;
}
