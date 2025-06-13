package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.request.LoginDto;
import com.openclassrooms.mddapi.dto.request.RegisterUserDto;
import com.openclassrooms.mddapi.dto.response.JwtResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtProvider;
import com.openclassrooms.mddapi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Registers a new user and returns a JWT token response
    public JwtResponseDto register(RegisterUserDto registerUserDto) {
        // Build a new User object using the builder pattern
        var user = User.builder()
                .username(registerUserDto.getUsername()) // Sets the user's name
                .email(registerUserDto.getEmail()) // Sets the user's email
                .password(passwordEncoder.encode(registerUserDto.getPassword())) // Hashes the password
                .build();

        userRepository.save(user); // Saves the user to the database

        // Generates a JWT token for the new user
        String jwtToken = jwtProvider.generateToken(user, "email");
        return new JwtResponseDto(jwtToken);
    }

    // Authenticates an existing user and returns a JWT token response
    public JwtResponseDto authenticate(LoginDto loginDto) {
        String identifier = loginDto.getIdentifier();
        if (identifier == null || identifier.isBlank()) {
            throw new BadCredentialsException("Identifiant manquant");
        }
        User user;
        String identifierType;

        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier);
            identifierType = "email";
        } else {
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));
            identifierType = "username";
        }

        if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Identifiants invalides");
        }

        String jwtToken = jwtProvider.generateToken(user, identifierType);
        return new JwtResponseDto(jwtToken);
    }
}
