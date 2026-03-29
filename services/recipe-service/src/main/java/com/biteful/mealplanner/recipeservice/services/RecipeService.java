package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    public Recipe createRecipe(Recipe recipe);

    public Recipe getRecipeById(String id);

    public List<Recipe> getRecipesByUser(UUID userId);

    public List<Recipe> getAllRecipes();

    public Recipe updateRecipe(String id, Recipe updatedRecipe);

    public void deleteRecipe(String id);
}
