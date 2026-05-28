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
public class MealPlanItem {

    private LocalDate date;

    private MealType type;

    private String recipeId;
}
