package com.biteful.mealplanner.listservice.services.impl;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.ListItemRequest;
import com.biteful.mealplanner.listservice.domain.dtos.ListItemResponse;
import com.biteful.mealplanner.listservice.domain.dtos.ListRequest;
import com.biteful.mealplanner.listservice.domain.dtos.ListResponse;
import com.biteful.mealplanner.listservice.domain.entities.ListEntity;
import com.biteful.mealplanner.listservice.domain.entities.ListItemEntity;
import com.biteful.mealplanner.listservice.repositories.ListItemRepository;
import com.biteful.mealplanner.listservice.repositories.ListRepository;
import com.biteful.mealplanner.listservice.services.ListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ListServiceImpl implements ListService {

    public final ListRepository listRepository;

    public final ListItemRepository listItemRepository;

    public ListServiceImpl(ListRepository listRepository, ListItemRepository listItemRepository) {
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
    }

    @Override
    public ListResponse createList(UserPrincipal userPrincipal, ListRequest listRequest) {

        ListEntity listEntity = ListEntity.builder()
                .userId(userPrincipal.getId())
                .title(listRequest.getTitle())
                .build();

        listEntity = listRepository.save(listEntity);

        return ListServiceUtils.toResponse(listEntity);
    }

    @Override
    public List<ListResponse> getMyLists(UserPrincipal userPrincipal) {
        List<ListEntity> entities = listRepository.findAllByUserId(userPrincipal.getId());

        List<ListResponse> responses = new ArrayList<>();
        for (ListEntity entity : entities) {
            responses.add(ListServiceUtils.toResponse(entity));
        }

        return responses;
    }

    @Override
    public List<ListResponse> getListsForUser(UserPrincipal userPrincipal, UUID userId) {
        if (!userPrincipal.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Insufficient permissions!");
        }

        List<ListEntity> entities = listRepository.findAllByUserId(userId);

        List<ListResponse> responses = new ArrayList<>();
        for (ListEntity entity : entities) {
            responses.add(ListServiceUtils.toResponse(entity));
        }

        return responses;
    }

    @Override
    public ListResponse updateList(UserPrincipal userPrincipal, UUID listId, ListRequest listRequest) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        listEntity.setTitle(listRequest.getTitle());
        listEntity = listRepository.save(listEntity);

        return ListServiceUtils.toResponse(listEntity);
    }

    @Override
    public void deleteList(UserPrincipal userPrincipal, UUID listId) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        listRepository.delete(listEntity);
    }

    @Override
    public ListItemResponse addItem(UserPrincipal userPrincipal, UUID listId, ListItemRequest listItemRequest) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        ListItemEntity listItemEntity = ListItemEntity.builder()
                .shoppingList(listEntity)
                .name(listItemRequest.getName())
                .unit(listItemRequest.getUnit())
                .quantity(listItemRequest.getQuantity())
                .build();
        listItemEntity = listItemRepository.save(listItemEntity);

        return ListServiceUtils.toResponse(listItemEntity);
    }

    @Override
    public List<ListItemResponse> getList(UserPrincipal userPrincipal, UUID listId) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        List<ListItemEntity> entities = listItemRepository.findAllByShoppingList(listEntity);

        List<ListItemResponse> listItemResponses = new ArrayList<>();
        for (ListItemEntity entity : entities) {
            listItemResponses.add(
                    ListServiceUtils.toResponse(entity)
            );
        }

        return listItemResponses;
    }

    @Override
    public ListItemResponse updateItem(UserPrincipal userPrincipal, UUID listId, Long itemId, ListItemRequest listItemRequest) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListItemEntity listItemEntity = listItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list item not found: " + itemId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        listItemEntity.setName(listItemRequest.getName());
        listItemEntity.setQuantity(listItemRequest.getQuantity());
        listItemEntity.setUnit(listItemRequest.getUnit());

        listItemEntity = listItemRepository.save(listItemEntity);

        return ListServiceUtils.toResponse(listItemEntity);
    }

    @Override
    public void deleteItem(UserPrincipal userPrincipal, UUID listId, Long itemId) {
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list not found: " + listId));

        ListItemEntity listItemEntity = listItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping list item not found: " + itemId));

        ListServiceUtils.validateListAccess(userPrincipal, listEntity);

        listItemRepository.delete(listItemEntity);
    }
}
