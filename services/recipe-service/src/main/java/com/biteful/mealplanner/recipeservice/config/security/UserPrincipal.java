package com.biteful.mealplanner.recipeservice.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private final UUID id;

    private final String username;

    private final String role;
}
