package com.biteful.mealplanner.userservice.services.impl;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.UpdatePasswordRequestDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.repositories.UserRepository;
import com.biteful.mealplanner.userservice.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    // CRUD logic
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> getUserByID(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity updateUserPassword(UUID id, UpdatePasswordRequestDto request) {
        Optional<UserEntity> user = getUserByID(id);
        if (user.isEmpty()) throw new RuntimeException("User not found");

        if (!passwordEncoder.matches(request.getOldPassword(), user.get().getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.get().setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user.get());
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    // Business logic
    @Override
    public String loginUser(LoginRequestDto loginRequestDto) {
        String usernameOrEmail = loginRequestDto.getUsernameOrEmail();
        Optional<UserEntity> user = userRepository.findByUsername(usernameOrEmail);

        if (user.isEmpty()) {
            user = userRepository.findByEmail(usernameOrEmail);
        }

        if (user.isEmpty())
            throw new RuntimeException("The credentials don't match any user");

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPasswordHash()))
            throw new RuntimeException("The credentials don't match any user");

        return jwtService.generateToken(user.get());
    }

}
