package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String password;
}
