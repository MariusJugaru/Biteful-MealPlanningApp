package com.biteful.mealplanner.recipeservice.controllers;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeSummaryDto;
import com.biteful.mealplanner.recipeservice.mappers.MapperFacade;
import com.biteful.mealplanner.recipeservice.services.FileService;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {

    private final RecipeService recipeService;
    private final FileService fileService;
    private MapperFacade mapperFacade;

    public RecipesController(RecipeService recipeService,
                             MapperFacade mapperFacade,
                             FileService fileService) {
        this.recipeService = recipeService;
        this.mapperFacade = mapperFacade;
        this.fileService = fileService;
    }

    // Creates a new recipe and returns the id of the recipe.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> postRecipe(
            @RequestPart("recipe") RecipeCreateDto recipeCreateDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserPrincipal principal) {

        String url = fileService.save(image);

        Recipe recipe = mapperFacade.mapFromCreate(recipeCreateDto, url);
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

    // Returns all the recipes in the database, paginated.
    @GetMapping
    @PreAuthorize(("hasRole('ADMIN')"))
    public Page<RecipeSummaryDto> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Recipe> recipes = recipeService.getAllRecipes(pageable);

        return recipes.map(mapperFacade::mapToSummary);
    }


    // Returns all the recipes created by the authenticated user, paginated.
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

    // Returns a specific recipe created by the authenticated user.
    @GetMapping("/me/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public RecipeDto getMyRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId) {
        Recipe recipe = recipeService.getByIdAndUserId(recipeId, principal.getId());

        return mapperFacade.mapToDto(recipe);
    }


    // Returns a specific recipe.
    @GetMapping("/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public RecipeDto getByIdAdmin(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId) {
        Recipe recipe = recipeService.getRecipeWithAccess(recipeId, principal);

        return mapperFacade.mapToDto(recipe);
    }

    @PutMapping("/me/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updateMyRecipe(
            @RequestPart("recipe") RecipeDto recipeDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId) {

        Recipe recipe = mapperFacade.mapFromDto(recipeDto);
        Recipe saved = recipeService.updateRecipe(recipeId, recipe, image, principal);

        Map<String, String> response = Map.of("id", saved.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/me/{recipeId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteMyRecipe(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String recipeId
    ) {
        recipeService.deleteRecipe(recipeId, principal);
        return ResponseEntity.noContent().build();
    }

}
