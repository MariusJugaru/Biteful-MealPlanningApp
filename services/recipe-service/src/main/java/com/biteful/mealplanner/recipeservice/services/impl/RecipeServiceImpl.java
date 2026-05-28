package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.NotAllowed;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.RecipeNotFound;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepository;
import com.biteful.mealplanner.recipeservice.services.FileService;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final FileService fileService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, FileService fileService) {
        this.recipeRepository = recipeRepository;
        this.fileService = fileService;
    }

    @Override
    public Recipe createRecipe(Recipe recipe, UUID userId, String role) {
        recipe.setUserId(userId);
        recipe.setUserType(role);

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe copyRecipe(String recipeId, UUID userId) {
        Recipe original = getRecipeById(recipeId);

        String imageCopy = fileService.copy(original.getImage());

        Recipe copy = Recipe.builder()
                .userId(userId)
                .userType(original.getUserType())
                .title(original.getTitle())
                .description(original.getDescription())
                .ingredients(original.getIngredients())
                .instructions(original.getInstructions())
                .tags(original.getTags())
                .prepTime(original.getPrepTime())
                .cookTime(original.getCookTime())
                .servings(original.getServings())
                .calories(original.getCalories())
                .image(imageCopy)
                .visibility("private")
                .build();

        return recipeRepository.save(copy);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return recipeRepository.findById(id)
                .orElseThrow(RecipeNotFound::new);
    }

    @Override
    public List<Recipe> getRecipesByIds(List<String> ids) {
        return recipeRepository.findByIdIn(ids);
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
    public boolean isEditableByUser(String recipeId, UserPrincipal userPrincipal) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(RecipeNotFound::new);

        // An admin can access any recipe
        boolean isAdmin = userPrincipal.getRole().equals("ADMIN");
        if (isAdmin)
            return true;

        // A user can edit a recipe if they own it
        return recipe.getUserId().equals(userPrincipal.getId());
    }

    @Override
    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    public Page<Recipe> getPublicRecipes(Pageable pageable) {
        return recipeRepository.findByVisibility("public", pageable);
    }

    @Override
    public Recipe updateRecipe(String id, Recipe updatedRecipe, MultipartFile image, UserPrincipal principal) {
        if (!isEditableByUser(id, principal))
            throw new NotAllowed();

        String url = fileService.save(image);
        updatedRecipe.setImage((url != null && !url.isEmpty()) ? url : updatedRecipe.getImage());

        Recipe oldRecipe = recipeRepository.findById(id)
                .orElseThrow(RecipeNotFound::new);
        updatedRecipe.setId(id);
        updatedRecipe.setCreatedAt(oldRecipe.getCreatedAt());

        if (!oldRecipe.getImage().equals(updatedRecipe.getImage())) {
            fileService.delete(oldRecipe.getImage());
        }

        return recipeRepository.save(updatedRecipe);
    }

    @Override
    public void deleteRecipe(String id, UserPrincipal principal) {
        if (!isEditableByUser(id, principal)) {
            throw new NotAllowed();
        }

        if (!recipeRepository.existsById(id)) {
            return;
        }

        try {
            Recipe recipe = recipeRepository.findById(id)
                    .orElseThrow(RecipeNotFound::new);

            fileService.delete(recipe.getImage());
        } catch (Exception e) {
            return;
        }

        recipeRepository.deleteById(id);
    }

}
