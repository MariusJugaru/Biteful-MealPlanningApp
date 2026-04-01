package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RecipeService {

    public Recipe createRecipe(Recipe recipe, UUID userId, String role);

    public Recipe getRecipeById(String id);

    public Page<Recipe> getRecipesByUser(UUID userId, Pageable pageable);

    Recipe getByIdAndUserId(String recipeId, UUID userId);

    Recipe getRecipeWithAccess(String recipeId, UserPrincipal userPrincipal);

    public Page<Recipe> getAllRecipes(Pageable pageable);

    public Recipe updateRecipe(String id, Recipe updatedRecipe);

    public void deleteRecipe(String id);
}
