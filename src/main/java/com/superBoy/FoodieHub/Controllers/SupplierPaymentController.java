package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ISupplierPaymentService;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/supplier-payments")
public class SupplierPaymentController {

    private final ISupplierPaymentService paymentService;

    @Autowired
    public SupplierPaymentController(ISupplierPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, String>> createRazorPayOrder(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createRazorPayOrder(dto));
    }

    @PostMapping("/verify")
    public ResponseEntity<SupplierPaymentResponseDTO> verifyAndSavePayment(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.verifyAndSavePayment(dto));
    }

    @PostMapping("/mark-failed")
    public ResponseEntity<SupplierPaymentResponseDTO> markPaymentAsFailed(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.markPaymentAsFailed(dto));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getPaymentsBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySupplierId(supplierId));
    }

    @GetMapping("/purchase-order/{poId}")
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getPaymentsByPurchaseOrderId(@PathVariable Long poId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPurchaseOrderId(poId));
    }

    @GetMapping
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
