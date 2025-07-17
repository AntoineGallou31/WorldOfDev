package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.request.LoginDto;
import com.openclassrooms.mddapi.dto.request.RegisterUserDto;
import com.openclassrooms.mddapi.dto.response.JwtResponseDto;
import com.openclassrooms.mddapi.services.AuthService;
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
    public JwtResponseDto register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return this.authService.register(registerUserDto);
    }

    // Endpoint to authenticate a user and return a JWT token
    @PostMapping("/login")
    public JwtResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        return this.authService.authenticate(loginDto);
    }
}
