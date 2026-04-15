package com.biteful.mealplanner.recipeservice.domain.dto;

import com.biteful.mealplanner.recipeservice.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeCreateDto {

    private String title;
    private String description;
    private List<Ingredient> ingredients;
    private String instructions;
    private List<String> tags;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private Integer calories;
    private String image;
    private String visibility;

}