package com.superBoy.FoodieHub.ExceptionHandling;

public class InvalidOrderException extends RuntimeException {

	public InvalidOrderException(String message) {
		super(message);
	}

}
