package com.biteful.mealplanner.userservice.exceptions.runtime;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
