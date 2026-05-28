package com.biteful.mealplanner.recipeservice.services;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeService {

    public Recipe createRecipe(Recipe recipe, UUID userId, String role);

    public Recipe copyRecipe(String recipeId, UUID userId);

    public Recipe getRecipeById(String id);

    public List<Recipe> getRecipesByIds(List<String> ids);

    public Page<Recipe> getRecipesByUser(UUID userId, Pageable pageable);

    Recipe getByIdAndUserId(String recipeId, UUID userId);

    Recipe getRecipeWithAccess(String recipeId, UserPrincipal userPrincipal);

    boolean isEditableByUser(String recipeId, UserPrincipal userPrincipal);

    public Page<Recipe> getAllRecipes(Pageable pageable);

    public Page<Recipe> getPublicRecipes(Pageable pageable);

    public Recipe updateRecipe(String id, Recipe updatedRecipe, MultipartFile image, UserPrincipal principal);

    public void deleteRecipe(String id, UserPrincipal principal);

}
