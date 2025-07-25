package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    private String identifier; // Can be either username or email

    private String username;

    @NotNull
    private String password;

}
