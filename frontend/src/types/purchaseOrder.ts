export type PurchaseOrderStatus = 'PENDING' | 'RECEIVED' | 'CANCELLED';

export interface PurchaseItem {
  purchaseItemId?: number;
  inventoryId: number;
  itemName?: string;
  quantity: number;
  unitPrice: number;
}

export interface PurchaseOrder {
  purchaseOrderId: number;
  supplierId: number;
  supplierName?: string;
  orderDate?: string;
  deliveryDate: string;
  status: PurchaseOrderStatus;
  totalAmount?: number;
  purchaseItems: PurchaseItem[];
}

export interface PurchaseOrderRequest {
  supplierId: number;
  deliveryDate: string; // ISO LocalDateTime string e.g. "2026-06-10T12:00:00"
  purchaseItems: {
    inventoryId: number;
    quantity: number;
    unitPrice: number;
  }[];
}
