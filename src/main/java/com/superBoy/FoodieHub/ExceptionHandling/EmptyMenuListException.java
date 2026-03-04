package com.superBoy.FoodieHub.ExceptionHandling;

public class EmptyMenuListException extends RuntimeException {
    public EmptyMenuListException(String message) {
        super(message);
    }
}