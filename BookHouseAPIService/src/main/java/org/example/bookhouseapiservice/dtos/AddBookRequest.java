package org.example.bookhouseapiservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhouseapiservice.models.BookEntity;

@Getter
@Setter
@AllArgsConstructor
public class AddBookRequest {
    private BookEntity book;
    private long bookHouseId = 0;
}
