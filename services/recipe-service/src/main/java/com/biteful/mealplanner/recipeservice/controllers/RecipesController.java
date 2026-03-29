package com.biteful.mealplanner.recipeservice.controllers;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {

    private final RecipeService recipeService;

    public RecipesController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public Recipe postRecipe(@RequestBody Recipe recipe) {
        return recipeService.createRecipe(recipe);
    }

    @GetMapping("/{id}")
    public Recipe getById(@PathVariable String id) {
        return recipeService.getRecipeById(id);
    }
}
