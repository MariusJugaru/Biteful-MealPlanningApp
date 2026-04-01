package com.biteful.mealplanner.recipeservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeSummaryDto {
    private String id;
    private String title;
    private String image;
}
