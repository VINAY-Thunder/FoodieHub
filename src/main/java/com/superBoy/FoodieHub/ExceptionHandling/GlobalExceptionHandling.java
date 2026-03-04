package com.superBoy.FoodieHub.ExceptionHandling;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.superBoy.FoodieHub.Enums.MenuStatus;

@RestControllerAdvice
public class GlobalExceptionHandling {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException exc) {
		return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<String> handleCategoryNotFound(CategoryNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AdminNotFoundException.class)
	public ResponseEntity<String> handleAdminNotFound(AdminNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileStorageException.class)
	public ResponseEntity<String> handleFileStorageError(FileStorageException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<String> handleInvalidFile(InvalidFileException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MenuNotFoundException.class)
	public ResponseEntity<String> handleMenuNotFound(MenuNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DiscountNotApplicable.class)
	public ResponseEntity<String> handleDiscountNotApplicable(DiscountNotApplicable e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException e) {
		String message = "Invalid input format or invalid value for a field. Please check your JSON data and ensure enum values are correct.";
		if (e.getMostSpecificCause() != null) {
			message += " Error: " + e.getMostSpecificCause().getMessage();
		}
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	// Catches invalid enum value passed in request (e.g. "INVALID" instead of
	// "AVAILABLE")
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		String message = "Invalid value for parameter '" + e.getName() + "': '" + e.getValue() + "'.";
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(OrderItemNotFoundException.class)
	public ResponseEntity<String> handleOrderItemNotFound(OrderItemNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
	}

	@ExceptionHandler(AddressNotFoundException.class)
	public ResponseEntity<String> handleAddressNotFound(AddressNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
	}

	@ExceptionHandler(InvalidOrderException.class)
	public ResponseEntity<String> handleInvalidOrder(InvalidOrderException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
