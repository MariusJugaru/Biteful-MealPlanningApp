package com.biteful.mealplanner.userservice.services;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
}
