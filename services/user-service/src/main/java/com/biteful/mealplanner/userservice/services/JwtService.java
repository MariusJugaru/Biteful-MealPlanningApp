package com.biteful.mealplanner.userservice.services;

import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import io.jsonwebtoken.Claims;

public interface JwtService {

    public String generateToken(UserEntity userEntity);

    public Claims parseJwt(String token);
}
