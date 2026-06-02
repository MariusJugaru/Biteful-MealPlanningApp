package com.biteful.mealplanner.listservice.services;

import com.biteful.mealplanner.listservice.config.security.UserPrincipal;
import com.biteful.mealplanner.listservice.domain.dtos.ListResponse;

import java.time.LocalDate;

public interface ListGenerationService {

    ListResponse generate(UserPrincipal principal, String authHeader, LocalDate start, LocalDate end, String title);
}
