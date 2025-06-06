package org.example.bookhousegui.models;

import lombok.Getter;
import lombok.Setter;
import org.example.bookhousegui.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
public class UserEntity {
    private Long id;

    private String username;

    private EUserType userType;

    private Long bookHouseEntityId;

    private List<BorrowedBookEntity> borrowedBooks;
}
