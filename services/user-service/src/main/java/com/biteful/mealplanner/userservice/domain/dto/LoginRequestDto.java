package com.biteful.mealplanner.userservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    @NotNull(message = "Username or email is required")
    private String usernameOrEmail;

    @NotNull(message = "Password is required")
    private String password;
}
