package com.superBoy.FoodieHub.ExceptionHandling;

public class SupplierNotPaidException extends RuntimeException {
	public SupplierNotPaidException(String message) {
		super(message);
	}
}
