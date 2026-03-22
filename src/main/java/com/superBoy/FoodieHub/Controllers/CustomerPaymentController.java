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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class CustomerPaymentController {

	private final ICustomerPaymentService paymentService;

	@Autowired
	public CustomerPaymentController(ICustomerPaymentService paymentService) {
		this.paymentService = paymentService;
	}

	// ═══════════════════════════════════════════
	// POST — Create RazorPay Order
	// ═══════════════════════════════════════════
	@PostMapping("/create-order")
	public ResponseEntity<Map<String, String>> createRazorPayOrder(@RequestBody @Valid CustomerPaymentRequestDTO dto)
			throws RazorpayException, CustomerNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createRazorPayOrder(dto));
	}

	// ═══════════════════════════════════════════
	// POST — Verify + Save Payment
	// ═══════════════════════════════════════════
	@PostMapping("/verify")
	public ResponseEntity<CustomerPaymentResponseDTO> verifyAndSavePayment(
			@RequestBody @Valid CustomerPaymentRequestDTO dto) throws CustomerNotFoundException {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.verifyAndSavePayment(dto));
	}

	// ═══════════════════════════════════════════
	// GET — Get Payment By ID
	// ═══════════════════════════════════════════
	@GetMapping("/{id}")
	public ResponseEntity<CustomerPaymentResponseDTO> getPaymentById(@PathVariable Long id) {
		return ResponseEntity.ok(paymentService.getPaymentById(id));
	}

	// ═══════════════════════════════════════════
	// GET — Get All Payments By Customer
	// ═══════════════════════════════════════════
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<CustomerPaymentSummaryDTO>> getPaymentsByCustomerId(@PathVariable Long customerId) throws CustomerNotFoundException {
		return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
	}

	// ═══════════════════════════════════════════
	// GET — Get All Payments (Admin)
	// ═══════════════════════════════════════════
	@GetMapping
	public ResponseEntity<List<CustomerPaymentSummaryDTO>> getAllPayments() {
		return ResponseEntity.ok(paymentService.getAllPayments());
	}
}
