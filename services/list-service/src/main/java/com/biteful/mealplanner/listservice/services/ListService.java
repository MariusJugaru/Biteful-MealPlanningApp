package com.biteful.mealplanner.listservice.services;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.*;
import com.biteful.mealplanner.listservice.domain.entities.ListEntity;
import com.biteful.mealplanner.listservice.domain.entities.ListItemEntity;

import java.util.List;
import java.util.UUID;

public interface ListService {

    // Creates a new list
    ListResponse createList(UserPrincipal userPrincipal, ListRequest listRequest);

    // Returns all the lists in the DB
    List<ListResponse> getMyLists(UserPrincipal userPrincipal);

    // Returns all the lists of a user
    List<ListResponse> getListsForUser(UserPrincipal userPrincipal, UUID userId);

    // Returns the list
    ListResponse getListData(UserPrincipal userPrincipal, UUID listId);

    // Updates the name of a list
    ListResponse updateList(UserPrincipal userPrincipal, UUID listId, ListRequest listRequest);

    // Deletes a list
    void deleteList(UserPrincipal userPrincipal, UUID listId);

    // Adds an item to a list
    ListItemResponse addItem(UserPrincipal userPrincipal, UUID listId, ListItemRequest listItemRequest);

    // Returns a list
    List<ListItemResponse> getList(UserPrincipal userPrincipal, UUID listId);

    // Updated an item in a list
    ListItemResponse updateItem(UserPrincipal userPrincipal, UUID listId, Long itemId, ListItemRequest listItemRequest);

    void deleteItem(UserPrincipal userPrincipal, UUID listId, Long itemId);

    ListItemResponse updateChecked(UserPrincipal userPrincipal, UUID listId, Long itemId, UpdateCheckedRequest checkedRequest);
}
