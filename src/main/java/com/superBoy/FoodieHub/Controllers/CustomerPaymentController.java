package com.superBoy.FoodieHub.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.superBoy.FoodieHub.I_Service.ICustomerPaymentService;
import com.superBoy.FoodieHub.Request.DTOs.CustomerPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.CustomerPaymentResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer-payments")
public class CustomerPaymentController {

    private final ICustomerPaymentService paymentService;

    @Autowired
    public CustomerPaymentController(ICustomerPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<CustomerPaymentResponseDTO> processPayment(@RequestBody @Valid CustomerPaymentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerPaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerPaymentResponseDTO>> getPaymentsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomerId(customerId));
    }
}
