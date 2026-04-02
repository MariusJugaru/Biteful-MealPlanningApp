package com.biteful.mealplanner.recipeservice;

import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.Ingredient;
import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeCreateDto;

import java.util.List;
import java.util.UUID;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static Recipe createPrivateRecipeA() {
        return Recipe.builder()
                .title("Pasta")
                .description("Pasta description")
                .ingredients(List.of(
                        Ingredient.builder()
                                .name("Pasta")
                                .quantity(500.0d)
                                .unit("g")
                                .build()))
                .instructions("Cook pasta")
                .tags(List.of("Lunch"))
                .prepTime(10)
                .cookTime(10)
                .servings(5)
                .image("default.png")
                .visibility("PRIVATE")
                .build();
    }

    public static Recipe createPublicRecipeB() {
        return Recipe.builder()
                .title("Lasagna")
                .description("Lasagna description")
                .ingredients(List.of(
                        Ingredient.builder()
                                .name("Lasagna sheets")
                                .quantity(500.0d)
                                .unit("g")
                                .build(),
                        Ingredient.builder()
                                .name("Minced meat")
                                .quantity(500.0d)
                                .unit("g")
                                .build()
                        )
                )
                .instructions("Cook lasagna sheets.\nCook meat.")
                .tags(List.of("Lunch", "Dinner"))
                .prepTime(10)
                .cookTime(10)
                .servings(5)
                .image("default.png")
                .visibility("PUBLIC")
                .build();
    }

    public static UserPrincipal createUserPrincipalA() {
        return UserPrincipal.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .role("USER")
                .username("User1")
                .build();
    }

    public static UserPrincipal createAdminPrincipalA() {
        return UserPrincipal.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111113"))
                .role("ADMIN")
                .username("Admin1")
                .build();
    }

    public static RecipeCreateDto getRecipeCreateDtoA() {
        RecipeCreateDto recipeCreateDto = RecipeCreateDto.builder()
                .title("Pasta")
                .description("Test desc")
                .ingredients(
                        List.of(
                                Ingredient.builder()
                                        .name("Pasta")
                                        .quantity(500.0d)
                                        .unit("g")
                                        .build()
                        )
                )
                .servings(5)
                .image("default.png")
                .visibility("PRIVATE")
                .build();
        return recipeCreateDto;
    }
}
