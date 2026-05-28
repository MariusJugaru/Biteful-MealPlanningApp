package com.biteful.mealplanner.recipeservice.domain.dto.mealplans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {

    private Integer caloriesObjective;

    private Integer days;

    private LocalDate startDate;

    private Integer cookingTimesPerPlan;

    private List<String> restrictions;

    private boolean hasPublic;

    private boolean hasPrivate;

    private boolean hasSnack;
}
