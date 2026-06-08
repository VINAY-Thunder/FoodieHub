export type OrderStatus = 'PENDING' | 'IN_PROGRESS' | 'DELIVERED' | 'CANCELLED';

export type OrderType = 'DELIVERY' | 'DINE_IN' | 'TAKEAWAY';

export interface OrderItem {
  orderItemId: number;
  menuId: number;
  itemName: string;
  quantity: number;
  price: number;
}

export interface Order {
  orderId: number;
  customerId: number;
  customerName: string;
  customerAddress: any;
  orderDate: string;
  orderStatus: OrderStatus;
  totalAmount: number;
  deliveryDate?: string;
  cancellationReason?: string;
  orderItems: OrderItem[];
}

export interface OrderRequest {
  customerId: number;
  orderType: OrderType;
  customerAddressId?: number;
  tableNumber?: string;
  specialInstructions?: string;
  couponCode?: string;
  orderItems: {
    menuId: number;
    quantity: number;
  }[];
}
