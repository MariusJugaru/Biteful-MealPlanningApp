package com.biteful.mealplanner.recipeservice.mappers.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.mappers.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeDtoMapper implements Mapper<Recipe, RecipeDto> {
    @Override
    public RecipeDto mapTo(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .userId(recipe.getUserId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .tags(recipe.getTags())
                .prepTime(recipe.getPrepTime())
                .cookTime(recipe.getCookTime())
                .servings(recipe.getServings())
                .calories(recipe.getCalories())
                .image(recipe.getImage())
                .visibility(recipe.getVisibility())
                .createdAt(recipe.getCreatedAt())
                .updatedAt(recipe.getUpdatedAt())
                .build();
    }

    @Override
    public Recipe mapFrom(RecipeDto recipeDto) {
        return Recipe.builder()
                .id(recipeDto.getId())
                .userId(recipeDto.getUserId())
                .title(recipeDto.getTitle())
                .description(recipeDto.getDescription())
                .ingredients(recipeDto.getIngredients())
                .instructions(recipeDto.getInstructions())
                .tags(recipeDto.getTags())
                .prepTime(recipeDto.getPrepTime())
                .cookTime(recipeDto.getCookTime())
                .servings(recipeDto.getServings())
                .calories(recipeDto.getCalories())
                .image(recipeDto.getImage())
                .visibility(recipeDto.getVisibility())
                .build();
    }

}
