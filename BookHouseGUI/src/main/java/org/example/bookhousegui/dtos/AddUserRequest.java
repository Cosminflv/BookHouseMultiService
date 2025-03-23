package org.example.bookhousegui.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhousegui.models.enums.EUserType;

@Getter
@Setter
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private EUserType userType;
}
