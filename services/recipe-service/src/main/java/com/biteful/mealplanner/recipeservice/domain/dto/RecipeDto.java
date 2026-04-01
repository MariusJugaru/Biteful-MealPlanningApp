package com.biteful.mealplanner.recipeservice.domain.dto;

import com.biteful.mealplanner.recipeservice.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeDto {

    private String id;
    private UUID userId;

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

    private Instant createdAt;
    private Instant updatedAt;
}
