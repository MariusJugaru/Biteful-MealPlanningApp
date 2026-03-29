package com.biteful.mealplanner.userservice.exceptions.runtime;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid credentials.");
    }
}
