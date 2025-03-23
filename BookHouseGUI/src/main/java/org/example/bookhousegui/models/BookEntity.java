package org.example.bookhousegui.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class BookEntity {
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private int totalStock;

    private BookHouseEntity bookHouseEntity;

    private List<BorrowedBookEntity> borrowedBooks;
}
