import api from './axiosInstance';
import { Customer, CustomerRequest, CustomerUpdateRequest } from '../types';

export const customerApi = {
  register: async (customer: CustomerRequest) => {
    const { data } = await api.post<Customer>('/api/customers', customer);
    return data;
  },
  getById: async (id: number) => {
    const { data } = await api.get<Customer>(`/api/customers/${id}`);
    return data;
  },
  getAll: async () => {
    const { data } = await api.get<Customer[]>('/api/customers');
    return data ?? [];
  },
  update: async (id: number, customer: CustomerUpdateRequest) => {
    const { data } = await api.put<Customer>(`/api/customers/${id}`, customer);
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/api/customers/${id}`);
  },
};
