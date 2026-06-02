package com.biteful.mealplanner.listservice.services.impl;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.*;
import com.biteful.mealplanner.listservice.services.ListGenerationService;
import com.biteful.mealplanner.listservice.services.ListService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListGenerationServiceImpl implements ListGenerationService {

    private RestClient restClient;

    private ListService listService;

    public ListGenerationServiceImpl(@Value("${service.api.url}") String apiUrl, ListService listService) {
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .build();

        this.listService = listService;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class IngredientKey {
        private String name;
        private String unit;

    }

    @Override
    public ListResponse generate(UserPrincipal principal, String authHeader, LocalDate start, LocalDate end, String title) {

        List<RecipeResponse> recipeResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/plans/recipes")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .build())
                .header("Authorization", authHeader)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RecipeResponse>>() {});

        if (recipeResponse == null || recipeResponse.isEmpty()) {
            throw new EntityNotFoundException("No meals in plan");
        }

        Map<String, Integer> recipeCount = new HashMap<>();
        for (RecipeResponse recipe : recipeResponse) {
            recipeCount.put(recipe.getId(), recipeCount.getOrDefault(recipe.getId(), 0) + 1);
        }

        Map<String, RecipeResponse> recipeMap = new HashMap<>();
        for (RecipeResponse recipe : recipeResponse.stream().distinct().toList()) {
            recipeMap.put(recipe.getId(), recipe);
        }

        List<Ingredient> ingredients = new ArrayList<>();

        for (String recipeKey : recipeCount.keySet()) {
            RecipeResponse recipe = recipeMap.get(recipeKey);

            if (recipe == null) continue;

            int occurrences = recipeCount.get(recipeKey);

            int servings = recipe.getServings() == 0 ? 1 : recipe.getServings();

            double scaleFactor = (double) occurrences / servings;

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients.add(new Ingredient(
                        ingredient.getName(),
                        ingredient.getQuantity() * scaleFactor,
                        ingredient.getUnit()
                ));
            }
        }

        Map<IngredientKey, Double> aggregated = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            IngredientKey key = new IngredientKey(
                    ingredient.getName(),
                    ingredient.getUnit()
            );

            aggregated.put(
                    key,
                    aggregated.getOrDefault(key, 0.0) + ingredient.getQuantity()
            );
        }

        ListResponse listResponse = listService.createList(
                principal,
                ListRequest.builder()
                        .title(title != null && !title.isEmpty() ? title : "My New Generated List")
                        .build()
        );

        List<ListItemRequest> items = new ArrayList<>();
        for (IngredientKey key : aggregated.keySet()) {
            items.add(
                    ListItemRequest.builder()
                            .name(key.getName())
                            .unit(key.getUnit())
                            .quantity(aggregated.get(key))
                            .build()
            );
        }

        listService.addItems(
                principal,
                listResponse.getId(),
                items
        );

        return listResponse;
    }
}
