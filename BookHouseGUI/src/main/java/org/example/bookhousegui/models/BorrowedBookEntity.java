package org.example.bookhousegui.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowedBookEntity {
    private Long id;

    private String title;
    private String author;
    private String isbn;

    private LocalDateTime borrowedAt;
    private LocalDateTime returnDate;

    private boolean returned;
}
