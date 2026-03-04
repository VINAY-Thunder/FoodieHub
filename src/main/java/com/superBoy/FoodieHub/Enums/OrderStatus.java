package com.superBoy.FoodieHub.Enums;

public enum OrderStatus {

	CREATED, // Order created but not confirmed
	CONFIRMED, // Order confirmed
	PREPARING, // Food is being prepared
	READY, // Ready for pickup / delivery
	COMPLETED, // Order delivered / completed
	CANCELLED ,// Order cancelled
	PENDING,
	DELIVERED
}
