package com.superBoy.FoodieHub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.superBoy.FoodieHub.Model.SupplierPayment;
import java.util.List;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {

	List<SupplierPayment> findBySupplierSupplierId(Long supplierId);

	boolean existsByPurchaseOrderPurchaseOrderIdAndPaymentStatus(Long purchaseOrderId, com.superBoy.FoodieHub.Enums.SupplierPaymentStatus status);

}
