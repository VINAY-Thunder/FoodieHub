package com.superBoy.FoodieHub.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

	// it will Catch invalid enum value passed in request (e.g. "INVALID" instead of
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

	@ExceptionHandler(EmptyMenuListException.class)
	public ResponseEntity<String> handleEmptyMenuList(EmptyMenuListException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
	}

	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<String> handleInventoryNotFound(InventoryNotFoundException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PaymentVerificationException.class)
	public ResponseEntity<String> handlePaymentVerificationNotFound(PaymentVerificationException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SupplierNotException.class)
	public ResponseEntity<String> handleSupplierNotFound(SupplierNotException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SupplierAddressNotException.class)
	public ResponseEntity<String> handleSupplierAddressNotFound(SupplierAddressNotException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PurchaseOrderNotFoundException.class)
	public ResponseEntity<String> handlePurchaseOrderNotFound(PurchaseOrderNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicatePaymentException.class)
	public ResponseEntity<String> handleDuplicatePayment(DuplicatePaymentException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<String> handlePaymentNotFound(PaymentNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PurchaseItemNotFoundException.class)
	public ResponseEntity<String> handlePurchaseItemNotFound(PurchaseItemNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

}
