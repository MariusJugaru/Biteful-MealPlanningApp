package com.biteful.mealplanner.recipeservice.mappers;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeSummaryDto;
import com.biteful.mealplanner.recipeservice.mappers.impl.RecipeCreateMapper;
import com.biteful.mealplanner.recipeservice.mappers.impl.RecipeDtoMapper;
import com.biteful.mealplanner.recipeservice.mappers.impl.RecipeSummaryMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperFacade {
    private final RecipeCreateMapper recipeCreateMapper;
    private final RecipeDtoMapper recipeDtoMapper;
    private final RecipeSummaryMapper recipeSummaryMapper;

    public MapperFacade(RecipeCreateMapper recipeCreateMapper,
                        RecipeDtoMapper recipeDtoMapper,
                        RecipeSummaryMapper recipeSummaryMapper) {
        this.recipeCreateMapper = recipeCreateMapper;
        this.recipeDtoMapper = recipeDtoMapper;
        this.recipeSummaryMapper = recipeSummaryMapper;
    }

    public Recipe mapFromCreate(RecipeCreateDto recipeCreateDto, String imageUrl) {
        return recipeCreateMapper.mapFrom(recipeCreateDto, imageUrl);
    }

    public Recipe mapFromDto(RecipeDto recipeDto) {
        return recipeDtoMapper.mapFrom(recipeDto);
    }

    public RecipeDto mapToDto(Recipe recipe) {
        return recipeDtoMapper.mapTo(recipe);
    }

    public RecipeSummaryDto mapToSummary(Recipe recipe) {
        return recipeSummaryMapper.mapTo(recipe);
    }
}
