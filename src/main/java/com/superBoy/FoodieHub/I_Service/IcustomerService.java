package com.superBoy.FoodieHub.I_Service;

import java.util.List;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.Request.DTOs.CustomerRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateRequestDTO;

public interface IcustomerService {

	CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequestDTO);

	CustomerResponseDTO fetchCustomerByID(Long customerID) throws CustomerNotFoundException;

	List<CustomerResponseDTO> fetchAllCustomers() throws CustomerNotFoundException;

	// full update
	CustomerResponseDTO updateCustomer(Long customerID, CustomerUpdateRequestDTO customerUpdateRequestDTO) throws CustomerNotFoundException;

	//Partial update
	CustomerResponseDTO updateCustomerById(Long customerId,CustomerUpdateDTO customerUpdateDTO) throws CustomerNotFoundException;

	// this will be done by admin can remove
	String deleteCustomerById(Long customerID) throws CustomerNotFoundException;

}
