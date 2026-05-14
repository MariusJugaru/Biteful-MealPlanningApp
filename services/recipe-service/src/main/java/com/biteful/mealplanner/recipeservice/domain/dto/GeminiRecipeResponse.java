package com.biteful.mealplanner.recipeservice.domain.dto;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import lombok.Data;

@Data
public class GeminiRecipeResponse {

    private boolean ready;

    private Recipe recipe;
}
