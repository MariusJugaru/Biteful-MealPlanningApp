package com.biteful.mealplanner.userservice.config;

import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.domain.entities.UserRole;
import com.biteful.mealplanner.userservice.repositories.UserRepository;
import com.biteful.mealplanner.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("!test")
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserService userService, PasswordEncoder passwordEncoder,
                                       @Value("${app.admin.username}") String adminUsername,
                                       @Value("${app.admin.password}") String adminPassword,
                                       @Value("${app.admin.email}") String adminEmail) {
        return args -> {

            if (userService.getUserByUsername(adminUsername).isEmpty()) {
                UserEntity admin = UserEntity.builder()
                        .username(adminUsername)
                        .email(adminEmail)
                        .passwordHash(passwordEncoder.encode(adminPassword))
                        .userRole(UserRole.ADMIN)
                        .build();

                userService.createUser(admin);
            }
        };
    }
}
