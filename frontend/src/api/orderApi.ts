import api from './axiosInstance';
import { Order, OrderRequest, OrderStatus } from '../types';

export const orderApi = {
  create: async (order: OrderRequest) => {
    const { data } = await api.post<Order>('/orders', order);
    return data;
  },
  getById: async (id: number) => {
    const { data } = await api.get<Order>(`/orders/${id}`);
    return data;
  },
  getByCustomer: async (customerId: number) => {
    const { data } = await api.get<Order[]>(`/orders/customer/${customerId}`);
    return data ?? [];
  },
  getAll: async () => {
    const { data } = await api.get<Order[]>('/orders');
    return data ?? [];
  },
  updateStatus: async (id: number, status: OrderStatus) => {
    const { data } = await api.patch<Order>(`/orders/${id}/status`, null, {
      params: { status },
    });
    return data;
  },
  cancel: async (id: number, reason: string) => {
    const { data } = await api.post<Order>(`/orders/${id}/cancel`, null, {
      params: { reason },
    });
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/orders/${id}`);
  },
};
