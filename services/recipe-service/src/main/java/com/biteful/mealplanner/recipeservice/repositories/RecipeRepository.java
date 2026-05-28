package com.biteful.mealplanner.recipeservice.repositories;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends MongoRepository<Recipe, String> {

    Page<Recipe> findByUserId(UUID userId, Pageable pageable);

    Optional<Recipe> findByIdAndUserId(String id, UUID userId);

    List<Recipe> findByUserIdAndVisibility(UUID userId, String visibility);

    Page<Recipe> findByVisibility(String visibility, Pageable pageable);

    List<Recipe> findByIdIn(List<String> ids);
}
