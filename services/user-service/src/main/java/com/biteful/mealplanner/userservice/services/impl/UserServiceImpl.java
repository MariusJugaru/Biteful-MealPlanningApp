package com.biteful.mealplanner.userservice.services.impl;

import com.biteful.mealplanner.userservice.domain.dto.UpdatePasswordRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.exceptions.runtime.EmailAlreadyExistsException;
import com.biteful.mealplanner.userservice.exceptions.runtime.InvalidPasswordException;
import com.biteful.mealplanner.userservice.exceptions.runtime.UserNotFoundException;
import com.biteful.mealplanner.userservice.repositories.UserRepository;
import com.biteful.mealplanner.userservice.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // CRUD logic
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isPresent())
            throw new EmailAlreadyExistsException();
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash()));
        return userRepository.save(userEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity getUserByID(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserEntity updateUserPassword(UUID id, UpdatePasswordRequestDto request) {
        UserEntity user = getUserByID(id);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }

    @Override
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }


}
