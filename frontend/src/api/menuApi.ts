import api from './axiosInstance';
import { MenuItem, Category } from '../types';

export const menuApi = {
  getAll: async () => {
    const { data } = await api.get<MenuItem[]>('/menu');
    return data ?? [];
  },
  getById: async (id: number) => {
    const { data } = await api.get<MenuItem>(`/menu/${id}`);
    return data;
  },
  getAvailable: async () => {
    const { data } = await api.get<MenuItem[]>('/menu/available');
    return data ?? [];
  },
  getByCategory: async (categoryId: number) => {
    const { data } = await api.get<MenuItem[]>(`/menu/category/${categoryId}`);
    return data ?? [];
  },
  create: async (formData: FormData) => {
    const { data } = await api.post<MenuItem>('/menu', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return data;
  },
  update: async (id: number, formData: FormData) => {
    const { data } = await api.put<MenuItem>(`/menu/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/menu/${id}`);
  },
  updateStatus: async (id: number, status: string) => {
    const { data } = await api.patch<MenuItem>(`/menu/${id}/status`, null, {
      params: { status },
    });
    return data;
  },
};

export const categoryApi = {
  getAll: async () => {
    const { data } = await api.get<Category[]>('/categories');
    return data ?? [];
  },
  create: async (formData: FormData) => {
    const { data } = await api.post<Category>('/categories', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return data;
  },
  update: async (id: number, formData: FormData) => {
    const { data } = await api.put<Category>(`/categories/${id}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return data;
  },
  updateStatus: async (id: number, status: 'ACTIVE' | 'INACTIVE') => {
    const { data } = await api.patch<Category>(`/categories/${id}/status`, null, {
      params: { status },
    });
    return data;
  },
  delete: async (id: number) => {
    await api.delete(`/categories/${id}`);
  },
};
