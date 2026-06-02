package com.biteful.mealplanner.listservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeResponse {

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
