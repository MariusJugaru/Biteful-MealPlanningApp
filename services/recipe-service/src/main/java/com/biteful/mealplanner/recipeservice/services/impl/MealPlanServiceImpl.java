package com.biteful.mealplanner.recipeservice.services.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.RecipeDto;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealPlanItem;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealResponse;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.UserPreferences;
import com.biteful.mealplanner.recipeservice.domain.entities.MealPlanEntity;
import com.biteful.mealplanner.recipeservice.domain.entities.MealPlanId;
import com.biteful.mealplanner.recipeservice.domain.entities.MealType;
import com.biteful.mealplanner.recipeservice.mappers.impl.RecipeDtoMapper;
import com.biteful.mealplanner.recipeservice.repositories.MealPlanRepository;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepositoryCustom;
import com.biteful.mealplanner.recipeservice.services.MealPlanService;
import com.biteful.mealplanner.recipeservice.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final RecipeService recipeService;
    private final RecipeRepositoryCustom recipeRepositoryCustom;

    public MealPlanServiceImpl(MealPlanRepository mealPlanRepository, RecipeService recipeService, RecipeRepositoryCustom recipeRepositoryCustom) {
        this.mealPlanRepository = mealPlanRepository;
        this.recipeService = recipeService;
        this.recipeRepositoryCustom = recipeRepositoryCustom;
    }

    @Override
    public List<MealResponse> getMealsInRange(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<MealPlanEntity> entities = mealPlanRepository.findByIdUserIdAndIdDateBetween(
                userId,
                startDate,
                endDate
        );

        // Get recipes IDs
        Set<String> recipeIdsSet = new HashSet<>();

        for (MealPlanEntity entity : entities) {
            recipeIdsSet.add(entity.getRecipeId());
        }
        List<String> recipeIds = new ArrayList<>(recipeIdsSet);

        // Map recipes to IDs
        List<Recipe> recipeEntities = recipeService.getRecipesByIds(recipeIds);
        Map<String, Recipe> recipes = new HashMap<>();

        for (Recipe entity : recipeEntities) {
            recipes.put(entity.getId(), entity);
        }

        // Create response
        List<MealResponse> response = new ArrayList<>();
        for (MealPlanEntity entity : entities) {
            Recipe recipe = recipes.get(entity.getRecipeId());

            if (recipe == null) {
                continue;
            }

            MealResponse meal = MealResponse.builder()
                    .recipeId(recipe.getId())
                    .title(recipe.getTitle())
                    .type(entity.getId().getType())
                    .date(entity.getId().getDate())
                    .image(recipe.getImage())
                    .calories(recipe.getCalories())
                    .build();

            response.add(meal);
        }

        return response;
    }

    @Override
    public MealResponse addMeal(UUID userId, LocalDate date, MealType type, String recipeId) {
        MealPlanEntity entity = MealPlanEntity.builder()
                .id(
                        MealPlanId.builder()
                                .userId(userId)
                                .date(date)
                                .type(type)
                                .build()
                )
                .recipeId(recipeId)
                .build();

        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            throw new RuntimeException("Recipe not found");
        }

        mealPlanRepository.save(entity);

        return MealResponse.builder()
                .recipeId(recipeId)
                .title(recipe.getTitle())
                .type(type)
                .date(date)
                .image(recipe.getImage())
                .calories(recipe.getCalories())
                .build();
    }

    @Override
    public void removeMeal(UUID userId, LocalDate date, MealType type) {
        mealPlanRepository.deleteById(
                MealPlanId.builder()
                        .userId(userId)
                        .date(date)
                        .type(type)
                        .build()
        );
    }

    // Returns the desired calories for a meal type.
    private static double getDesiredCalories(MealType type, UserPreferences preferences) {
        double desiderCalories = 0;
        switch (type) {
            case BREAKFAST, DINNER -> desiderCalories = preferences.isHasSnack() ?
                    preferences.getCaloriesObjective() * 0.25
                    :
                    preferences.getCaloriesObjective() * 0.3;
            case LUNCH -> desiderCalories = preferences.isHasSnack() ?
                    preferences.getCaloriesObjective() * 0.35
                    :
                    preferences.getCaloriesObjective() * 0.4;
            case SNACK -> desiderCalories = preferences.getCaloriesObjective() * 0.15;
        }
        return desiderCalories;
    }

    // Creates a MealResponse and adds it to a list.
    public void addMealToList(List<MealResponse> meals, Recipe recipe, MealType type, LocalDate date) {
        if (recipe == null) return;
        meals.add(MealResponse.builder()
                .recipeId(recipe.getId())
                .title(recipe.getTitle())
                .type(type)
                .date(date)
                .image(recipe.getImage())
                .calories(recipe.getCalories())
                .build());
    }

    // Picks a random recipe from a pool of recipes.
    Recipe pick(List<Recipe> pool, Set<Recipe> used) {
        for (int i = 0; i < 10; i++) {
            Recipe r = pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
            if (!used.contains(r)) return r;
        }
        return pool.get(0);
    }

    public void generatePlanForMealType(UUID userId, List<MealResponse> meals, MealType type, UserPreferences preferences) {

        // Get recipe candidates
        List<Recipe> publicRecipes = new ArrayList<>();
        List<Recipe> savedRecipes = new ArrayList<>();

        if (preferences.isHasPrivate()) {
            savedRecipes = recipeRepositoryCustom.getRecipeCandidates(type, preferences.getRestrictions(), false, userId);
        }
        if (preferences.isHasPublic()) {
            publicRecipes = recipeRepositoryCustom.getRecipeCandidates(type, preferences.getRestrictions(), true, userId);
        }

        List<Recipe> allRecipes = new ArrayList<>();
        allRecipes.addAll(publicRecipes);
        allRecipes.addAll(savedRecipes);

        // If there are no candidates for a meal type use any type.
        if (allRecipes.isEmpty()) {

            if (preferences.isHasPrivate()) {
                savedRecipes = recipeRepositoryCustom.getRecipeCandidates(null, preferences.getRestrictions(), false, userId);
            }
            if (preferences.isHasPublic()) {
                publicRecipes = recipeRepositoryCustom.getRecipeCandidates(null, preferences.getRestrictions(), true, userId);
            }

            allRecipes.addAll(publicRecipes);
            allRecipes.addAll(savedRecipes);
        }
        if (allRecipes.isEmpty()) return;

        double desiredCalories = getDesiredCalories(type, preferences);
        if (desiredCalories != 0) {
            allRecipes = allRecipes.stream()
                    .filter(r -> r.getCalories() != null)
                    .sorted(Comparator.comparingDouble(r ->
                            Math.abs(r.getCalories() - desiredCalories)
                    ))
                    .toList();
        } else {
            allRecipes = allRecipes.stream()
                    .sorted(Comparator.comparingDouble(r ->
                            Math.abs(r.getCalories() - desiredCalories)
                    ))
                    .toList();
        }

        Set<Recipe> used = new HashSet<>();
        int span = 1;
        if (preferences.getCookingTimesPerPlan() != 5) {
            span = preferences.getDays() / preferences.getCookingTimesPerPlan();
        }
        for (int i = 0; i < preferences.getCookingTimesPerPlan(); i++) {

            // Get a recipe that will be used for floor(preferences.getDays() / preferences.getCookingTimesPerPlan()) days.
            Recipe recipe;
            int attempts = 5;
            if (desiredCalories == 0) {
                recipe = pick(allRecipes, used);
            } else {
                List<Recipe> top = allRecipes.stream().limit(5).toList();
                recipe = pick(top, used);
            }
            used.add(recipe);

            // Add the recipe to the plan
            for (int j = i + i * span; j <= Math.min(i + (i + 1) * span, preferences.getDays() - 1); j++) {
                LocalDate date = preferences.getStartDate().plusDays(j);
                addMealToList(meals, recipe, type, date);
            }
        }
    }

    @Override
    public List<MealResponse> generatePlan(UUID userId, UserPreferences preferences) {
        if (preferences.getDays() < preferences.getCookingTimesPerPlan()) {
            throw new RuntimeException("You can't cook more times than days!");
        }

        List<MealResponse> response = new ArrayList<>();

        generatePlanForMealType(userId, response, MealType.BREAKFAST, preferences);
        generatePlanForMealType(userId, response, MealType.LUNCH, preferences);
        generatePlanForMealType(userId, response, MealType.DINNER, preferences);
        if (preferences.isHasSnack())
            generatePlanForMealType(userId, response, MealType.SNACK, preferences);

        return response;
    }

    @Override
    public void savePlan(UUID userId, List<MealPlanItem> meals) {

        for (MealPlanItem item : meals) {
            MealPlanEntity entity = MealPlanEntity.builder()
                    .id(MealPlanId.builder()
                            .type(item.getType())
                            .date(item.getDate())
                            .userId(userId)
                            .build())
                    .recipeId(item.getRecipeId())
                    .build();

            mealPlanRepository.save(entity);
        }
    }

    @Override
    public List<RecipeDto> getRecipesInRange(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<MealResponse> meals = getMealsInRange(userId, startDate, endDate);

        List<String> recipeIds = new ArrayList<>();

        for (MealResponse meal : meals) {
            recipeIds.add(meal.getRecipeId());
        }

        List<Recipe> recipes = recipeService.getRecipesByIds(recipeIds.stream().distinct().toList());

        Map<String, Recipe> recipeMap = new HashMap<>();
        for (Recipe recipe : recipes) {
            recipeMap.put(recipe.getId(), recipe);
        }

        List<RecipeDto> recipesResponse = new ArrayList<>();
        RecipeDtoMapper mapper = new RecipeDtoMapper();
        for (String recipeId : recipeIds) {
            recipesResponse.add(
                    mapper.mapTo(recipeMap.get(recipeId))
            );
        }

        return recipesResponse;
    }
}
