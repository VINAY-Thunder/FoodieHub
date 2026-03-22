package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.superBoy.FoodieHub.Enums.CustomerPaymentStatus;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.DuplicatePaymentException;
import com.superBoy.FoodieHub.ExceptionHandling.OrderNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.PaymentNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.PaymentVerificationException;
import com.superBoy.FoodieHub.I_Service.ICustomerPaymentService;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.CustomerPayment;
import com.superBoy.FoodieHub.Model.Orders;
import com.superBoy.FoodieHub.Repository.CustomerPaymentRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Repository.OrderRepository;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentSummaryDTO;

import jakarta.transaction.Transactional;

@Service
public class CustomerPaymentService implements ICustomerPaymentService {

	private final CustomerPaymentRepository paymentRepo;
	private final CustomerRepository customerRepo;
	private final OrderRepository orderRepo;
	private final RazorpayClient razorpayClient;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	@Autowired
	public CustomerPaymentService(CustomerPaymentRepository paymentRepo, CustomerRepository customerRepo,
			OrderRepository orderRepo, RazorpayClient razorpayClient) {
		this.paymentRepo = paymentRepo;
		this.customerRepo = customerRepo;
		this.orderRepo = orderRepo;
		this.razorpayClient = razorpayClient;
	}

	// METHOD 1 — Create RazorPay Order
	@Override
	public Map<String, String> createRazorPayOrder(CustomerPaymentRequestDTO dto) throws RazorpayException {

		int amountInPaise = dto.getAmount().multiply(BigDecimal.valueOf(100)).intValue();

		JSONObject options = new JSONObject();
		options.put("amount", amountInPaise);
		options.put("currency", "INR");
		options.put("receipt", "receipt_order_" + dto.getOrderId());

		Order razorPayOrder = razorpayClient.orders.create(options);

		Map<String, String> response = new HashMap<>();
		response.put("razorpayOrderId", razorPayOrder.get("id"));
		response.put("amount", String.valueOf(amountInPaise));
		response.put("currency", "INR");

		return response;
	}

	// METHOD 2 — Verify Signature + Save Payment
	@Override
	@Transactional
	public CustomerPaymentResponseDTO verifyAndSavePayment(CustomerPaymentRequestDTO dto)
			throws CustomerNotFoundException {

		// Step 1: Validate customer
		Customer customer = customerRepo.findById(dto.getCustomerId())
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + dto.getCustomerId()));

		// Step 2: Validate order
		Orders order = orderRepo.findById(dto.getOrderId())
				.orElseThrow(() -> new OrderNotFoundException("Order not found: " + dto.getOrderId()));

		// STEP 2.5: Prevent Double Payment!
		if (paymentRepo.existsByOrderOrderIdAndPaymentStatus(dto.getOrderId(), CustomerPaymentStatus.SUCCESS)) {
			throw new DuplicatePaymentException(
					"This Order has already been paid successfully! Duplicate payments are not allowed.");
		}

		// Step 3: Verify signature
		boolean isValid = verifySignature(dto.getRazorpayOrderId(), dto.getRazorpayPaymentId(),
				dto.getRazorpaySignature());

		if (!isValid) {
			throw new PaymentVerificationException("Payment verification failed!");
		}

		// Step 4: Build entity
		CustomerPayment payment = new CustomerPayment();
		payment.setCustomer(customer);
		payment.setOrder(order);
		payment.setAmount(dto.getAmount());
		payment.setPaymentMethod(dto.getPaymentMethod());
		payment.setTransactionId(dto.getRazorpayPaymentId());
		payment.setPaymentStatus(CustomerPaymentStatus.SUCCESS);
		payment.setPaymentDate(LocalDateTime.now());

		// Step 5: Save and return
		CustomerPayment saved = paymentRepo.save(payment);
		return mapToResponseDTO(saved);
	}

	// METHOD 3 — Get Payment By ID
	@Override
	public CustomerPaymentResponseDTO getPaymentById(Long id) {

		CustomerPayment payment = paymentRepo.findById(id)
				.orElseThrow(() -> new com.superBoy.FoodieHub.ExceptionHandling.PaymentNotFoundException(
						"Payment not found with ID: " + id));

		return mapToResponseDTO(payment);
	}

	// ═══════════════════════════════════════════
	// METHOD 4 — Get Payments By Customer
	// ═══════════════════════════════════════════
	@Override
	public List<CustomerPaymentSummaryDTO> getPaymentsByCustomerId(Long customerId) throws CustomerNotFoundException {

		customerRepo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerId));

		List<CustomerPayment> payments = paymentRepo.findByCustomerCustomerId(customerId);

		if (payments.isEmpty()) {
			throw new PaymentNotFoundException("No payments found for customer: " + customerId);
		}

		return payments.stream().map(p -> mapToSummaryDTO(p)).toList();
	}

	// METHOD 5 — Get All Payments
	@Override
	public List<CustomerPaymentSummaryDTO> getAllPayments() {

		List<CustomerPayment> payments = paymentRepo.findAll();

		if (payments.isEmpty()) {
			throw new PaymentNotFoundException("No payments found!");
		}

		return payments.stream().map(p -> mapToSummaryDTO(p)).toList();
	}

	// PRIVATE — Map to ResponseDTO
	private CustomerPaymentResponseDTO mapToResponseDTO(CustomerPayment payment) {
		CustomerPaymentResponseDTO dto = new CustomerPaymentResponseDTO();

		dto.setPaymentId(payment.getPaymentId());
		dto.setAmount(payment.getAmount());
		dto.setTransactionId(payment.getTransactionId());
		dto.setPaymentDate(payment.getPaymentDate());
		dto.setPaymentMethod(payment.getPaymentMethod().name());
		dto.setPaymentStatus(payment.getPaymentStatus().name());
		dto.setCustomerId(payment.getCustomer().getCustomerId());
		dto.setCustomerName(payment.getCustomer().getName());
		dto.setOrderId(payment.getOrder().getOrderId());

		return dto;
	}

	// PRIVATE — Map to SummaryDTO
	private CustomerPaymentSummaryDTO mapToSummaryDTO(CustomerPayment payment) {
		CustomerPaymentSummaryDTO dto = new CustomerPaymentSummaryDTO();

		dto.setPaymentId(payment.getPaymentId());
		dto.setAmount(payment.getAmount());
		dto.setPaymentStatus(payment.getPaymentStatus()); // ✅ CustomerPaymentStatus
		dto.setPaymentDate(payment.getPaymentDate());

		return dto;
	}

	// PRIVATE — Verify Signature
	private boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String signature) {
		try {
			String payload = razorpayOrderId + "|" + razorpayPaymentId;
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
			mac.init(secretKey);
			byte[] hash = mac.doFinal(payload.getBytes());
			String generated = bytesToHex(hash);
			return generated.equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}