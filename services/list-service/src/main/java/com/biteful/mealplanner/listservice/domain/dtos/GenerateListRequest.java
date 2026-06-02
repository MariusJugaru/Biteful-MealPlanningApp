package com.biteful.mealplanner.listservice.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateListRequest {
    private String title;
    private LocalDate start;
    private LocalDate end;
}
