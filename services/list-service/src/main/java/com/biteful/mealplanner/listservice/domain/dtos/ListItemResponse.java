package com.biteful.mealplanner.listservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListItemResponse {

    private Long id;

    private UUID shoppingListId;

    private String name;
    private Double quantity;
    private String unit;
}
