package com.biteful.mealplanner.userservice.services;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.LoginResponseDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;

public interface AuthService {
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);

    LoginResponseDto generateTokenForUser(UserEntity userEntity);
}
