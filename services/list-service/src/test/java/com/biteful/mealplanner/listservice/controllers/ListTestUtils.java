package com.biteful.mealplanner.listservice.controllers;

import com.biteful.mealplanner.listservice.domain.dtos.ListItemRequest;
import com.biteful.mealplanner.listservice.domain.dtos.ListRequest;
import com.biteful.mealplanner.listservice.domain.dtos.UpdateCheckedRequest;
import com.biteful.mealplanner.listservice.domain.entities.ListEntity;

public class ListTestUtils {
    static ListRequest getListA() {
        return ListRequest.builder()
                .title("My new List")
                .build();
    }

    static ListRequest getListB() {
        return ListRequest.builder()
                .title("My List")
                .build();
    }

    static ListItemRequest getItemA() {
        return ListItemRequest.builder()
                .name("Pasta")
                .unit("g")
                .quantity(500.0)
                .build();
    }

    static ListItemRequest getItemB() {
        return ListItemRequest.builder()
                .name("Sauce")
                .unit("l")
                .quantity(250.0)
                .build();
    }

    static UpdateCheckedRequest getChecked() {
        return UpdateCheckedRequest.builder()
                .checked(true)
                .build();
    }

    static UpdateCheckedRequest getUnchecked() {
        return UpdateCheckedRequest.builder()
                .checked(false)
                .build();
    }
}
