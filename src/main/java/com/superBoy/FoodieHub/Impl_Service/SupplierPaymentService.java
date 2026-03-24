package com.superBoy.FoodieHub.Impl_Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.annotation.PostConstruct;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.superBoy.FoodieHub.Enums.SupplierPaymentStatus;
import com.superBoy.FoodieHub.ExceptionHandling.PaymentVerificationException;
import com.superBoy.FoodieHub.ExceptionHandling.PurchaseOrderNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.SupplierNotException;
import com.superBoy.FoodieHub.I_Service.ISupplierPaymentService;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Model.SupplierPayment;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Repository.SupplierPaymentRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class SupplierPaymentService implements ISupplierPaymentService {

	private final SupplierPaymentRepository paymentRepo;
	private final SupplierRepository supplierRepo;
	private final PurchaseOrderRepository poRepo;
	private final ModelMapper modelMapper;
	private final RazorpayClient razorpayClient;

    @PersistenceContext
    private EntityManager entityManager;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	@Autowired
	public SupplierPaymentService(SupplierPaymentRepository paymentRepo, SupplierRepository supplierRepo,
			PurchaseOrderRepository poRepo, ModelMapper modelMapper, RazorpayClient razorpayClient) {
		this.paymentRepo = paymentRepo;
		this.supplierRepo = supplierRepo;
		this.poRepo = poRepo;
		this.modelMapper = modelMapper;
		this.razorpayClient = razorpayClient;
	}

    @PostConstruct
    @Transactional
    public void fixDatabaseEnumColumns() {
        try {
            // Fix Hibernate 6 MySQL Enum truncation issues.
            entityManager.createNativeQuery("ALTER TABLE supplier_payment MODIFY payment_method VARCHAR(50)").executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE customer_payment MODIFY payment_method VARCHAR(50)").executeUpdate();
            System.out.println("✅ Database 'payment_method' columns successfully converted to VARCHAR(50).");
        } catch (Exception e) {
            System.out.println("⚠️ Could not execute ALTER TABLE: " + e.getMessage());
        }
    }

	@Override
	public Map<String, String> createRazorPayOrder(SupplierPaymentRequestDTO dto) throws RazorpayException {
		// SECURITY UPDATE: Fetch true total amount directly from database, ignoring any
		// frontend tampering!
		PurchaseOrder po = poRepo.findById(dto.getPurchaseOrderId())
				.orElseThrow(() -> new PurchaseOrderNotFoundException(
						"Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

		int amountInPaise = po.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue();

		JSONObject options = new JSONObject();
		options.put("amount", amountInPaise);
		options.put("currency", "INR");
		options.put("receipt", "receipt_po_" + dto.getPurchaseOrderId());
		options.put("payment_capture", 0); // 0 = Manual Capture later!

		Order razorPayOrder = razorpayClient.orders.create(options);

		Map<String, String> response = new HashMap<>();
		response.put("razorpayOrderId", razorPayOrder.get("id"));
		response.put("amount", String.valueOf(amountInPaise));
		response.put("currency", "INR");

		return response;
	}

	@Override
	@Transactional
	public SupplierPaymentResponseDTO verifyAndSavePayment(SupplierPaymentRequestDTO dto) throws Exception {
		Supplier supplier = supplierRepo.findById(dto.getSupplierId())
				.orElseThrow(() -> new com.superBoy.FoodieHub.ExceptionHandling.SupplierNotException(
						"Supplier not found with ID: " + dto.getSupplierId()));

		PurchaseOrder po = poRepo.findById(dto.getPurchaseOrderId())
				.orElseThrow(() -> new PurchaseOrderNotFoundException(
						"Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

		if (paymentRepo.existsByPurchaseOrderPurchaseOrderIdAndPaymentStatus(dto.getPurchaseOrderId(),
				SupplierPaymentStatus.SUCCESS)) {
			throw new com.superBoy.FoodieHub.ExceptionHandling.DuplicatePaymentException(
					"This Purchase Order has already been paid successfully! Duplicate payments are not allowed.");
		}

		boolean isValid = verifySignature(dto.getRazorpayOrderId(), dto.getRazorpayPaymentId(),
				dto.getRazorpaySignature());
		if (!isValid) {
			throw new PaymentVerificationException("Payment verification failed!");
		}

		SupplierPayment payment = new SupplierPayment();
		payment.setAmount(po.getTotalAmount()); // 100% Secure database link

		// --- FETCH EXACT PAYMENT METHOD FROM RAZORPAY ---
		String exactMethod = dto.getPaymentMethod().name();
		try {
			com.razorpay.Payment rzpPayment = razorpayClient.payments.fetch(dto.getRazorpayPaymentId());
			if (rzpPayment.get("method") != null) {
				exactMethod = rzpPayment.get("method").toString().toUpperCase();
			}
		} catch (Exception e) {
			System.out.println("Could not fetch real method, using fallback.");
		}

		try {
			payment.setPaymentMethod(com.superBoy.FoodieHub.Enums.SupplierPaymentMethod.valueOf(exactMethod));
		} catch (Exception e) {
			payment.setPaymentMethod(dto.getPaymentMethod()); // fallback bounds
		}
		payment.setSupplier(supplier);
		payment.setPurchaseOrder(po);
		payment.setPaymentStatus(SupplierPaymentStatus.SUCCESS);
		payment.setTransactionId(dto.getRazorpayPaymentId());

		SupplierPayment saved = paymentRepo.save(payment);

		// OPTION 2: WE CAPTURE THE PAYMENT MANUALLY HERE BECAUSE THE DB CHECK PASSED
		try {
			JSONObject captureRequest = new JSONObject();
			captureRequest.put("amount", po.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue());
			captureRequest.put("currency", "INR");

			razorpayClient.payments.capture(dto.getRazorpayPaymentId(), captureRequest);

		} catch (RazorpayException e) {
			throw new PaymentVerificationException(
					"Manual Capture Failed on Razorpay! Money is safe. " + e.getMessage());
		}

		return modelMapper.map(saved, SupplierPaymentResponseDTO.class);
	}

	@Override
	@Transactional
	public SupplierPaymentResponseDTO markPaymentAsFailed(SupplierPaymentRequestDTO dto) throws Exception {
		Supplier supplier = supplierRepo.findById(dto.getSupplierId())
				.orElseThrow(() -> new SupplierNotException("Supplier not found with ID: " + dto.getSupplierId()));

		PurchaseOrder po = poRepo.findById(dto.getPurchaseOrderId())
				.orElseThrow(() -> new PurchaseOrderNotFoundException(
						"Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

		SupplierPayment payment = new SupplierPayment();
		payment.setAmount(po.getTotalAmount()); // 100% Secure database link
		payment.setPaymentMethod(dto.getPaymentMethod());
		payment.setSupplier(supplier);
		payment.setPurchaseOrder(po);
		payment.setPaymentStatus(SupplierPaymentStatus.FAILED);
		payment.setTransactionId("FAILED_" + (dto.getRazorpayOrderId() != null ? dto.getRazorpayOrderId() : "UNKNOWN"));

		SupplierPayment saved = paymentRepo.save(payment);
		return modelMapper.map(saved, SupplierPaymentResponseDTO.class);
	}

	private boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String signature) {
		try {
			String payload = razorpayOrderId + "|" + razorpayPaymentId;
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
			mac.init(secretKey);
			byte[] hash = mac.doFinal(payload.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString().equals(signature);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<SupplierPaymentResponseDTO> getPaymentsBySupplierId(Long supplierId) {

		poRepo.findById(supplierId)
				.orElseThrow(() -> new PurchaseOrderNotFoundException("supplier is not found with id: " + supplierId));

		return paymentRepo.findAll().stream().filter(p -> p.getSupplier().getSupplierId().equals(supplierId))
				.map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class)).toList();
	}

	@Override
	public List<SupplierPaymentResponseDTO> getPaymentsByPurchaseOrderId(Long poId) {

		poRepo.findById(poId)
				.orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase Order is not found with id: " + poId));
		return paymentRepo.findAll().stream().filter(p -> p.getPurchaseOrder().getPurchaseOrderId().equals(poId))
				.map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class)).toList();
	}

	@Override
	public List<SupplierPaymentResponseDTO> getAllPayments() {
		List<SupplierPayment> payments = paymentRepo.findAll();

		if (payments.isEmpty()) {
			throw new com.superBoy.FoodieHub.ExceptionHandling.PaymentNotFoundException("No supplier payments found!");
		}

		return payments.stream().map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class)).toList();
	}
}
