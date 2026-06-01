package com.biteful.mealplanner.listservice.services.impl;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.ListItemResponse;
import com.biteful.mealplanner.listservice.domain.dtos.ListResponse;
import com.biteful.mealplanner.listservice.domain.entities.ListEntity;
import com.biteful.mealplanner.listservice.domain.entities.ListItemEntity;
import org.springframework.security.access.AccessDeniedException;

public class ListServiceUtils {
    static ListItemResponse toResponse(ListItemEntity listItemEntity) {
        return ListItemResponse.builder()
                .id(listItemEntity.getId())
                .shoppingListId(listItemEntity.getShoppingList().getId())
                .name(listItemEntity.getName())
                .quantity(listItemEntity.getQuantity())
                .unit(listItemEntity.getUnit())
                .checked(listItemEntity.getChecked())
                .build();
    }

    static ListResponse toResponse(ListEntity listEntity) {
        return ListResponse.builder()
                .id(listEntity.getId())
                .title(listEntity.getTitle())
                .createdAt(listEntity.getCreatedAt())
                .build();
    }

    static void validateListAccess(UserPrincipal userPrincipal, ListEntity listEntity) {
        if (!listEntity.getUserId().equals(userPrincipal.getId())
                && !userPrincipal.getRole().equalsIgnoreCase("ADMIN")) {
            throw new AccessDeniedException("Insufficient permissions");
        }
    }

    static void validateItemInList(ListItemEntity listItemEntity, ListEntity listEntity) {
        if (!listItemEntity.getShoppingList().getId().equals(listEntity.getId())) {
            throw new AccessDeniedException("Insufficient permissions");
        }
    }
}
