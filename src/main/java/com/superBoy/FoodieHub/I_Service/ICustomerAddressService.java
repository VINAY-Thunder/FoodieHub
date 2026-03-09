package com.superBoy.FoodieHub.I_Service;

import java.util.List;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerAddressUpdateRequestDto;

public interface ICustomerAddressService {
	CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO addressRequestDTO);

	List<CustomerAddressResponseDTO> getAddressesByCustomerId(Long customerId) throws CustomerNotFoundException;

	CustomerAddressResponseDTO updateCustomerAddress(Long id,CustomerAddressUpdateRequestDto addressRequestDTO) throws CustomerNotFoundException;

	CustomerAddressResponseDTO getAddressesByAddressId(Long addressId);
	void deleteAddress(Long addressId);

	List<CustomerAddressResponseDTO> getAllAddresses();
}
