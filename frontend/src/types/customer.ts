export type GenderIdentity = 'MALE' | 'FEMALE' | 'OTHER';


export interface CustomerAddress {
  addressId: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface Customer {
  customerId: number;
  name: string;
  email: string;
  phone: string;
  createdAt?: string;
  dateOfBirth?: string;
  isActive: boolean;
  gender: GenderIdentity;
  updatedAt?: string;
  addresses: CustomerAddress[];
}

export interface CustomerRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  gender: GenderIdentity;
  dateOfBirth: string;
  address: {
    street: string;
    city: string;
    state: string;
    zipCode: string;
    addressType: string;
  };
}

export interface CustomerUpdateRequest {
  name?: string;
  email?: string;
  phone?: string;
  gender?: GenderIdentity;
  dateOfBirth?: string;
}
