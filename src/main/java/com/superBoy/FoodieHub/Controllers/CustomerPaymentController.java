package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.razorpay.RazorpayException;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.ICustomerPaymentService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
@Tag(name = "Customer Payment", description = "Razorpay-powered customer checkout and payment verification")
public class CustomerPaymentController {

	private final ICustomerPaymentService paymentService;

	@Autowired
	public CustomerPaymentController(ICustomerPaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Operation(summary = "Create Razorpay order", description = "Initiates a Razorpay payment order for the customer. Returns the Razorpay order ID and amount needed by the frontend checkout widget.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Razorpay order created"),
		@ApiResponse(responseCode = "400", description = "Validation error"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@PostMapping("/create-order")
	public ResponseEntity<Map<String, String>> createRazorPayOrder(@RequestBody @Valid CustomerPaymentRequestDTO dto)
			throws RazorpayException, CustomerNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createRazorPayOrder(dto));
	}

	@Operation(summary = "Verify and save payment", description = "Validates the Razorpay payment signature and persists the payment record in the database.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Payment verified and saved"),
		@ApiResponse(responseCode = "400", description = "Invalid signature or validation error"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@PostMapping("/verify")
	public ResponseEntity<CustomerPaymentResponseDTO> verifyAndSavePayment(
			@RequestBody @Valid CustomerPaymentRequestDTO dto) throws CustomerNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.verifyAndSavePayment(dto));
	}

	@Operation(summary = "Get payment by ID", description = "Fetches the details of a specific payment record.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Payment found"),
		@ApiResponse(responseCode = "404", description = "Payment not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<CustomerPaymentResponseDTO> getPaymentById(@PathVariable Long id) {
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}

	@Operation(summary = "Get payments by customer", description = "Returns a summary list of all payments made by a specific customer.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Payments returned"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<CustomerPaymentSummaryDTO>> getPaymentsByCustomerId(@PathVariable Long customerId)
			throws CustomerNotFoundException {
		return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
	}

	@Operation(summary = "Get all payments (Admin)", description = "Returns a summary of all customer payments. Admin-only endpoint.")
	@ApiResponse(responseCode = "200", description = "All payments returned")
	@GetMapping
	public ResponseEntity<List<CustomerPaymentSummaryDTO>> getAllPayments() {
		return ResponseEntity.ok(paymentService.getAllPayments());
	}
}
