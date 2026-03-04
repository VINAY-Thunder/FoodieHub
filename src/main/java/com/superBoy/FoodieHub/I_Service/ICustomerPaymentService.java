package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;

public interface ICustomerPaymentService {
    CustomerPaymentResponseDTO processPayment(CustomerPaymentRequestDTO paymentDTO);
    CustomerPaymentResponseDTO getPaymentById(Long paymentId);
    List<CustomerPaymentResponseDTO> getPaymentsByCustomerId(Long customerId);
}
