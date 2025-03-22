package org.example.bookhouseauthservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookhouseauthservice.models.EUserType;

@Getter
@Setter
@AllArgsConstructor
public class AddUserRequest {
    private String username;
    private String password;
    private EUserType userType;
}
