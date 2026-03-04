package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.I_Service.IProfileSummary;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.CustomerAddress;
import com.superBoy.FoodieHub.Model.CustomerPayment;
import com.superBoy.FoodieHub.Model.Orders;
import com.superBoy.FoodieHub.Repository.CustomerAddressRepository;
import com.superBoy.FoodieHub.Repository.CustomerPaymentRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Repository.OrderRepository;
import com.superBoy.FoodieHub.Response.DTOs.CustomerAddressSummaryDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentSummaryDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerProfileDTO;
import com.superBoy.FoodieHub.Response.DTOs.OrderSummaryDTO;

@Service
public class CustomerProfileSummary implements IProfileSummary {

	private CustomerRepository customerrepo;
	private CustomerAddressRepository addressrepo;
	private CustomerPaymentRepository customerpaymentrepo;
	private OrderRepository orderrepo;
	private ModelMapper modelMapper;

	@Autowired // constructor
	public CustomerProfileSummary(CustomerRepository customerrepo, CustomerAddressRepository addressrepo,
			CustomerPaymentRepository customerpaymentrepo, OrderRepository orderrepo, ModelMapper modelMapper) {
		super();
		this.customerrepo = customerrepo;
		this.addressrepo = addressrepo;
		this.customerpaymentrepo = customerpaymentrepo;
		this.orderrepo = orderrepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public CustomerProfileDTO displayCustomerProfile(Long customerId) throws CustomerNotFoundException {
		Customer customerDeatils = customerrepo.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

		// Map basic deatils
		CustomerProfileDTO profileDTO = modelMapper.map(customerDeatils, CustomerProfileDTO.class);

		// addressSummaryDTOs (Fetch Address)
		List<CustomerAddress> customeraddressesEnity = addressrepo.findByCustomerCustomerId(customerId);
		List<CustomerAddressSummaryDTO> addressSummary = customeraddressesEnity.stream()
				.map(address -> modelMapper.map(address, CustomerAddressSummaryDTO.class))
				.toList();
		profileDTO.setAddressSummaryDTOs(addressSummary);

		// OrderSummaryDTOs(Fetch Orders)
		List<Orders> orderEntity = orderrepo.findByCustomerCustomerId(customerId);
		List<OrderSummaryDTO> orderSummary = orderEntity.stream()
				.map(order -> modelMapper.map(order, OrderSummaryDTO.class))
				.toList();
		profileDTO.setOrderSummaryDTOs(orderSummary);
		
		// paymentSummaryDTOs (Fetch Payments)
		List<CustomerPayment> customerPayment = customerpaymentrepo.findByCustomerCustomerId(customerId);
		List<CustomerPaymentSummaryDTO> paymentSummaryDTO = customerPayment.stream()
				.map(payment -> modelMapper.map(payment, CustomerPaymentSummaryDTO.class))
				.toList();
		profileDTO.setCustomerPaymentSummaryDTOs(paymentSummaryDTO);

		return profileDTO;

	}

}
