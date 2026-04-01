package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.NotAllowed;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.RecipeNotFound;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepository;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe createRecipe(Recipe recipe, UUID userId, String role) {
        recipe.setUserId(userId);
        recipe.setUserType(role);
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(RecipeNotFound::new);
    }

    @Override
    public Page<Recipe> getRecipesByUser(UUID userId, Pageable pageable) {
        return recipeRepository.findByUserId(userId, pageable);
    }

    @Override
    public Recipe getByIdAndUserId(String recipeId, UUID userId) {
        return recipeRepository.findByIdAndUserId(recipeId, userId)
                .orElseThrow(RecipeNotFound::new);
    }

    @Override
    public Recipe getRecipeWithAccess(String recipeId, UserPrincipal userPrincipal) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(RecipeNotFound::new);

        // An admin can access any recipe
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        if (isAdmin)
            return recipe;

        // A user can see a recipe only if the recipe is public, or they own it.
        boolean isPublic = recipe.getVisibility().equalsIgnoreCase("PUBLIC");
        boolean isOwner = recipe.getUserId().equals(userPrincipal.getId());
        if (isPublic || isOwner)
            return recipe;

        throw new NotAllowed();
    }

    @Override
    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    public Recipe updateRecipe(String id, Recipe updatedRecipe) {
        return null;
    }

    @Override
    public void deleteRecipe(String id) {

    }
}
