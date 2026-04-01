package com.biteful.mealplanner.recipeservice.controllers;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeSummaryDto;
import com.biteful.mealplanner.recipeservice.mappers.MapperFacade;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {

    private final RecipeService recipeService;
    private MapperFacade mapperFacade;

    public RecipesController(RecipeService recipeService,
                             MapperFacade mapperFacade) {
        this.recipeService = recipeService;
        this.mapperFacade = mapperFacade;
    }

    // Creates a new recipe and returns the id of the recipe.
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> postRecipe(@RequestBody RecipeCreateDto recipeCreateDto,
                                                          @AuthenticationPrincipal UserPrincipal principal) {
        Recipe recipe = mapperFacade.mapFromCreate(recipeCreateDto);
        Recipe saved = recipeService.createRecipe(
                recipe,
                principal.getId(),
                principal.getRole()
        );

        Map<String, String> response = Map.of("id", saved.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<RecipeSummaryDto> getMyRecipes(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Recipe> recipes = recipeService.getRecipesByUser(principal.getId(), pageable);

        return recipes.map(mapperFacade::mapToSummary);
    }

    @GetMapping("/me/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public RecipeDto getMyRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId) {
        Recipe recipe = recipeService.getByIdAndUserId(recipeId, principal.getId());

        return mapperFacade.mapToDto(recipe);
    }


    @GetMapping("/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public RecipeDto getByIdAdmin(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId) {
        Recipe recipe = recipeService.getRecipeWithAccess(recipeId, principal);

        return mapperFacade.mapToDto(recipe);
    }

}
