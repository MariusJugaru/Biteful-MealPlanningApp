package com.biteful.mealplanner.recipeservice.domain.documents;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "recipes")
@CompoundIndex(name = "user_visibility_idx", def = "{'userId': 1, 'visibility': 1}")
public class Recipe {

    @Id
    private String id;

    @Indexed
    private UUID userId;

    @Indexed
    private String userType;

    private String title;
    private String description;
    private List<String> ingredients;
    private String instructions;
    private List<String> tags;
    private int prepTime;
    private int cookTime;
    private String image;
    private String visibility;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
