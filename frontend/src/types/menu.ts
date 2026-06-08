import { InventoryItem } from './inventory';

export type MenuStatus = 'AVAILABLE' | 'UNAVAILABLE' | 'OUT_OF_STOCK';
export type CategoryStatus = 'ACTIVE' | 'INACTIVE';

export interface Category {
  categoryId: number;
  categoryName: string;
  description: string;
  imageUrl: string;
  displayOrder: number;
  categoryStatus: CategoryStatus;
}

export interface MenuItem {
  menuId: number;
  categoryId: number;
  categoryName: string;
  itemName: string;
  description: string;
  price: number;
  menuStatus: MenuStatus;
  isVeg: boolean;
  imageUrl: string;
  discountPercent: number;
  discountedPrice: number;
  inventoryItems: InventoryItem[];
}

export interface MenuRequest {
  categoryId: number;
  itemName: string;
  description: string;
  price: number;
  discountPercent: number;
  isVeg: boolean;
  imageUrl: string;
  inventoryIds: number[];
  menuStatus: MenuStatus;
}
