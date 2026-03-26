package com.biteful.mealplanner.userservice.services.impl;

import com.biteful.mealplanner.userservice.domain.dto.LoginRequestDto;
import com.biteful.mealplanner.userservice.domain.dto.LoginResponseDto;
import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.repositories.UserRepository;
import com.biteful.mealplanner.userservice.services.AuthService;
import com.biteful.mealplanner.userservice.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        String usernameOrEmail = loginRequestDto.getUsernameOrEmail();
        Optional<UserEntity> user = userRepository.findByUsername(usernameOrEmail);

        if (user.isEmpty()) {
            user = userRepository.findByEmail(usernameOrEmail);
        }

        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");

        String token = jwtService.generateToken(user.get());

        return LoginResponseDto.builder()
                .token(token)
                .username(user.get().getUsername())
                .role(user.get().getUserRole().name())
                .build();
    }
}
