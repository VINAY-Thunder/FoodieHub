import { MenuItem, Category, Order, InventoryItem, Customer } from '../types';
import { Supplier } from '../types/supplier';
import { PurchaseOrder } from '../types/purchaseOrder';

export const MOCK_CATEGORIES: Category[] = [
  {
    categoryId: 1,
    categoryName: 'Pizzas',
    description: 'Freshly baked stone-oven pizzas with premium toppings.',
    imageUrl: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=600&q=80',
    displayOrder: 1,
    categoryStatus: 'ACTIVE'
  },
  {
    categoryId: 2,
    categoryName: 'Burgers',
    description: 'Juicy, flame-grilled burgers with custom house sauces.',
    imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=600&q=80',
    displayOrder: 2,
    categoryStatus: 'ACTIVE'
  },
  {
    categoryId: 3,
    categoryName: 'Gourmet Mains',
    description: 'Exquisite, aromatic main course specialties.',
    imageUrl: 'https://images.unsplash.com/photo-1565557623262-b51c2513a641?auto=format&fit=crop&w=600&q=80',
    displayOrder: 3,
    categoryStatus: 'ACTIVE'
  },
  {
    categoryId: 4,
    categoryName: 'Sweet Cravings',
    description: 'Decadent desserts to satisfy your sweet tooth.',
    imageUrl: 'https://images.unsplash.com/photo-1563729784474-d77dbb933a9e?auto=format&fit=crop&w=600&q=80',
    displayOrder: 4,
    categoryStatus: 'ACTIVE'
  },
  {
    categoryId: 5,
    categoryName: 'Beverages',
    description: 'Refreshing cold brews, mocktails, and traditional lassis.',
    imageUrl: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=600&q=80',
    displayOrder: 5,
    categoryStatus: 'ACTIVE'
  }
];

