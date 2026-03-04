package com.superBoy.FoodieHub.ExceptionHandling;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(String message) {
		super(message);
	}
}
