package com.biteful.mealplanner.listservice.repositories;

import com.biteful.mealplanner.listservice.domain.entities.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, UUID> {
    List<ListEntity> findAllByUserId(UUID id);
}
