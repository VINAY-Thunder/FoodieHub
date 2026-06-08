package com.superBoy.FoodieHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.IcustomerService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "Customer registration and profile operations")
public class CustomerController {

	private IcustomerService customerService;

	@Autowired
	public CustomerController(IcustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@Operation(summary = "Register a new customer", description = "Creates a new customer account along with their default address.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Customer created successfully"),
		@ApiResponse(responseCode = "400", description = "Validation error in request body")
	})
	@PostMapping()
	public ResponseEntity<CustomerResponseDTO> registerCustomer(
			@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
		CustomerResponseDTO customerresDto = customerService.registerCustomer(customerRequestDTO);
		return new ResponseEntity<>(customerresDto, HttpStatus.CREATED);
	}

	@Operation(summary = "Get customer by ID", description = "Fetches a single customer profile by their unique ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Customer found"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long customerId)
			throws CustomerNotFoundException {
		CustomerResponseDTO customerresDto = customerService.fetchCustomerByID(customerId);
		return new ResponseEntity<>(customerresDto, HttpStatus.OK);
	}

	@Operation(summary = "Get all customers", description = "Returns a list of all registered customers.")
	@ApiResponse(responseCode = "200", description = "List of customers returned successfully")
	@GetMapping()
	public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() throws CustomerNotFoundException {
		List<CustomerResponseDTO> listcustomerresDto = customerService.fetchAllCustomers();
		return new ResponseEntity<>(listcustomerresDto, HttpStatus.OK);
	}

	@Operation(summary = "Full update of customer", description = "Completely replaces a customer profile (PUT semantics).")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Customer updated successfully"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@PutMapping("/{customerId}")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(
			@Valid @RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO,
			@PathVariable Long customerId) throws CustomerNotFoundException {
		CustomerResponseDTO customerresDto = customerService.updateCustomer(customerId, customerUpdateRequestDTO);
		return new ResponseEntity<>(customerresDto, HttpStatus.OK);
	}

	@Operation(summary = "Partial update of customer", description = "Partially updates a customer profile (PATCH semantics). Only provided fields are updated.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Customer partially updated"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@PatchMapping("/{customerId}")
	public ResponseEntity<CustomerResponseDTO> patchCustomer(@PathVariable Long customerId,
			@Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) throws CustomerNotFoundException {
		CustomerResponseDTO customerDto = customerService.updateCustomerById(customerId, customerUpdateDTO);
		return new ResponseEntity<>(customerDto, HttpStatus.OK);
	}

	@Operation(summary = "Delete customer", description = "Permanently removes a customer account by ID.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
		@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@DeleteMapping("/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {
		String customerDto = customerService.deleteCustomerById(customerId);
		return new ResponseEntity<>(customerDto, HttpStatus.OK);
	}
}
