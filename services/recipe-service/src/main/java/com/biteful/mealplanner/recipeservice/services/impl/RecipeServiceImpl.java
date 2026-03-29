package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.RecipeNotFound;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepository;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(RecipeNotFound::new);
    }

    @Override
    public List<Recipe> getRecipesByUser(UUID userId) {
        return recipeRepository.findByUserId(userId);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe updateRecipe(String id, Recipe updatedRecipe) {
        return null;
    }

    @Override
    public void deleteRecipe(String id) {

    }
}
