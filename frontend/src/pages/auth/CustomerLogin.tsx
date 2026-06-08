import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ChefHat, LogIn, Eye, EyeOff, Loader2 } from 'lucide-react';
import { authApi } from '../../api/authApi';
import { useAuth } from '../../store/AuthContext';

const CustomerLogin: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ userName: '', password: '' });
  const [showPass, setShowPass] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await authApi.loginUser({ userName: form.userName, password: form.password });
      if (res.role !== 'USER') {
        setError('This account is an Admin account. Please use Admin Login.');
        return;
      }
      login({ id: res.userId, role: res.role, userName: res.userName });
      navigate('/');
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-amber-50 to-red-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <Link to="/" className="inline-flex items-center gap-2 text-3xl font-black text-orange-600 hover:text-orange-700 transition-colors">
            <ChefHat size={36} className="text-orange-500" />
            FoodieHub
          </Link>
          <p className="text-gray-500 mt-2 text-sm">Welcome back! Sign in to your account.</p>
        </div>

        <div className="bg-white rounded-3xl shadow-xl border border-gray-100 p-8">
          <div className="flex items-center gap-2 mb-6">
            <div className="w-9 h-9 bg-orange-100 rounded-xl flex items-center justify-center">
              <LogIn className="text-orange-600" size={18} />
            </div>
            <div>
              <h1 className="text-xl font-extrabold text-gray-900">Customer Login</h1>
              <p className="text-xs text-gray-400">Sign in to browse & order food</p>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm font-medium">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-1">Username</label>
              <input
                id="customer-username"
                type="text"
                required
                autoComplete="username"
                className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none text-sm transition-all"
                placeholder="Enter your username"
                value={form.userName}
                onChange={e => setForm({ ...form, userName: e.target.value })}
              />
            </div>

            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-1">Password</label>
              <div className="relative">
                <input
                  id="customer-password"
                  type={showPass ? 'text' : 'password'}
                  required
                  autoComplete="current-password"
                  className="w-full px-4 py-3 pr-10 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none text-sm transition-all"
                  placeholder="Enter your password"
                  value={form.password}
                  onChange={e => setForm({ ...form, password: e.target.value })}
                />
                <button
                  type="button"
                  onClick={() => setShowPass(!showPass)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
            </div>

            <button
              id="customer-login-btn"
              type="submit"
              disabled={loading}
              className="w-full bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 rounded-xl shadow-md hover:shadow-lg transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-60"
            >
              {loading ? <Loader2 size={18} className="animate-spin" /> : <LogIn size={18} />}
              {loading ? 'Signing in...' : 'Sign In'}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-gray-100 text-center">
            <p className="text-sm text-gray-500">
              Don't have an account?{' '}
              <Link to="/register" className="text-orange-600 font-bold hover:text-orange-700">
                Register here
              </Link>
            </p>
            <p className="text-xs text-gray-400 mt-3">
              Are you an admin?{' '}
              <Link to="/admin/login" className="text-slate-600 font-semibold hover:text-slate-800">
                Admin Login →
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerLogin;
