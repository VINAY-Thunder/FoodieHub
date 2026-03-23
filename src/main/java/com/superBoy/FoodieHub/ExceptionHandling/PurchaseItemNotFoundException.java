package com.superBoy.FoodieHub.ExceptionHandling;

public class PurchaseItemNotFoundException extends RuntimeException {

	public PurchaseItemNotFoundException(String message) {
		super(message);
	}
}
