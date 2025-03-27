package org.example.bookhousegui.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.bookhousegui.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
public class BookHouseUserDTO {
    private Long id;
    private String username;
    private EUserType userType;
    private List<Long> bookHouseSubscriptionIds;
    private List<Long> borrowedBooksIds;
}