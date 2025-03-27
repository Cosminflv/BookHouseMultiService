package org.example.bookhouseapiservice.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookhouseapiservice.models.enums.EUserType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserEntityDTO {
    private Long id;
    private String username;
    private EUserType userType;
    private long bookHouseId;
    private List<Long> borrowedBooksIds;
}
