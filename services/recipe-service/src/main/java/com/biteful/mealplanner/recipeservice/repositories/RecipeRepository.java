package com.biteful.mealplanner.recipeservice.repositories;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository extends MongoRepository<Recipe, String> {

    List<Recipe> findByUserId(UUID userId);

    List<Recipe> findByUserIdAndVisibility(UUID userId, String visibility);
}
