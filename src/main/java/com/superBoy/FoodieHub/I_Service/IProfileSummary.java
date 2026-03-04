package com.superBoy.FoodieHub.I_Service;

import com.superBoy.FoodieHub.ExceptionHandling.CustomerNotFoundException;
import com.superBoy.FoodieHub.Response.DTOs.CustomerProfileDTO;

public interface IProfileSummary {

	CustomerProfileDTO displayCustomerProfile(Long CustomerId) throws CustomerNotFoundException;
}
