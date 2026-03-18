package com.biteful.mealplanner.userservice;

import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.domain.entities.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class TestDataUtil {

    private TestDataUtil() {}

    public static UserEntity createUserEnityA(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .username("userA")
                .email("emailA@test.com")
                .passwordHash(passwordEncoder.encode("123456"))
                .userRole(UserRole.USER)
                .build();
    }

    public static UserEntity createUserEnityB(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .username("userB")
                .email("emailB@test.com")
                .passwordHash(passwordEncoder.encode("789012"))
                .userRole(UserRole.USER)
                .build();
    }
}
