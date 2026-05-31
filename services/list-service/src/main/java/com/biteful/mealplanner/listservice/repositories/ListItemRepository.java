package com.biteful.mealplanner.listservice.repositories;

import com.biteful.mealplanner.listservice.domain.entities.ListEntity;
import com.biteful.mealplanner.listservice.domain.entities.ListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListItemRepository extends JpaRepository<ListItemEntity, Long> {
    List<ListItemEntity> findAllByShoppingList(ListEntity shoppingList);
}
