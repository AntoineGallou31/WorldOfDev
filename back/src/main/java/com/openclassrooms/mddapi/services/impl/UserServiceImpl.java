package com.openclassrooms.mddapi.services.impl;


import com.openclassrooms.mddapi.dto.request.UserUpdateDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.exception.ValidationException;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Data
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // This method loads a user by their email address.
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email ne peut pas être vide");
        }

        // Search for the user in the database using the email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }

        // Return a Spring Security User object with username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

    // This method loads a user by their username.
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Le nom d'utilisateur ne peut pas être vide");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
    }

    // This method retrieves the user details based on the UserDetails object.
    public UserResponseDto getUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new ValidationException("Les détails utilisateur ne peuvent pas être null");
        }

        String email = userDetails.getUsername();
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email manquant dans les détails utilisateur");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email);
        }

        return userToUserResponseDto(user);
    }

    // This method updates a user by their ID.
    public MessageResponseDto updateUserById (Long id, UserUpdateDto userUpdateDto) {
        if (id == null) {
            throw new ValidationException("L'ID utilisateur ne peut pas être null");
        }

        if (userUpdateDto == null) {
            throw new ValidationException("Les données de mise à jour ne peuvent pas être null");
        }

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
        }

        User user = userOptional.get();
        user.setUsername(userUpdateDto.getUsername());
        user.setEmail(userUpdateDto.getEmail());

        // Ne mettre à jour le mot de passe que s'il est fourni
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        userRepository.save(user);
        return new MessageResponseDto().setMessage("Compte mis à jour avec succès !");
    }

    // This method converts a User entity to a UserResponseDto.
    private UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getRealUsername());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
        }
}