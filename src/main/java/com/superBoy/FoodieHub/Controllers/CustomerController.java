package com.superBoy.FoodieHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.IcustomerService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private IcustomerService customerService;

	@Autowired
	public CustomerController(IcustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@PostMapping()
//	@PostMapping("/registerCustomer")
	public ResponseEntity<CustomerResponseDTO> registerCustomer(
			@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {

		CustomerResponseDTO customerresDto = customerService.registerCustomer(customerRequestDTO);
		return new ResponseEntity<CustomerResponseDTO>(customerresDto, HttpStatus.CREATED);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<CustomerResponseDTO> registerCustomer(@PathVariable Long customerId)
			throws CustomerNotFoundException {

		CustomerResponseDTO customerresDto = customerService.fetchCustomerByID(customerId);
		return new ResponseEntity<CustomerResponseDTO>(customerresDto, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() throws CustomerNotFoundException {

		List<CustomerResponseDTO> listcustomerresDto = customerService.fetchAllCustomers();
		return new ResponseEntity<List<CustomerResponseDTO>>(listcustomerresDto, HttpStatus.OK);
	}

	@PutMapping("/{customerId}")
//	@PutMapping("/update/{customerId}")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO,
			@PathVariable Long customerId) throws CustomerNotFoundException {

		CustomerResponseDTO customerresDto = customerService.updateCustomer(customerId, customerRequestDTO);
		return new ResponseEntity<CustomerResponseDTO>(customerresDto, HttpStatus.OK);
	}

	@PatchMapping("/{customerId}")
	// @PatchMapping("/someupdate/{customerId}")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long customerId,
			@Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) throws CustomerNotFoundException {

		CustomerResponseDTO customerDto = customerService.updateCustomerById(customerId, customerUpdateDTO);
		return new ResponseEntity<CustomerResponseDTO>(customerDto, HttpStatus.OK);
	}

	@DeleteMapping("/{customerId}")
//	@DeleteMapping("/delete/{customerId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) throws CustomerNotFoundException {

		String customerDto = customerService.deleteCustomerById(customerId);
		return new ResponseEntity<String>(customerDto, HttpStatus.OK);
	}

}
