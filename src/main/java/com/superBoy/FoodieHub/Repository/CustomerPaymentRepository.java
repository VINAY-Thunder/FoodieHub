package com.superBoy.FoodieHub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.CustomerPayment;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

	List<CustomerPayment> findByCustomerCustomerId(Long customerId);

}
