package com.biteful.mealplanner.recipeservice.domain.dto;

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
    private List<String> ingredients;
    private String instructions;
    private List<String> tags;
    private int prepTime;
    private int cookTime;
    private String image;
    private String visibility;

}
