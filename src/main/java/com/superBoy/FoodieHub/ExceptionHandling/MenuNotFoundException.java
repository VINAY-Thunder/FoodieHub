package com.superBoy.FoodieHub.ExceptionHandling;

public class MenuNotFoundException extends RuntimeException {

	public MenuNotFoundException(String message) {
		super(message);
	}

}
