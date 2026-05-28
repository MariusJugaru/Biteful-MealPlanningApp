package com.biteful.mealplanner.recipeservice.domain.dto.mealplans;

import com.biteful.mealplanner.recipeservice.domain.entities.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MealResponse {
    private String recipeId;

    private String title;

    private MealType type;

    private LocalDate date;

    private String image;

    private Integer calories;
}
