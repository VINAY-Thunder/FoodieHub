import api from './axiosInstance';
import { Supplier, SupplierRequest } from '../types';

export const supplierApi = {
  getAll: async () => {
    const { data } = await api.get<Supplier[]>('/suppliers');
    return data ?? [];
  },
  getById: async (id: number) => {
    const { data } = await api.get<Supplier>(`/suppliers/${id}`);
    return data;
  },
  create: async (supplier: SupplierRequest) => {
    const { data } = await api.post<Supplier>('/suppliers', supplier);
    return data;
  },
  update: async (id: number, supplier: SupplierRequest) => {
    const { data } = await api.put<Supplier>(`/suppliers/${id}`, supplier);
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/suppliers/${id}`);
  },
};