export const MOCK_MENU_ITEMS: MenuItem[] = [
  {
    menuId: 101,
    categoryId: 1,
    categoryName: 'Pizzas',
    itemName: 'Signature Margherita',
    description: 'San Marzano tomatoes, fresh mozzarella, fresh basil, and extra virgin olive oil drizzle.',
    price: 12.99,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=600&q=80',
    discountPercent: 10,
    discountedPrice: 11.69,
    inventoryItems: []
  },
  {
    menuId: 102,
    categoryId: 1,
    categoryName: 'Pizzas',
    itemName: 'Spicy Pepperoni Diablo',
    description: 'Double portion of spicy pepperoni, fresh jalapeños, mozzarella, and hot honey drizzle.',
    price: 15.99,
    menuStatus: 'AVAILABLE',
    isVeg: false,
    imageUrl: 'https://images.unsplash.com/photo-1628840042765-356cda07504e?auto=format&fit=crop&w=600&q=80',
    discountPercent: 0,
    discountedPrice: 15.99,
    inventoryItems: []
  },
  {
    menuId: 103,
    categoryId: 2,
    categoryName: 'Burgers',
    itemName: 'Truffle Swiss Burger',
    description: 'Angus beef patty, caramelized onions, melted Swiss cheese, and luxurious black truffle aioli.',
    price: 14.49,
    menuStatus: 'AVAILABLE',
    isVeg: false,
    imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=600&q=80',
    discountPercent: 15,
    discountedPrice: 12.32,
    inventoryItems: []
  },
  {
    menuId: 104,
    categoryId: 2,
    categoryName: 'Burgers',
    itemName: 'Smoked Crispy Veggie Burger',
    description: 'Handcrafted quinoa & black bean patty, crispy onion rings, vegan cheddar, and house BBQ sauce.',
    price: 11.99,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1525059696034-4967a8e1dca2?auto=format&fit=crop&w=600&q=80',
    discountPercent: 5,
    discountedPrice: 11.39,
    inventoryItems: []
  },
  {
    menuId: 105,
    categoryId: 3,
    categoryName: 'Gourmet Mains',
    itemName: 'Royal Butter Chicken',
    description: 'Tender tandoori chicken cooked in a rich, creamy, tomato-butter gravy. Served with fresh butter naan.',
    price: 16.99,
    menuStatus: 'AVAILABLE',
    isVeg: false,
    imageUrl: 'https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?auto=format&fit=crop&w=600&q=80',
    discountPercent: 0,
    discountedPrice: 16.99,
    inventoryItems: []
  },
  {
    menuId: 106,
    categoryId: 3,
    categoryName: 'Gourmet Mains',
    itemName: 'Paneer Lababdar Feast',
    description: 'Cottage cheese cubes simmered in a luscious cashew-onion tomato gravy, garnished with fresh cream.',
    price: 15.49,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=600&q=80',
    discountPercent: 10,
    discountedPrice: 13.94,
    inventoryItems: []
  },
  {
    menuId: 107,
    categoryId: 4,
    categoryName: 'Sweet Cravings',
    itemName: 'Belgian Chocolate Lava Cake',
    description: 'Warm, chocolate cake with a molten center of premium Belgian dark chocolate. Served with vanilla bean ice cream.',
    price: 7.99,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=600&q=80',
    discountPercent: 0,
    discountedPrice: 7.99,
    inventoryItems: []
  },
  {
    menuId: 108,
    categoryId: 4,
    categoryName: 'Sweet Cravings',
    itemName: 'Warm Cinnamon Apple Waffles',
    description: 'Crisp, golden waffles topped with warm caramelized apples, cinnamon powder, and maple glaze.',
    price: 8.49,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?auto=format&fit=crop&w=600&q=80',
    discountPercent: 12,
    discountedPrice: 7.47,
    inventoryItems: []
  },
  {
    menuId: 109,
    categoryId: 5,
    categoryName: 'Beverages',
    itemName: 'Iced Rose Matcha Latte',
    description: 'Premium ceremonial grade matcha layered with organic rose milk syrup and cold foam.',
    price: 5.99,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1536256263959-770b48d82b0a?auto=format&fit=crop&w=600&q=80',
    discountPercent: 0,
    discountedPrice: 5.99,
    inventoryItems: []
  },
  {
    menuId: 110,
    categoryId: 5,
    categoryName: 'Beverages',
    itemName: 'Mango Mint Cooler',
    description: 'Fresh Alfonso mango pulp blended with fresh mint leaves, lime juice, and sparkling club soda.',
    price: 4.99,
    menuStatus: 'AVAILABLE',
    isVeg: true,
    imageUrl: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=600&q=80',
    discountPercent: 0,
    discountedPrice: 4.99,
    inventoryItems: []
  }
];

export const MOCK_INVENTORY: InventoryItem[] = [
  { inventoryId: 1, itemName: 'Mozzarella Cheese', currentStock: 45.5, minStock: 15.0, unit: 'kg', city: 'Mumbai', state: 'Maharashtra', lastUpdated: '2026-06-08' },
  { inventoryId: 2, itemName: 'Pizza Flour', currentStock: 80.0, minStock: 25.0, unit: 'kg', city: 'Delhi', state: 'Delhi', lastUpdated: '2026-06-08' },
  { inventoryId: 3, itemName: 'Angus Beef Patties', currentStock: 12, minStock: 20, unit: 'pcs', city: 'Bangalore', state: 'Karnataka', lastUpdated: '2026-06-08' },
  { inventoryId: 4, itemName: 'Chicken Breast', currentStock: 35.0, minStock: 10.0, unit: 'kg', city: 'Mumbai', state: 'Maharashtra', lastUpdated: '2026-06-08' },
  { inventoryId: 5, itemName: 'Fresh Paneer', currentStock: 8.0, minStock: 10.0, unit: 'kg', city: 'Mumbai', state: 'Maharashtra', lastUpdated: '2026-06-08' },
  { inventoryId: 6, itemName: 'Matcha Powder', currentStock: 2.5, minStock: 1.0, unit: 'kg', city: 'Tokyo', state: 'Japan Import', lastUpdated: '2026-06-08' }
];

