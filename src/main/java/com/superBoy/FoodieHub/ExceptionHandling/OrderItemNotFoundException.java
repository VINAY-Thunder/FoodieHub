package com.superBoy.FoodieHub.ExceptionHandling;

public class OrderItemNotFoundException extends RuntimeException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}