package com.biteful.mealplanner.userservice.domain.dto;

import com.biteful.mealplanner.userservice.domain.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {

    private UserRole role;
}
