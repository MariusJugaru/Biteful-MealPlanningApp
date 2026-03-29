package com.biteful.mealplanner.userservice.services;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.UpdatePasswordRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // Creates a user
    UserEntity createUser(UserEntity userEntity);

    // Checks if user exists by username
    public boolean existsByUsername(String username);

    // Returns a user by ID
    UserEntity getUserByID(UUID id);

    // Returns a user by username
    UserEntity getUserByUsername(String username);

    // Returns a user by email
    UserEntity getUserByEmail(String email);

    // Updates a user's password
    UserEntity updateUserPassword(UUID id, UpdatePasswordRequestDto request);

    // Returns all users
    Page<UserEntity> getAllUsers(Pageable pageable);

    // Deletes a user by ID
    void deleteUser(UUID id);
}
