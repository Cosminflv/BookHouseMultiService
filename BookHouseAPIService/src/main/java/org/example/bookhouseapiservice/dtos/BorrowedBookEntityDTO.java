package org.example.bookhouseapiservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BorrowedBookEntityDTO {
    private Long id;

    private String title;
    private String author;
    private String isbn;

    private LocalDateTime borrowedAt;
    private LocalDateTime returnDate;
}
