package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
