package com.biteful.mealplanner.userservice.controllers;

import com.biteful.mealplanner.userservice.config.security.UserPrincipal;
import com.biteful.mealplanner.userservice.domain.dto.AdminUsersDto;
import com.biteful.mealplanner.userservice.domain.dto.RoleRequest;
import com.biteful.mealplanner.userservice.domain.dto.UserDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.mappers.impl.AdminUserMapper;
import com.biteful.mealplanner.userservice.mappers.impl.UserMapper;
import com.biteful.mealplanner.userservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UsersController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AdminUserMapper adminUserMapper;

    UsersController(UserService userService, UserMapper userMapper, AdminUserMapper adminUserMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.adminUserMapper = adminUserMapper;
    }

    @GetMapping("/api/users/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserDto getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.getId();

        UserEntity user = userService.getUserByID(userId);

        return userMapper.mapTo(user);
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminUsersDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

         Page<UserEntity> usersPage = userService.getAllUsers(pageable);

        return usersPage.map(adminUserMapper::mapTo);
    }

    @GetMapping("/api/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUsersDto getUser(@PathVariable UUID id) {
        UserEntity userEntity = userService.getUserByID(id);

        return adminUserMapper.mapTo(userEntity);
    }

    @PatchMapping("/api/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeUserRole(
            @PathVariable UUID id,
            @RequestBody RoleRequest request
            ) {
        userService.changeRole(id, request.getRole());
    }

}
