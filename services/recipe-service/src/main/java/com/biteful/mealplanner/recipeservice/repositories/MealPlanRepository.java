package com.biteful.mealplanner.recipeservice.repositories;

import com.biteful.mealplanner.recipeservice.domain.entities.MealPlanEntity;
import com.biteful.mealplanner.recipeservice.domain.entities.MealPlanId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlanEntity, MealPlanId> {

    List<MealPlanEntity> findByIdUserIdAndIdDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
