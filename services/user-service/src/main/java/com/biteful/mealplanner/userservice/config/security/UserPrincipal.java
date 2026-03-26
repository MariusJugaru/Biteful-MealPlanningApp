package com.biteful.mealplanner.userservice.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private final UUID id;

    private final String username;
}
