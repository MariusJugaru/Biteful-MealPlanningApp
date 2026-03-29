package com.biteful.mealplanner.userservice.exceptions.runtime;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Old password is incorrect.");
    }
}
