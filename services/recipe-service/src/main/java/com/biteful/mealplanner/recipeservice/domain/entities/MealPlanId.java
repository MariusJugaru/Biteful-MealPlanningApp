package com.biteful.mealplanner.recipeservice.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class MealPlanId implements Serializable {

    private UUID userId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private MealType type;
}
