package com.superBoy.FoodieHub.ExceptionHandling;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
