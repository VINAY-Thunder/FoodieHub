package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.IPurchaseOrderService;
import com.superBoy.FoodieHub.Request.DTOs.PurchaseOrderRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.PurchaseOrderResponseDTO;
import com.superBoy.FoodieHub.Enums.PurchaseOrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/purchase-orders")
@Tag(name = "Purchase Order", description = "Procurement / purchase order management")
public class PurchaseOrderController {

    private final IPurchaseOrderService poService;

    @Autowired
    public PurchaseOrderController(IPurchaseOrderService poService) {
        this.poService = poService;
    }

    @Operation(summary = "Create purchase order", description = "Creates a new purchase order to procure items from a supplier.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Purchase order created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDTO> createPurchaseOrder(@RequestBody @Valid PurchaseOrderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poService.createPurchaseOrder(dto));
    }

    @Operation(summary = "Get purchase order by ID", description = "Fetches a single purchase order by its unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchase order found"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDTO> getPurchaseOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(poService.getPurchaseOrderById(id));
    }

    @Operation(summary = "Get all purchase orders", description = "Returns a list of all purchase orders in the system.")
    @ApiResponse(responseCode = "200", description = "All purchase orders returned")
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDTO>> getAllPurchaseOrders() {
        return ResponseEntity.ok(poService.getAllPurchaseOrders());
    }

    @Operation(summary = "Update purchase order status", description = "Changes the status of a purchase order (PENDING → RECEIVED / CANCELLED).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status updated"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PurchaseOrderResponseDTO> updateStatus(@PathVariable Long id, @RequestParam PurchaseOrderStatus status) {
        return ResponseEntity.ok(poService.updatePurchaseOrderStatus(id, status));
    }

    @Operation(summary = "Delete purchase order", description = "Permanently removes a purchase order by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Purchase order deleted"),
        @ApiResponse(responseCode = "404", description = "Purchase order not found")
    })
    @DeleteMapping("/{purchaseOrderId}")
    public ResponseEntity<String> removePurchaseOrderId(@PathVariable Long purchaseOrderId) {
        return new ResponseEntity<>(poService.removePurchaseOrderById(purchaseOrderId), HttpStatus.OK);
    }
}
