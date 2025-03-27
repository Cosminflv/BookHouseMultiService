package org.example.bookhouseapiservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class BookHouseDTO {
    private Long id;
    private String name;
    private String location;
    private List<BookHouseUserDTO> users;
    private List<BookHouseBookDTO> books;
}