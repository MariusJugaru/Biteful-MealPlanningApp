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
public class UpdatePasswordRequestDto {
    @NotNull(message = "Old password is required")
    private String oldPassword;

    @NotNull(message = "New password is required")
    private String newPassword;
}
