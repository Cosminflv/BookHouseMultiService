package org.example.bookhousegui.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.bookhousegui.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
public class UserEntityDTO {
    private Long id;
    private String username;
    private EUserType userType;
    private long bookHouseId;
    private List<Long> borrowedBooksIds;
}
