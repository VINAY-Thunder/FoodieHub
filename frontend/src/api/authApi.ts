import api from './axiosInstance';

export interface LoginRequest {
  userName: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  role: 'USER' | 'ADMIN';
  userName: string;
  message: string;
}

export interface RegisterUserRequest {
  userName: string;
  password: string;
  email: string;
}

export const authApi = {
  loginUser: async (req: LoginRequest): Promise<LoginResponse> => {
    const { data } = await api.post<LoginResponse>('/auth/login', req);
    if (!data.userId) throw new Error(data.message || 'Login failed');
    return data;
  },

  registerUserAccount: async (req: RegisterUserRequest): Promise<string> => {
    const { data } = await api.post<string>('/auth/user', req);
    return data;
  },

  registerAdminAccount: async (req: RegisterUserRequest): Promise<string> => {
    const { data } = await api.post<string>('/auth/admin', req);
    return data;
  },
};
