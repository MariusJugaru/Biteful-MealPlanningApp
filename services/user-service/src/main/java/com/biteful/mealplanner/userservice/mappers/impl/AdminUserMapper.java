package com.biteful.mealplanner.userservice.mappers.impl;

import com.biteful.mealplanner.userservice.domain.dto.AdminUsersDto;
import com.biteful.mealplanner.userservice.domain.dto.UserDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.mappers.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AdminUserMapper implements Mapper<UserEntity, AdminUsersDto> {
    @Override
    public AdminUsersDto mapTo(UserEntity userEntity) {
        return AdminUsersDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .userRole(userEntity.getUserRole())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    @Override
    public UserEntity mapFrom(AdminUsersDto AdminUsersDto) {
        throw new UnsupportedOperationException("Mapping from UserDto not supported");
    }
}
