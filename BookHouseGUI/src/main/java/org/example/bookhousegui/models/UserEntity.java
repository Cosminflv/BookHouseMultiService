package org.example.bookhousegui.models;

import lombok.Getter;
import org.example.bookhousegui.models.enums.EUserType;

import java.util.List;

@Getter
public class UserEntity {
    private Long id;

    private String username;

    private EUserType userType;

    private BookHouseEntity bookHouseEntity;

    private List<BorrowedBookEntity> borrowedBooks;
}
