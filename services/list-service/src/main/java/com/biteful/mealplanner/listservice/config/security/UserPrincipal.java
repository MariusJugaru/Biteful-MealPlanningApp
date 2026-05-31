package com.biteful.mealplanner.listservice.config.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UserPrincipal {
    private final UUID id;

    private final String username;

    private final String role;
}
