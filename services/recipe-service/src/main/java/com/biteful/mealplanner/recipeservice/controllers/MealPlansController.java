package com.biteful.mealplanner.recipeservice.controllers;


import com.biteful.mealplanner.recipeservice.config.security.UserPrincipal;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealPlanItem;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.MealResponse;
import com.biteful.mealplanner.recipeservice.domain.dto.mealplans.UserPreferences;
import com.biteful.mealplanner.recipeservice.services.MealPlanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class MealPlansController {

    private final MealPlanService mealPlanService;

    public MealPlansController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<MealResponse> getMeals(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return mealPlanService.getMealsInRange(
                principal.getId(),
                startDate,
                endDate
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void addMealEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody MealPlanItem request
    ) {
        mealPlanService.addMeal(
                principal.getId(),
                request.getDate(),
                request.getType(),
                request.getRecipeId()
        );
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteMeal(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody MealPlanItem request
    ) {
        mealPlanService.removeMeal(
                principal.getId(),
                request.getDate(),
                request.getType()
        );
    }

    @GetMapping("/generate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<MealResponse> getPlan(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody UserPreferences preferences
    ) {

        return mealPlanService.generatePlan(principal.getId(), preferences);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void savePlanEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody List<MealPlanItem> meals
    ) {
        mealPlanService.savePlan(principal.getId(), meals);
    }
}
