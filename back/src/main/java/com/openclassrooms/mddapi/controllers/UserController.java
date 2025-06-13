package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.request.UserUpdateDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import com.openclassrooms.mddapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    @Autowired
    private final UserService userService;

    // Endpoint to retrieve a user by their ID
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDto getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    // Endpoint to update a user by their ID
    @PutMapping("/{id}")
    @Operation (summary = "Update user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public MessageResponseDto updateById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id,
            @Parameter(description = "User data to update", required = true) @RequestBody UserUpdateDto userUpdateDto) {
        return this.userService.updateUserById(id, userUpdateDto);
    }
}
