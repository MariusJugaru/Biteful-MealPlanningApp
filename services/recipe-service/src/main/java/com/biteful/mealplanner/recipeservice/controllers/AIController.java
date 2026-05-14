package com.biteful.mealplanner.recipeservice.controllers;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.dto.AiRequest;
import com.biteful.mealplanner.recipeservice.domain.dto.GeminiRecipeResponse;
import com.biteful.mealplanner.recipeservice.services.GeminiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate")
    public GeminiRecipeResponse generate(@RequestBody AiRequest request) throws Exception {
        return geminiService.generateRecipe(request.getPrompt());
    }
}
