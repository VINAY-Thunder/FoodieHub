package com.superBoy.FoodieHub.ExceptionHandling;

public class AddressNotFoundException extends RuntimeException {

	public AddressNotFoundException(String message) {
		super(message);
	}

}
