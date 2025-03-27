package org.example.bookhousegui.models;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BorrowedBookEntity {
    private Long id;

    private UserEntity borrower;

    private String title;
    private String author;
    private String isbn;

    private LocalDateTime borrowedAt = LocalDateTime.now();
    private LocalDateTime returnDate;
    private boolean returned = false;
}
