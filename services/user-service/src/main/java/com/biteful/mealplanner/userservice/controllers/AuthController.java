package com.biteful.mealplanner.userservice.controllers;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.UserCreateRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.mappers.Mapper;
import com.biteful.mealplanner.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private Mapper<UserEntity, UserCreateRequestDto> createMapper;
    private UserService userService;

    public AuthController(Mapper<UserEntity, UserCreateRequestDto> createMapper, UserService userService) {
        this.createMapper = createMapper;
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public LoginRequestDto login(@RequestBody @Valid LoginRequestDto loginRequest) {
        return loginRequest;
    }

    @PostMapping(path = "/register")
    public UserCreateRequestDto register(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        UserEntity userEntity = createMapper.mapFrom(userCreateRequestDto);
        UserEntity savedUserEntity = userService.createUser(userEntity);

        return createMapper.mapTo(savedUserEntity);
    }
}
