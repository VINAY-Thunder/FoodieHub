package com.superBoy.FoodieHub.Impl_Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.superBoy.FoodieHub.I_Service.ISupplierPaymentService;
import com.superBoy.FoodieHub.Model.SupplierPayment;
import com.superBoy.FoodieHub.Model.Supplier;
import com.superBoy.FoodieHub.Model.PurchaseOrder;
import com.superBoy.FoodieHub.Repository.SupplierPaymentRepository;
import com.superBoy.FoodieHub.Repository.SupplierRepository;
import com.superBoy.FoodieHub.Repository.PurchaseOrderRepository;
import com.superBoy.FoodieHub.Request.DTOs.SupplierPaymentRequestDTO;
import com.superBoy.FoodieHub.Response.DTOs.SupplierPaymentResponseDTO;
import com.superBoy.FoodieHub.Enums.SupplierPaymentStatus;

@Service
public class SupplierPaymentService implements ISupplierPaymentService {

    private final SupplierPaymentRepository paymentRepo;
    private final SupplierRepository supplierRepo;
    private final PurchaseOrderRepository poRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierPaymentService(SupplierPaymentRepository paymentRepo, SupplierRepository supplierRepo,
                                 PurchaseOrderRepository poRepo, ModelMapper modelMapper) {
        this.paymentRepo = paymentRepo;
        this.supplierRepo = supplierRepo;
        this.poRepo = poRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public SupplierPaymentResponseDTO processPayment(SupplierPaymentRequestDTO dto) {
        Supplier supplier = supplierRepo.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + dto.getSupplierId()));
        
        PurchaseOrder po = poRepo.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + dto.getPurchaseOrderId()));

        SupplierPayment payment = modelMapper.map(dto, SupplierPayment.class);
        payment.setSupplier(supplier);
        payment.setPurchaseOrder(po);
        payment.setPaymentStatus(SupplierPaymentStatus.SUCCESS); // Assuming immediate completion for now

        SupplierPayment saved = paymentRepo.save(payment);
        return modelMapper.map(saved, SupplierPaymentResponseDTO.class);
    }

    @Override
    public List<SupplierPaymentResponseDTO> getPaymentsBySupplierId(Long supplierId) {
        // Need custom method in repo or use stream filter (repo is better)
        return paymentRepo.findAll().stream()
                .filter(p -> p.getSupplier().getSupplierId().equals(supplierId))
                .map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class))
                .toList();
    }

    @Override
    public List<SupplierPaymentResponseDTO> getPaymentsByPurchaseOrderId(Long poId) {
        return paymentRepo.findAll().stream()
                .filter(p -> p.getPurchaseOrder().getPurchaseOrderId().equals(poId))
                .map(p -> modelMapper.map(p, SupplierPaymentResponseDTO.class))
                .toList();
    }
}
