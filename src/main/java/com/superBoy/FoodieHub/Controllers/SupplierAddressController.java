package com.superBoy.FoodieHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.I_Service.ISupplierAddressService;
import com.superBoy.FoodieHub.Request.DTOs.SupplierAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierAddressResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/supplier-addresses")
public class SupplierAddressController {

	private final ISupplierAddressService addressService;

	@Autowired
	public SupplierAddressController(ISupplierAddressService addressService) {
		this.addressService = addressService;
	}

	@PostMapping
	public ResponseEntity<SupplierAddressResponseDTO> addSupplierAddress(
			@RequestBody @Valid SupplierAddressRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addSupplierAddress(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SupplierAddressResponseDTO> updateSupplierAddress(@PathVariable Long addressId,
			@RequestBody @Valid SupplierAddressRequestDTO dto) {
		return ResponseEntity.ok(addressService.updateSupplierAddress(addressId, dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SupplierAddressResponseDTO> getSupplierAddressById(@PathVariable Long addressId) {
		return ResponseEntity.ok(addressService.getSupplierAddressById(addressId));
	}

	// GET ALL (ADMIN)
	@GetMapping
	public ResponseEntity<List<SupplierAddressResponseDTO>> getAllSuppliers() {

		return new ResponseEntity<List<SupplierAddressResponseDTO>>(addressService.getAllSupplierAddress(),
				HttpStatus.FOUND);
	}

	@GetMapping("/supplier/{supplierId}")
	public ResponseEntity<List<SupplierAddressResponseDTO>> getAddressesBySupplierId(@PathVariable Long supplierId) {
		return ResponseEntity.ok(addressService.getAddressesBySupplierId(supplierId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSupplierAddress(@PathVariable Long id) {
		addressService.deleteSupplierAddress(id);
		return ResponseEntity.ok("Supplier Address manually deleted successfully");
	}
}
