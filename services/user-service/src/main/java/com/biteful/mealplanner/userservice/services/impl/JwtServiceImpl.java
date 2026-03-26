package com.biteful.mealplanner.userservice.services.impl;

import com.biteful.mealplanner.userservice.domain.entities.UserEntity;
import com.biteful.mealplanner.userservice.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final String secret;
    private final long expirationMs;

    JwtServiceImpl(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expirationMs}") long expirationMs
    ) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }


    @Override
    public String generateToken(UserEntity userEntity) {
        return Jwts.builder()
                .setSubject(userEntity.getId().toString())
                .claim("username", userEntity.getUsername())
                .claim("role", userEntity.getUserRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationMs))
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
