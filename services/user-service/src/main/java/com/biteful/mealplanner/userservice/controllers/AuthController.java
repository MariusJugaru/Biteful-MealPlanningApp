package com.biteful.mealplanner.userservice.controllers;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.LoginResponseDto;
import com.biteful.mealplanner.userservice.domain.dto.UserCreateRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.mappers.Mapper;
import com.biteful.mealplanner.userservice.services.AuthService;
import com.biteful.mealplanner.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final Mapper<UserEntity, UserCreateRequestDto> createMapper;
    private final UserService userService;
    private final AuthService authService;

    public AuthController(Mapper<UserEntity, UserCreateRequestDto> createMapper, UserService userService, AuthService authService) {
        this.createMapper = createMapper;
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(path = "/api/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        return authService.loginUser(loginRequest);
    }

    @PostMapping(path = "/api/register")
    public UserCreateRequestDto register(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        UserEntity userEntity = createMapper.mapFrom(userCreateRequestDto);
        UserEntity savedUserEntity = userService.createUser(userEntity);

        return createMapper.mapTo(savedUserEntity);
    }
}
