package com.biteful.mealplanner.recipeservice.exceptions.runtime;

import java.util.function.Supplier;

public class RecipeNotFound extends RuntimeException {
    public RecipeNotFound() {
        super("Recipe not found");
    }
}
