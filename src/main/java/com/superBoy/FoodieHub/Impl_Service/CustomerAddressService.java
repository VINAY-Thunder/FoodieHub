package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.ExceptionHandling.AddressNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.ExceptionHandling.UnauthorizedException;
import com.superBoy.FoodieHub.I_Service.ICustomerAddressService;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Repository.CustomerAddressRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Request.DTOs.CustomerAddressRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerAddressUpdateRequestDto;

@Service
public class CustomerAddressService implements ICustomerAddressService {

	private final CustomerAddressRepository addressRepo;
	private final CustomerRepository customerRepo;
	private final ModelMapper modelMapper;

	@Autowired
	public CustomerAddressService(CustomerAddressRepository addressRepo, CustomerRepository customerRepo,
			ModelMapper modelMapper) {
		this.addressRepo = addressRepo;
		this.customerRepo = customerRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public CustomerAddressResponseDTO addAddress(CustomerAddressRequestDTO dto) {
		Customer customer = customerRepo.findById(dto.getCustomerId())
				.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));

		CustomerAddress address = modelMapper.map(dto, CustomerAddress.class);
		address.setCustomer(customer);
		CustomerAddress saved = addressRepo.save(address);
		return modelMapper.map(saved, CustomerAddressResponseDTO.class);
	}

	public List<CustomerAddressResponseDTO> getAddressesByCustomerId(Long customerId) throws CustomerNotFoundException {

		// Step 1: Check customer exists first!
		customerRepo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

		// Step 2: Get addresses
		List<CustomerAddress> addresses = addressRepo.findByCustomerCustomerId(customerId);

		// Step 3: Check if addresses exist
		if (addresses.isEmpty()) {
			throw new AddressNotFoundException("No addresses found for customer id: " + customerId);
		}

		return addresses.stream().map(a -> modelMapper.map(a, CustomerAddressResponseDTO.class)).toList();
	}

	@Override
	public CustomerAddressResponseDTO updateCustomerAddress(Long id, CustomerAddressUpdateRequestDto addressRequestDTO)
			throws CustomerNotFoundException {

		// Step 1: Find address or throw
		CustomerAddress addressEntity = addressRepo.findById(id)
				.orElseThrow(() -> new AddressNotFoundException("CustomerAddress Not Found with id " + id));

		// Step 2: Security check — address belongs to this customer?
		if (!addressEntity.getCustomer().getCustomerId().equals(addressRequestDTO.getCustomerId())) {
			throw new UnauthorizedException("Address does not belong to this customer");
		}

		// Step 3: Update fields
		addressEntity.setStreet(addressRequestDTO.getStreet());
		addressEntity.setCity(addressRequestDTO.getCity());
		addressEntity.setState(addressRequestDTO.getState());
		addressEntity.setZipCode(addressRequestDTO.getZipCode());
		addressEntity.setAddressType(addressRequestDTO.getAddressType());

		// Step 4: Save and return ← capture returned entity!
		CustomerAddress updated = addressRepo.save(addressEntity);
		return modelMapper.map(updated, CustomerAddressResponseDTO.class);
	}

	@Override
	public CustomerAddressResponseDTO getAddressesByAddressId(Long addressId) {
		CustomerAddress address = addressRepo.findById(addressId)
				.orElseThrow(() -> new AddressNotFoundException("Address is not found with id: " + addressId));

		return modelMapper.map(address, CustomerAddressResponseDTO.class);
	}

	@Override
	public List<CustomerAddressResponseDTO> getAllAddresses() {

		List<CustomerAddress> addresses = addressRepo.findAll();

		if (addresses.isEmpty()) {
			throw new AddressNotFoundException("No addresses found!");
		}

		return addresses.stream().map(a -> modelMapper.map(a, CustomerAddressResponseDTO.class)).toList();
	}

	@Override
	public void deleteAddress(Long addressId) {
		if (!addressRepo.existsById(addressId)) {
			throw new RuntimeException("Address not found with ID: " + addressId);
		}
		addressRepo.deleteById(addressId);
	}
}
