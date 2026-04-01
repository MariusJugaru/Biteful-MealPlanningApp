package com.biteful.mealplanner.recipeservice.exceptions.runtime;

public class NotAllowed extends RuntimeException {
    public NotAllowed() {
        super("You are not allowed to access this recipe.");
    }
}
