package com.biteful.mealplanner.listservice.controllers;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.ListItemRequest;
import com.biteful.mealplanner.listservice.domain.dtos.ListItemResponse;
import com.biteful.mealplanner.listservice.domain.dtos.ListRequest;
import com.biteful.mealplanner.listservice.domain.dtos.ListResponse;
import com.biteful.mealplanner.listservice.services.ListService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    public final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ListResponse> getMyListsEndpoint(@AuthenticationPrincipal UserPrincipal principal) {
        return listService.getMyLists(principal);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ListResponse> getListsForUserEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID userId
    ) {
        return listService.getListsForUser(principal, userId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ListResponse createListEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody ListRequest request
    ) {
        return listService.createList(principal, request);
    }

    @PatchMapping("/{listId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ListResponse updateListEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @RequestBody ListRequest request
    ) {
        return listService.updateList(principal, listId, request);
    }

    @DeleteMapping("/{listId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void updateListEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId
    ) {
        listService.deleteList(principal, listId);
    }

    @GetMapping("/{listId}/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ListItemResponse> getListItems(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId
    ) {
        return listService.getList(principal, listId);
    }

    @PostMapping("/{listId}/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ListItemResponse addItemToListEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @RequestBody ListItemRequest listItemRequest
    ) {
        return listService.addItem(principal, listId, listItemRequest);
    }

    @PutMapping("/{listId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ListItemResponse updateItemEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @PathVariable Long itemId,
            @RequestBody ListItemRequest listItemRequest
    ) {
        return listService.updateItem(principal, listId, itemId, listItemRequest);
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteItemFromListEndpoint(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID listId,
            @PathVariable Long itemId
    ) {
        listService.deleteItem(principal, listId, itemId);
    }
}
