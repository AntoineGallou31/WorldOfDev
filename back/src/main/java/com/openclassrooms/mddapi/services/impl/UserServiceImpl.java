package com.openclassrooms.mddapi.services.impl;


import com.openclassrooms.mddapi.dto.request.UserUpdateDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their email.
     *
     * @param email the user's email, used as the username.
     * @return a UserDetails object containing user information and roles.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Search for the user in the database using the email
        User user = userRepository.findByEmail(email);

        // Return a Spring Security User object with username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities() // Ensure getAuthorities() returns a valid GrantedAuthority list
        );
    }

    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Retrieves a user by their ID and converts it to a UserResponseDto.
     *
     * @param id the user's identifier.
     * @return a UserResponseDto object containing user details.
     * @throws UsernameNotFoundException if no user is found with the provided ID.
     */
    public UserResponseDto getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return userToUserResponseDto(user);
        } else {
            // Throw an exception if the user is not found
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

    /**
     * Converts a User entity to a UserResponseDto.
     *
     * @param user the User entity to convert.
     * @return a UserResponseDto populated with user information.
     */
    private UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }

    // Updates a user's information by their ID
    public MessageResponseDto updateUserById (Long id, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userUpdateDto.getUsername());
            user.setEmail(userUpdateDto.getEmail());
            userRepository.save(user);
            return new MessageResponseDto().setMessage("Account update !");

        } else {
            throw new UsernameNotFoundException("User not found with id: " + id);
        }
    }

}