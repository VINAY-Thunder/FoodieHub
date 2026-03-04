package com.superBoy.FoodieHub.ExceptionHandling;

public class CustomerNotFoundException extends Exception {
	
	public CustomerNotFoundException (String message) {
		super(message);
	}
}
