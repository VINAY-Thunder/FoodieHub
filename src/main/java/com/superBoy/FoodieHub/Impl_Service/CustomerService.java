package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.IcustomerService;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Repository.CustomerAddressRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Request.DTOs.CustomerRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerResponseDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateDTO;
import com.superBoy.FoodieHub.Update.CustomerUpdateRequestDTO;

@Service
public class CustomerService implements IcustomerService {

	private CustomerRepository customerrepo;
	private CustomerAddressRepository addressrepo;
	private ModelMapper modelMapper;

	@Autowired
	public CustomerService(CustomerRepository customerrepo, ModelMapper modelMapper,
			CustomerAddressRepository addressrepo) {
		this.customerrepo = customerrepo;
		this.modelMapper = modelMapper;
		this.addressrepo = addressrepo;
	}

	@Override
	public CustomerResponseDTO registerCustomer(CustomerRequestDTO customerRequestDTO) {
		Customer customerEntity = modelMapper.map(customerRequestDTO, Customer.class);
		CustomerAddress CustomerAddressEntity;

		customerrepo.save(customerEntity);
		if (customerRequestDTO.getAddress() != null) {
			// setingRelationShip for Address and customer
			CustomerAddressEntity = modelMapper.map(customerRequestDTO.getAddress(), CustomerAddress.class);
			CustomerAddressEntity.setCustomer(customerEntity);
			customerEntity.getAddresses().add(CustomerAddressEntity);
			addressrepo.save(CustomerAddressEntity);
		}

		return modelMapper.map(customerEntity, CustomerResponseDTO.class);
	}

	@Override
	public CustomerResponseDTO fetchCustomerByID(Long customerID) throws CustomerNotFoundException {
		Optional<Customer> optional = customerrepo.findById(customerID);
		Customer CustomerEntity;
		if (optional.isPresent() == true) {
			CustomerEntity = optional.get();
			// 🔥 IMPORTANT LINE (force initialize lazy collection)
			CustomerEntity.getAddresses().size();
		} else {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerID);
		}
		return modelMapper.map(CustomerEntity, CustomerResponseDTO.class);
	}

	@Override
	public List<CustomerResponseDTO> fetchAllCustomers() {

		List<Customer> customers = customerrepo.findAll();

		return customers.stream().map(customer -> modelMapper.map(customer, CustomerResponseDTO.class)).toList();
	}

	@Override
	public CustomerResponseDTO updateCustomer(Long customerID, CustomerUpdateRequestDTO customerUpdateRequestDTO)
			throws CustomerNotFoundException {
		Customer customerEntity;
		Optional<Customer> optional = customerrepo.findById(customerID);
		if (optional.isPresent()) {
			customerEntity = optional.get();
			modelMapper.map(customerUpdateRequestDTO, customerEntity);
			customerrepo.save(customerEntity);
		} else {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerID);
		}
		return modelMapper.map(customerEntity, CustomerResponseDTO.class);
	}

	@Override
	public CustomerResponseDTO updateCustomerById(Long customerId, CustomerUpdateDTO customerUpdateDTO)
			throws CustomerNotFoundException {
		Customer customerEntity;
		Optional<Customer> optional = customerrepo.findById(customerId);
		if (optional.isPresent()) {
			customerEntity = optional.get();
			customerEntity.setPassword(customerUpdateDTO.getPassword());
			customerEntity.setPhone(customerUpdateDTO.getPhone());
			customerrepo.save(customerEntity);
		} else {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}
		return modelMapper.map(customerEntity, CustomerResponseDTO.class);
	}

	@Override
	public String deleteCustomerById(Long customerID) throws CustomerNotFoundException {
		Optional<Customer> optional = customerrepo.findById(customerID);
		if (optional.isPresent()) {
			customerrepo.deleteById(customerID);
			return "Succesfully Deleted Customer ID: " + customerID;
		}
		throw new CustomerNotFoundException("Customer not found with ID: " + customerID);
	}

}
