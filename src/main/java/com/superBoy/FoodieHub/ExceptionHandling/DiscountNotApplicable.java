package com.superBoy.FoodieHub.ExceptionHandling;

public class DiscountNotApplicable extends RuntimeException {

	public DiscountNotApplicable(String message) {
		super(message);
	}

}
