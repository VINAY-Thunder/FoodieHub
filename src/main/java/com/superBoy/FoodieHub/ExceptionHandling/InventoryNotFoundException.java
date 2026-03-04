package com.superBoy.FoodieHub.ExceptionHandling;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}