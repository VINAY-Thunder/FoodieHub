import api from './axiosInstance';
import { PurchaseOrder, PurchaseOrderRequest } from '../types';

export const purchaseOrderApi = {
  getAll: async () => {
    const { data } = await api.get<PurchaseOrder[]>('/purchase-orders');
    return data ?? [];
  },
  getById: async (id: number) => {
    const { data } = await api.get<PurchaseOrder>(`/purchase-orders/${id}`);
    return data;
  },
  create: async (po: PurchaseOrderRequest) => {
    const { data } = await api.post<PurchaseOrder>('/purchase-orders', po);
    return data;
  },
  updateStatus: async (id: number, status: string) => {
    const { data } = await api.patch<PurchaseOrder>(`/purchase-orders/${id}/status`, null, {
      params: { status },
    });
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/purchase-orders/${id}`);
  },
};
