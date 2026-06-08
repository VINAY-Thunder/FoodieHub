package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ISupplierAddressService;
import com.superBoy.FoodieHub.Request.DTOs.SupplierAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierAddressResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/supplier-addresses")
@Tag(name = "Supplier Address", description = "Manage addresses for suppliers")
public class SupplierAddressController {

	private final ISupplierAddressService addressService;

	@Autowired
	public SupplierAddressController(ISupplierAddressService addressService) {
		this.addressService = addressService;
	}

	@Operation(summary = "Add supplier address", description = "Creates a new address and links it to the specified supplier.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Address created"),
		@ApiResponse(responseCode = "400", description = "Validation error")
	})
	@PostMapping
	public ResponseEntity<SupplierAddressResponseDTO> addSupplierAddress(
			@RequestBody @Valid SupplierAddressRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addSupplierAddress(dto));
	}

	@Operation(summary = "Update supplier address", description = "Fully replaces an existing supplier address by its ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address updated"),
		@ApiResponse(responseCode = "404", description = "Address not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<SupplierAddressResponseDTO> updateSupplierAddress(@PathVariable Long addressId,
			@RequestBody @Valid SupplierAddressRequestDTO dto) {
		return ResponseEntity.ok(addressService.updateSupplierAddress(addressId, dto));
	}

	@Operation(summary = "Get supplier address by ID", description = "Fetches a single supplier address record by its unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address found"),
		@ApiResponse(responseCode = "404", description = "Address not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<SupplierAddressResponseDTO> getSupplierAddressById(@PathVariable Long addressId) {
		return ResponseEntity.ok(addressService.getSupplierAddressById(addressId));
	}

	@Operation(summary = "Get all supplier addresses (Admin)", description = "Returns every supplier address in the system.")
	@ApiResponse(responseCode = "302", description = "All supplier addresses found")
	@GetMapping
	public ResponseEntity<List<SupplierAddressResponseDTO>> getAllSupplierAddresses() {
		return new ResponseEntity<>(addressService.getAllSupplierAddress(), HttpStatus.FOUND);
	}

	@Operation(summary = "Get addresses by supplier ID", description = "Returns all addresses linked to the specified supplier.")
	@ApiResponse(responseCode = "200", description = "Addresses returned")
	@GetMapping("/supplier/{supplierId}")
	public ResponseEntity<List<SupplierAddressResponseDTO>> getAddressesBySupplierId(@PathVariable Long supplierId) {
		return ResponseEntity.ok(addressService.getAddressesBySupplierId(supplierId));
	}

	@Operation(summary = "Delete supplier address", description = "Permanently removes a supplier address record by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address deleted"),
		@ApiResponse(responseCode = "404", description = "Address not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSupplierAddress(@PathVariable Long id) {
		addressService.deleteSupplierAddress(id);
		return ResponseEntity.ok("Supplier Address manually deleted successfully");
	}
}
