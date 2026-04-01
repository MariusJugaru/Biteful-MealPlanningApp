package com.biteful.mealplanner.jwtutils;
import io.jsonwebtoken.Claims;

public interface JwtUtilsService {

    public Claims parseJwt(String token);
}
