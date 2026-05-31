package com.biteful.mealplanner.listservice.config.security;

import com.biteful.mealplanner.jwtutils.JwtUtilsService;
import com.biteful.mealplanner.jwtutils.JwtUtilsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String string;

    @Bean
    public JwtUtilsService jwtUtilsService() {
        return new JwtUtilsServiceImpl(string);
    }
}
