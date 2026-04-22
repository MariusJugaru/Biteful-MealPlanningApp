package com.biteful.mealplanner.recipeservice.mappers;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;

public interface Mapper<A, B> {

    B mapTo(A a);

    A mapFrom(B b);

}
