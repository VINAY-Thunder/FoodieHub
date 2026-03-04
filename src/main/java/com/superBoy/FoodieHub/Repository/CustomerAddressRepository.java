package com.superBoy.FoodieHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

	// repository decides which table/entity you are querying.
// 👉 “From CustomerAddress table, give me addresses where customer.customerId=// ?”
	// We are querying the Address table, not the Customer table.

	List<CustomerAddress> findByCustomerCustomerId(Long customerId);
	
	
	
	
	
	
}
