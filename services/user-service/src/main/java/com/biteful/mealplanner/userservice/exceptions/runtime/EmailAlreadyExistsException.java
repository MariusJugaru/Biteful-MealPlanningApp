package com.biteful.mealplanner.userservice.exceptions.runtime;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already in use.");
    }
}
