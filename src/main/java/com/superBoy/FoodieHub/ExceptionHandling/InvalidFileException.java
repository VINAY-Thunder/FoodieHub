package com.superBoy.FoodieHub.ExceptionHandling;

public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }
}