package com.superBoy.FoodieHub.ExceptionHandling;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String message) {
        super(message);
    }
}
