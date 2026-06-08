import api from './axiosInstance';
import { Admin, AdminRequest, InventoryItem, InventoryRequest } from '../types';

export const adminApi = {
  getAll: async () => {
    const { data } = await api.get<Admin[]>('/admins');
    return data ?? [];
  },
  getById: async (id: number) => {
    const { data } = await api.get<Admin>(`/admins/${id}`);
    return data;
  },
  create: async (admin: AdminRequest) => {
    const { data } = await api.post<Admin>('/admins', admin);
    return data;
  },
  update: async (id: number, admin: AdminRequest) => {
    const { data } = await api.put<Admin>(`/admins/${id}`, admin);
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/admins/${id}`);
  },
};

export const inventoryApi = {
  getAll: async () => {
    const { data } = await api.get<InventoryItem[]>('/inventory');
    return data ?? [];
  },
  update: async (id: number, item: InventoryRequest) => {
    const { data } = await api.put<InventoryItem>(`/inventory/${id}`, item);
    return data;
  },
};
