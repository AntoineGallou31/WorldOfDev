package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.request.UserUpdateDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private final UserService userService;

    // Endpoint to retrieve a user by their ID
    @GetMapping("/me")
    public UserResponseDto getUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        return this.userService.getUser(userDetails);
    }

    // Endpoint to update a user by their ID
    @PutMapping("/{id}")
    public MessageResponseDto updateById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "User data to update", required = true) @RequestBody UserUpdateDto userUpdateDto) {
        return this.userService.updateUserById(id, userUpdateDto);
    }
}
