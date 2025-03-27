package org.example.bookhouseapiservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookHouseUserDTO {
    private Long id;
    private String username;
    private List<Long> bookHouseSubscriptionIds;
    private List<Long> borrowedBooksIds;
}