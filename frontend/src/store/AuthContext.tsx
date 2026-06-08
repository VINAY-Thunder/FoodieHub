import React, { createContext, useContext, useState, useEffect } from 'react';

interface AuthUser {
  id: number;
  role: 'USER' | 'ADMIN';
  userName: string;
}

interface AuthContextType {
  user: AuthUser | null;
  login: (user: AuthUser) => void;
  logout: () => void;
  isCustomer: boolean;
  isAdmin: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<AuthUser | null>(() => {
    try {
      const stored = sessionStorage.getItem('foodiehub_user');
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  });

  const login = (userData: AuthUser) => {
    setUser(userData);
    sessionStorage.setItem('foodiehub_user', JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    sessionStorage.removeItem('foodiehub_user');
  };

  return (
    <AuthContext.Provider value={{
      user,
      login,
      logout,
      isCustomer: user?.role === 'USER',
      isAdmin: user?.role === 'ADMIN',
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};
