package org.example.bookhouseapiservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhouseapiservice.models.enums.EUserType;

@Getter
@Setter
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private EUserType userType;
}
