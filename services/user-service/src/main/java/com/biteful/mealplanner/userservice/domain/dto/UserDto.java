package com.biteful.mealplanner.userservice.domain.dto;

import com.biteful.mealplanner.userservice.domain.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;

    private String username;

    private String email;

    private UserRole userRole;

    private Instant createdAt;
}
