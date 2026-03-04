package com.superBoy.FoodieHub.ExceptionHandling;

public class CategoryNotFoundException extends RuntimeException {
	
	public CategoryNotFoundException(String message) {
		super(message);
	}

}
