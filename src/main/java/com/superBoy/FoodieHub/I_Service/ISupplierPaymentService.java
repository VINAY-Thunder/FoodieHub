package com.superBoy.FoodieHub.I_Service;

import java.util.List;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;

import java.util.Map;
import com.razorpay.RazorpayException;

public interface ISupplierPaymentService {
    Map<String, String> createRazorPayOrder(SupplierPaymentRequestDTO paymentDTO) throws RazorpayException;
    SupplierPaymentResponseDTO verifyAndSavePayment(SupplierPaymentRequestDTO paymentDTO) throws Exception;
    List<SupplierPaymentResponseDTO> getPaymentsBySupplierId(Long supplierId);
    List<SupplierPaymentResponseDTO> getPaymentsByPurchaseOrderId(Long poId);
}
