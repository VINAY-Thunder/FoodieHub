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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.superBoy.FoodieHub.Enums.SupplierPaymentStatus;
import com.superBoy.FoodieHub.ExceptionHandling.PaymentVerificationException;
import com.superBoy.FoodieHub.ExceptionHandling.PurchaseOrderNotFoundException;
import com.superBoy.FoodieHub.I_Service.ISupplierPaymentService;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Model.SupplierPayment;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Repository.SupplierPaymentRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;

@Service
public class SupplierPaymentService implements ISupplierPaymentService {

    private final SupplierPaymentRepository paymentRepo;
    private final SupplierRepository supplierRepo;
    private final PurchaseOrderRepository poRepo;
    private final ModelMapper modelMapper;
    private final RazorpayClient razorpayClient;

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

    @Override
    public Map<String, String> createRazorPayOrder(SupplierPaymentRequestDTO dto) throws RazorpayException {
        int amountInPaise = dto.getAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValue();

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "receipt_po_" + dto.getPurchaseOrderId());

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
                .orElseThrow(() -> new com.superBoy.FoodieHub.ExceptionHandling.SupplierNotException("Supplier not found with ID: " + dto.getSupplierId()));
        
        PurchaseOrder po = poRepo.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

        if (paymentRepo.existsByPurchaseOrderPurchaseOrderIdAndPaymentStatus(dto.getPurchaseOrderId(), SupplierPaymentStatus.SUCCESS)) {
            throw new com.superBoy.FoodieHub.ExceptionHandling.DuplicatePaymentException("This Purchase Order has already been paid successfully! Duplicate payments are not allowed.");
        }

        boolean isValid = verifySignature(dto.getRazorpayOrderId(), dto.getRazorpayPaymentId(), dto.getRazorpaySignature());
        if (!isValid) {
            throw new PaymentVerificationException("Payment verification failed!");
        }

        SupplierPayment payment = modelMapper.map(dto, SupplierPayment.class);
        payment.setSupplier(supplier);
        payment.setPurchaseOrder(po);
        payment.setPaymentStatus(SupplierPaymentStatus.SUCCESS);
        payment.setTransactionId(dto.getRazorpayPaymentId());

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
        // Need custom method in repo or use stream filter (repo is better)
        return paymentRepo.findAll().stream()
                .filter(p -> p.getSupplier().getSupplierId().equals(supplierId))
                .map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class))
                .toList();
    }

    @Override
    public List<SupplierPaymentResponseDTO> getPaymentsByPurchaseOrderId(Long poId) {
        return paymentRepo.findAll().stream()
                .filter(p -> p.getPurchaseOrder().getPurchaseOrderId().equals(poId))
                .map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class))
                .toList();
    }
}
