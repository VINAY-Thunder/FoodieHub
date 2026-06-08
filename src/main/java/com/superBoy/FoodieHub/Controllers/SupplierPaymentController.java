package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ISupplierPaymentService;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/supplier-payments")
@Tag(name = "Supplier Payment", description = "Razorpay-based supplier payment gateway")
public class SupplierPaymentController {

    private final ISupplierPaymentService paymentService;

    @Autowired
    public SupplierPaymentController(ISupplierPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create Razorpay order for supplier", description = "Initiates a Razorpay payment order for paying a supplier against a purchase order.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Razorpay order created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/create-order")
    public ResponseEntity<Map<String, String>> createRazorPayOrder(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createRazorPayOrder(dto));
    }

    @Operation(summary = "Verify and save supplier payment", description = "Validates Razorpay signature and persists the supplier payment record.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Payment verified and saved"),
        @ApiResponse(responseCode = "400", description = "Invalid signature or validation error")
    })
    @PostMapping("/verify")
    public ResponseEntity<SupplierPaymentResponseDTO> verifyAndSavePayment(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.verifyAndSavePayment(dto));
    }

    @Operation(summary = "Mark supplier payment as failed", description = "Records a failed payment attempt for audit and retry purposes.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Payment marked as failed"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/mark-failed")
    public ResponseEntity<SupplierPaymentResponseDTO> markPaymentAsFailed(@RequestBody @Valid SupplierPaymentRequestDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.markPaymentAsFailed(dto));
    }

    @Operation(summary = "Get payments by supplier", description = "Returns all payment records for a specific supplier.")
    @ApiResponse(responseCode = "200", description = "Payments returned")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getPaymentsBySupplierId(@PathVariable Long supplierId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySupplierId(supplierId));
    }

    @Operation(summary = "Get payments by purchase order", description = "Returns all payments linked to a specific purchase order.")
    @ApiResponse(responseCode = "200", description = "Payments returned")
    @GetMapping("/purchase-order/{poId}")
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getPaymentsByPurchaseOrderId(@PathVariable Long poId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPurchaseOrderId(poId));
    }

    @Operation(summary = "Get all supplier payments (Admin)", description = "Returns every supplier payment record. Admin-only endpoint.")
    @ApiResponse(responseCode = "200", description = "All supplier payments returned")
    @GetMapping
    public ResponseEntity<List<SupplierPaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
