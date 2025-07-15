package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.request.UserUpdateDto;
import com.openclassrooms.mddapi.dto.response.MessageResponseDto;
import com.openclassrooms.mddapi.dto.response.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails loadUserByEmail(String email);

    UserResponseDto getUser( UserDetails userDetails);

    MessageResponseDto updateUserById(Long id, UserUpdateDto userUpdateDto);

}
