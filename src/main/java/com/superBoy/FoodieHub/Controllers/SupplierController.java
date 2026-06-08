package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ISupplierService;
import com.superBoy.FoodieHub.Request.DTOs.SupplierRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/suppliers")
@Tag(name = "Supplier", description = "Supplier profile management")
public class SupplierController {

    private final ISupplierService supplierService;

    @Autowired
    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(summary = "Add a new supplier", description = "Registers a new supplier in the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Supplier created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDTO> addSupplier(@RequestBody @Valid SupplierRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.addSupplier(dto));
    }

    @Operation(summary = "Get supplier by ID", description = "Fetches a supplier profile by their unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier found"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @Operation(summary = "Get all suppliers", description = "Returns a list of all registered suppliers.")
    @ApiResponse(responseCode = "200", description = "All suppliers returned")
    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @Operation(summary = "Update supplier", description = "Fully replaces a supplier's profile data by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier updated"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(@PathVariable Long id, @RequestBody @Valid SupplierRequestDTO dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, dto));
    }

    @Operation(summary = "Delete supplier", description = "Permanently removes a supplier profile by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier deleted"),
        @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
