export type AdminRole = 'SUPER_ADMIN' | 'ADMIN';


export interface Admin {
  adminId: number;
  name: string;
  email: string;
  phone: string;
  role: AdminRole;
  createdAt?: string;
}

export interface AdminRequest {
  name: string;
  email: string;
  phone: string;
  role: AdminRole;
}
