package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;

public interface ISupplierPaymentService {
    SupplierPaymentResponseDTO processPayment(SupplierPaymentRequestDTO paymentDTO);
    List<SupplierPaymentResponseDTO> getPaymentsBySupplierId(Long supplierId);
    List<SupplierPaymentResponseDTO> getPaymentsByPurchaseOrderId(Long poId);
}
