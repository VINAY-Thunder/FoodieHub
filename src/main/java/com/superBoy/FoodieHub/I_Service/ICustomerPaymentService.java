package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import java.util.Map;

import com.razorpay.RazorpayException;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentSummaryDTO;

public interface ICustomerPaymentService {
	// Create RazorPay order → returns razorpayOrderId
	Map<String, String> createRazorPayOrder(CustomerPaymentRequestDTO dto) throws RazorpayException, CustomerNotFoundException;

	// Verify signature + Save payment to DB
	CustomerPaymentResponseDTO verifyAndSavePayment(CustomerPaymentRequestDTO dto) throws CustomerNotFoundException;

	// Get payment by paymentId
	CustomerPaymentResponseDTO getPaymentById(Long id);

	// Get all payments by customerId
	List<CustomerPaymentSummaryDTO> getPaymentsByCustomerId(Long customerId) throws CustomerNotFoundException;

	// Get all payments
	List<CustomerPaymentSummaryDTO> getAllPayments();
}
