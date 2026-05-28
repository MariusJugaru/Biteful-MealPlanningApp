package com.biteful.mealplanner.recipeservice.repositories;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.entities.MealType;

import java.util.List;
import java.util.UUID;

public interface RecipeRepositoryCustom {

    List<Recipe> getRecipeCandidates(MealType type, List<String> allergens, boolean excludeUser, UUID userId);
}
