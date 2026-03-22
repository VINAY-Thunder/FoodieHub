package com.superBoy.FoodieHub.ExceptionHandling;

public class DuplicatePaymentException extends RuntimeException {
    public DuplicatePaymentException(String message) {
        super(message);
    }
}
