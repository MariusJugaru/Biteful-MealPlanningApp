package com.biteful.mealplanner.recipeservice.mappers.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.mappers.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeCreateMapper implements Mapper<Recipe, RecipeCreateDto> {
    @Override
    public RecipeCreateDto mapTo(Recipe recipe) {
        throw new UnsupportedOperationException("Mapping to recipeCreateDto not supported");
    }

    @Override
    public Recipe mapFrom(RecipeCreateDto recipeCreateDto) {
        return Recipe.builder()
                .title(recipeCreateDto.getTitle() != null ? recipeCreateDto.getTitle() : "My new recipe")
                .description(recipeCreateDto.getDescription())
                .ingredients(recipeCreateDto.getIngredients())
                .instructions(recipeCreateDto.getInstructions())
                .tags(recipeCreateDto.getTags())
                .prepTime(recipeCreateDto.getPrepTime())
                .cookTime(recipeCreateDto.getCookTime())
                .servings(recipeCreateDto.getServings() != null ? recipeCreateDto.getServings() : 1)
                .calories(recipeCreateDto.getCalories() != null ? recipeCreateDto.getCalories() : 0)
                .visibility(recipeCreateDto.getVisibility() != null ? recipeCreateDto.getVisibility() : "PRIVATE")
                .build();
    }

    public Recipe mapFrom(RecipeCreateDto recipeCreateDto, String imageUrl) {
        return Recipe.builder()
                .title(recipeCreateDto.getTitle() != null ? recipeCreateDto.getTitle() : "My new recipe")
                .description(recipeCreateDto.getDescription())
                .ingredients(recipeCreateDto.getIngredients())
                .instructions(recipeCreateDto.getInstructions())
                .tags(recipeCreateDto.getTags())
                .prepTime(recipeCreateDto.getPrepTime())
                .cookTime(recipeCreateDto.getCookTime())
                .servings(recipeCreateDto.getServings() != null ? recipeCreateDto.getServings() : 1)
                .calories(recipeCreateDto.getCalories() != null ? recipeCreateDto.getCalories() : 0)
                .image(imageUrl)
                .visibility(recipeCreateDto.getVisibility() != null ? recipeCreateDto.getVisibility() : "PRIVATE")
                .build();

    }
}
