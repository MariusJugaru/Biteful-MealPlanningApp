package com.biteful.mealplanner.userservice.mappers.impl;

import com.biteful.mealplanner.userservice.domain.dto.UserCreateRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.domain.entities.UserRole;
import com.biteful.mealplanner.userservice.mappers.Mapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapper implements Mapper<UserEntity, UserCreateRequestDto> {

    @Override
    public UserCreateRequestDto mapTo(UserEntity userEntity) {
        return UserCreateRequestDto.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }

    @Override
    public UserEntity mapFrom(UserCreateRequestDto userCreateRequestDto) {
        return UserEntity.builder()
                .username(userCreateRequestDto.getUsername())
                .email(userCreateRequestDto.getEmail())
                .passwordHash(userCreateRequestDto.getPassword())
                .userRole(UserRole.USER)
                .build();
    }
}
