export type SupplierStatus = 'ACTIVE' | 'INACTIVE';
export type ContactPerson = 'Manager' | 'assisantManager';
export type SupplierAddressType = 'WAREHOUSE' | 'OFFICE' | 'OTHER';

export interface SupplierAddress {
  addressId?: number;
  supplierId?: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
  addressType: SupplierAddressType;
}

export interface Supplier {
  supplierId: number;
  supplierName: string;
  contactPerson: ContactPerson;
  email: string;
  phone: string;
  status: SupplierStatus;
  createdAt?: string;
  addresses?: SupplierAddress[];
}

export interface SupplierRequest {
  supplierName: string;
  contactPerson: ContactPerson;
  email: string;
  phone: string;
  status: SupplierStatus;
  addresses?: SupplierAddress; // The backend takes a single address object in SupplierRequestDTO
}
