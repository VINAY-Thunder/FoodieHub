package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.ICustomerAddressService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerAddressUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer-addresses")
@Tag(name = "Customer Address", description = "Manage delivery addresses for customers")
public class CustomerAddressController {

	private final ICustomerAddressService addressService;

	@Autowired
	public CustomerAddressController(ICustomerAddressService addressService) {
		this.addressService = addressService;
	}

	@Operation(summary = "Add customer address", description = "Creates a new delivery address and associates it with the given customer.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Address created"),
		@ApiResponse(responseCode = "400", description = "Validation error")
	})
	@PostMapping
	public ResponseEntity<CustomerAddressResponseDTO> addAddress(@RequestBody @Valid CustomerAddressRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(dto));
	}

	@Operation(summary = "Get addresses by customer ID", description = "Returns all delivery addresses linked to the specified customer.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Addresses returned"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<CustomerAddressResponseDTO>> getAddressesByCustomerId(@PathVariable Long customerId)
			throws CustomerNotFoundException {
		return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
	}

	@Operation(summary = "Get address by ID", description = "Fetches a single customer address record by its unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address found"),
		@ApiResponse(responseCode = "404", description = "Address not found")
	})
	@GetMapping("{addressID}")
	public ResponseEntity<CustomerAddressResponseDTO> getAddressesByaddressID(@PathVariable Long addressID) {
		return ResponseEntity.ok(addressService.getAddressesByAddressId(addressID));
	}

	@Operation(summary = "Update customer address", description = "Replaces all fields of an existing customer address.")
	@ApiResponses({
		@ApiResponse(responseCode = "202", description = "Address updated"),
		@ApiResponse(responseCode = "404", description = "Address or Customer not found")
	})
	@PutMapping("{addressID}")
	public ResponseEntity<CustomerAddressResponseDTO> updateCustomerAddress(@PathVariable Long addressID,
			@RequestBody CustomerAddressUpdateRequestDto addressRequestDTO) throws CustomerNotFoundException {
		CustomerAddressResponseDTO addressResponseDTO = addressService.updateCustomerAddress(addressID, addressRequestDTO);
		return new ResponseEntity<>(addressResponseDTO, HttpStatus.ACCEPTED);
	}

	@Operation(summary = "Get all customer addresses (Admin)", description = "Returns every customer address in the system.")
	@ApiResponse(responseCode = "200", description = "All addresses returned")
	@GetMapping
	public ResponseEntity<List<CustomerAddressResponseDTO>> getAllAddresses() {
		return ResponseEntity.ok(addressService.getAllAddresses());
	}

	@Operation(summary = "Delete customer address", description = "Permanently removes a customer delivery address by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address deleted"),
		@ApiResponse(responseCode = "404", description = "Address not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
		addressService.deleteAddress(id);
		return ResponseEntity.ok("Address deleted successfully");
	}
}
