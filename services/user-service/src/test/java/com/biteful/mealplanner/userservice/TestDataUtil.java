package com.biteful.mealplanner.userservice;

import com.biteful.mealplanner.userservice.domain.dto.UserCreateRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.domain.entities.UserRole;
public final class TestDataUtil {

    private TestDataUtil() {}

    public static UserEntity createUserEnityA() {
        return UserEntity.builder()
                .username("userA")
                .email("emailA@test.com")
                .passwordHash("123456")
                .userRole(UserRole.USER)
                .build();
    }

    public static UserEntity createUserEnityB() {
        return UserEntity.builder()
                .username("userB")
                .email("emailB@test.com")
                .passwordHash("789012")
                .userRole(UserRole.USER)
                .build();
    }

    public static UserCreateRequestDto createRequestDtoA() {
        return UserCreateRequestDto.builder()
                .username("userA")
                .email("emailA@test.com")
                .password("123456")
                .build();
    }

    public static UserCreateRequestDto createRequestDtoB() {
        return UserCreateRequestDto.builder()
                .username("userB")
                .email("emailB@test.com")
                .password("789012")
                .build();
    }
}
