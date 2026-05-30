package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealPlanItem;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealResponse;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.UserPreferences;
import com.biteful.mealplanner.recipeservice.domain.entities.MealType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MealPlanService {

    public List<MealResponse> getMealsInRange(UUID userId, LocalDate startDate, LocalDate endDate);

    public MealResponse addMeal(UUID userId, LocalDate date, MealType type, String recipeId);

    public void removeMeal(UUID userId, LocalDate date, MealType type);

    public List<MealResponse> generatePlan(UUID userId, UserPreferences preferences);

    public void savePlan(UUID userId, List<MealPlanItem> meals);
}
