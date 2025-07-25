package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.request.LoginDto;
import com.openclassrooms.mddapi.dto.request.RegisterUserDto;
import com.openclassrooms.mddapi.dto.response.JwtResponseDto;

public interface AuthService {

    JwtResponseDto register(RegisterUserDto userDto);

    JwtResponseDto authenticate(LoginDto loginDto);

}