export const MOCK_SUPPLIERS: Supplier[] = [
  {
    supplierId: 1,
    supplierName: 'Dairy Gold Farms',
    contactPerson: 'Manager',
    email: 'dairy.gold@farms.com',
    phone: '9876543210',
    status: 'ACTIVE',
    createdAt: '2026-05-10',
    addresses: [{ addressId: 11, street: '12 Dairy Road', city: 'Mumbai', state: 'Maharashtra', zipCode: '400001', addressType: 'WAREHOUSE' }]
  },
  {
    supplierId: 2,
    supplierName: 'Prime Choice Meats Ltd.',
    contactPerson: 'assisantManager',
    email: 'orders@primechoicemeats.com',
    phone: '8765432109',
    status: 'ACTIVE',
    createdAt: '2026-05-15',
    addresses: [{ addressId: 12, street: '44 Abbotoir Ave', city: 'Bangalore', state: 'Karnataka', zipCode: '560001', addressType: 'OFFICE' }]
  },
  {
    supplierId: 3,
    supplierName: 'Global Spice & Flour Co.',
    contactPerson: 'Manager',
    email: 'info@globalspiceflour.com',
    phone: '7654321098',
    status: 'ACTIVE',
    createdAt: '2026-05-20',
    addresses: [{ addressId: 13, street: '8 Spice Plaza', city: 'Delhi', state: 'Delhi', zipCode: '110001', addressType: 'OTHER' }]
  }
];

export const MOCK_PURCHASE_ORDERS: PurchaseOrder[] = [
  {
    purchaseOrderId: 501,
    supplierId: 1,
    supplierName: 'Dairy Gold Farms',
    orderDate: '2026-06-01T10:00:00',
    deliveryDate: '2026-06-10T18:00:00',
    status: 'PENDING',
    totalAmount: 180.50,
    purchaseItems: [
      { inventoryId: 1, itemName: 'Mozzarella Cheese', quantity: 20, unitPrice: 7.50 },
      { inventoryId: 5, itemName: 'Fresh Paneer', quantity: 5, unitPrice: 6.10 }
    ]
  },
  {
    purchaseOrderId: 502,
    supplierId: 2,
    supplierName: 'Prime Choice Meats Ltd.',
    orderDate: '2026-05-25T14:30:00',
    deliveryDate: '2026-05-28T12:00:00',
    status: 'RECEIVED',
    totalAmount: 320.00,
    purchaseItems: [
      { inventoryId: 3, itemName: 'Angus Beef Patties', quantity: 50, unitPrice: 6.40 }
    ]
  }
];

export const MOCK_ORDERS: Order[] = [
  {
    orderId: 1001,
    customerId: 1,
    customerName: 'Vinay Kumar',
    customerAddress: { street: '123 Food Street', city: 'Mumbai', state: 'Maharashtra', zipCode: '400001' },
    orderDate: '2026-06-07T20:15:00',
    orderStatus: 'DELIVERED',
    totalAmount: 23.38,
    orderItems: [
      { orderItemId: 1, menuId: 101, itemName: 'Signature Margherita', quantity: 1, price: 11.69 },
      { orderItemId: 2, menuId: 104, itemName: 'Smoked Crispy Veggie Burger', quantity: 1, price: 11.39 }
    ]
  },
  {
    orderId: 1002,
    customerId: 1,
    customerName: 'Vinay Kumar',
    customerAddress: { street: '123 Food Street', city: 'Mumbai', state: 'Maharashtra', zipCode: '400001' },
    orderDate: '2026-06-08T01:10:00',
    orderStatus: 'IN_PROGRESS',
    totalAmount: 20.98,
    orderItems: [
      { orderItemId: 3, menuId: 102, itemName: 'Spicy Pepperoni Diablo', quantity: 1, price: 15.99 },
      { orderItemId: 4, menuId: 110, itemName: 'Mango Mint Cooler', quantity: 1, price: 4.99 }
    ]
  }
];

export const MOCK_CUSTOMER: Customer = {
  customerId: 1,
  name: 'Vinay Kumar',
  email: 'vinay@foodiehub.com',
  phone: '9876543210',
  isActive: true,
  gender: 'MALE',
  dateOfBirth: '1995-05-15',
  addresses: [
    { addressId: 1, street: '123 Gourmet Heights, Bandra West', city: 'Mumbai', state: 'Maharashtra', zipCode: '400050' }
  ]
};
