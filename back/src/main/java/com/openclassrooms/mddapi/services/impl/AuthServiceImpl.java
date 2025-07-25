package com.openclassrooms.mddapi.services.impl;

import com.openclassrooms.mddapi.dto.request.LoginDto;
import com.openclassrooms.mddapi.dto.request.RegisterUserDto;
import com.openclassrooms.mddapi.dto.response.JwtResponseDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.EmailAlreadyUsedException;
import com.openclassrooms.mddapi.exception.InvalidPasswordException;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.JwtProvider;
import com.openclassrooms.mddapi.services.AuthService;
import lombok.RequiredArgsConstructor;
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

        if (userRepository.findByEmail(registerUserDto.getEmail()) != null) {
            throw new EmailAlreadyUsedException("L'email est déjà utilisé.");
        }

        userRepository.findByUsername(registerUserDto.getUsername())
                .ifPresent(user -> { throw new EmailAlreadyUsedException("Le nom d'utilisateur est déjà utilisé."); });

        var user = User.builder()
                .username(registerUserDto.getUsername())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();

        userRepository.save(user);

        String jwtToken = jwtProvider.generateToken(user, "email");
        return new JwtResponseDto(jwtToken);
    }

    // Authenticates an existing user and returns a JWT token response
    public JwtResponseDto authenticate(LoginDto loginDto) {
        String identifier = loginDto.getIdentifier();
        if (identifier == null || identifier.isBlank()) {
            throw new EmailAlreadyUsedException("L'identifiant ne peut pas être vide");
        }
        User user;
        String identifierType;

        // Auth via email
        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier);
            identifierType = "email";

            if (user == null) {
                throw new EmailAlreadyUsedException("Email incorrect ou non trouvé.");
            }
        }
        // Auth via username
        else {
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new EmailAlreadyUsedException("Nom d'utilisateur incorrect ou non trouvé."));
            identifierType = "username";
        }

        // Check password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Mot de passe incorrect.");
        }

        String jwtToken = jwtProvider.generateToken(user, identifierType);
        return new JwtResponseDto(jwtToken);
    }
}
