package com.biteful.mealplanner.recipeservice.repositories.impl;

import com.biteful.mealplanner.recipeservice.domain.documents.Recipe;
import com.biteful.mealplanner.recipeservice.domain.entities.MealType;
import com.biteful.mealplanner.recipeservice.repositories.RecipeRepositoryCustom;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SampleOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public RecipeRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Recipe> getRecipeCandidates(MealType type, List<String> allergens, boolean excludeUser, UUID userId) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (userId != null) {
            if (excludeUser) {
                criteriaList.add(Criteria.where("visibility").is("public"));
                criteriaList.add(Criteria.where("userId").ne(userId));
            } else {
//                criteriaList.add(Criteria.where("visibility").is("private"));
                criteriaList.add(Criteria.where("userId").is(userId));
            }
        }

        if (type != null) {
            criteriaList.add(
                    Criteria.where("tags")
                            .regex("^" + type.name() + "$", "i")
            );
        }

        if (allergens != null && !allergens.isEmpty()) {
            criteriaList.add(Criteria.where("tags").nin(allergens));
        }

        MatchOperation match = Aggregation.match(
                new Criteria().andOperator(criteriaList.toArray(new Criteria[0]))
        );

        SampleOperation sample = Aggregation.sample(50);

        Aggregation aggregation = Aggregation.newAggregation(match, sample);

        return mongoTemplate.aggregate(aggregation, "recipes", Recipe.class)
                .getMappedResults();
    }
}
