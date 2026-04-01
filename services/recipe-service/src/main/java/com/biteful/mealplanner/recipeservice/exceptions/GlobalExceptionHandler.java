package com.biteful.mealplanner.recipeservice.exceptions;

import com.biteful.mealplanner.recipeservice.exceptions.runtime.NotAllowed;
import com.biteful.mealplanner.recipeservice.exceptions.runtime.RecipeNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RecipeNotFound.class)
    public ResponseEntity<Map<String, Object>> handleRecipeNotFound(RecipeNotFound ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(NotAllowed.class)
    public ResponseEntity<Map<String, Object>> handleNotAllowed(NotAllowed ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }
}
