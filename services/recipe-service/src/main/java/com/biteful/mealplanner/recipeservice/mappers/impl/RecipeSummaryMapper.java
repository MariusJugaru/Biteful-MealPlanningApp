package com.biteful.mealplanner.recipeservice.mappers.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeSummaryDto;
import com.biteful.mealplanner.recipeservice.mappers.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeSummaryMapper implements Mapper<Recipe, RecipeSummaryDto> {
    @Override
    public RecipeSummaryDto mapTo(Recipe recipe) {
        return RecipeSummaryDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .image(recipe.getImage())
                .build();
    }

    @Override
    public Recipe mapFrom(RecipeSummaryDto recipeSummaryDto) {
        throw new UnsupportedOperationException("Mapping from recipeSummaryDto not supported");
    }
}
