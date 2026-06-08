export interface InventoryItem {
  inventoryId: number;
  itemName: string;
  currentStock: number;
  city: string;
  state: string;
  minStock: number;
  unit: string;
  lastUpdated: string;
}

export interface InventoryRequest {
  itemName: string;
  currentStock: number;
  city: string;
  state: string;
  minStock: number;
  unit: string;
  menuIds: number[];
}
