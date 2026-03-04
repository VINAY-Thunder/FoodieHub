package com.superBoy.FoodieHub.Impl_Service;

import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.superBoy.FoodieHub.I_Service.ICustomerPaymentService;
import com.superBoy.FoodieHub.Model.CustomerPayment;
import com.superBoy.FoodieHub.Model.Customer;
import com.superBoy.FoodieHub.Model.Orders;
import com.superBoy.FoodieHub.Repository.CustomerPaymentRepository;
import com.superBoy.FoodieHub.Repository.CustomerRepository;
import com.superBoy.FoodieHub.Repository.OrderRepository;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;
import com.superBoy.FoodieHub.Enums.CustomerPaymentStatus;

@Service
public class CustomerPaymentService implements ICustomerPaymentService {

    private final CustomerPaymentRepository paymentRepo;
    private final CustomerRepository customerRepo;
    private final OrderRepository orderRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerPaymentService(CustomerPaymentRepository paymentRepo, CustomerRepository customerRepo, 
                                 OrderRepository orderRepo, ModelMapper modelMapper) {
        this.paymentRepo = paymentRepo;
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CustomerPaymentResponseDTO processPayment(CustomerPaymentRequestDTO dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + dto.getCustomerId()));
        
        Orders order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + dto.getOrderId()));

        CustomerPayment payment = modelMapper.map(dto, CustomerPayment.class);
        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setPaymentStatus(CustomerPaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        CustomerPayment saved = paymentRepo.save(payment);
        return modelMapper.map(saved, CustomerPaymentResponseDTO.class);
    }

    @Override
    public CustomerPaymentResponseDTO getPaymentById(Long id) {
        CustomerPayment payment = paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
        return modelMapper.map(payment, CustomerPaymentResponseDTO.class);
    }

    @Override
    public List<CustomerPaymentResponseDTO> getPaymentsByCustomerId(Long customerId) {
        return paymentRepo.findByCustomerCustomerId(customerId).stream()
                .map(p -> modelMapper.map(p, CustomerPaymentResponseDTO.class))
                .toList();
    }
}
