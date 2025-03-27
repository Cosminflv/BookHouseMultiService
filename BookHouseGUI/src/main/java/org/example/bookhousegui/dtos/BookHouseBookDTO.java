package org.example.bookhousegui.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookHouseBookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int totalStock;
    private long bookHouseEntityId;
}