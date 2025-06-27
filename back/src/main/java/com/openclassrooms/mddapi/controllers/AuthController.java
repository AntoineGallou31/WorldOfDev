package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.request.LoginDto;
import com.openclassrooms.mddapi.dto.request.RegisterUserDto;
import com.openclassrooms.mddapi.dto.response.JwtResponseDto;
import com.openclassrooms.mddapi.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to user authentication and registration")
public class AuthController {

    private final AuthService authService;

    // Endpoint to register a new user
    @PostMapping("/register")
    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public JwtResponseDto register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return this.authService.register(registerUserDto);
    }

    // Endpoint to authenticate a user and return a JWT token
    @PostMapping("/login")
    @Operation(summary = "Login user", responses = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    })
    public JwtResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        return this.authService.authenticate(loginDto);
    }
}
