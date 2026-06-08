import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Settings, LogIn, Eye, EyeOff, Loader2, Shield } from 'lucide-react';
import { authApi } from '../../api/authApi';
import { useAuth } from '../../store/AuthContext';

const AdminLogin: React.FC = () => {
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
      if (res.role !== 'ADMIN') {
        setError('This account is a Customer account. Please use Customer Login.');
        return;
      }
      login({ id: res.userId, role: res.role, userName: res.userName });
      navigate('/admin');
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Invalid credentials');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center p-4">
      {/* Decorative background */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-64 h-64 bg-orange-500/5 rounded-full blur-3xl" />
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-blue-500/5 rounded-full blur-3xl" />
      </div>

      <div className="w-full max-w-md relative z-10">
        <div className="text-center mb-8">
          <div className="inline-flex items-center gap-2 text-white">
            <div className="w-12 h-12 bg-orange-500 rounded-2xl flex items-center justify-center shadow-lg shadow-orange-500/30">
              <Shield size={24} className="text-white" />
            </div>
            <div className="text-left">
              <div className="text-2xl font-black">FoodieHub</div>
              <div className="text-orange-400 text-xs font-bold uppercase tracking-widest">Admin Portal</div>
            </div>
          </div>
          <p className="text-slate-400 mt-3 text-sm">Restricted access. Authorized personnel only.</p>
        </div>

        <div className="bg-slate-800/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-slate-700/50 p-8">
          <div className="flex items-center gap-3 mb-6">
            <div className="w-10 h-10 bg-orange-500/10 border border-orange-500/20 rounded-xl flex items-center justify-center">
              <Settings className="text-orange-400" size={18} />
            </div>
            <div>
              <h1 className="text-xl font-extrabold text-white">Admin Sign In</h1>
              <p className="text-xs text-slate-400">Access the management dashboard</p>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-900/30 border border-red-700/50 rounded-xl text-red-300 text-sm font-medium">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Username</label>
              <input
                id="admin-username"
                type="text"
                required
                autoComplete="username"
                className="w-full px-4 py-3 bg-slate-700/50 border border-slate-600 rounded-xl focus:ring-2 focus:ring-orange-500/30 focus:border-orange-500 outline-none text-sm text-white placeholder-slate-500 transition-all"
                placeholder="Admin username"
                value={form.userName}
                onChange={e => setForm({ ...form, userName: e.target.value })}
              />
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Password</label>
              <div className="relative">
                <input
                  id="admin-password"
                  type={showPass ? 'text' : 'password'}
                  required
                  autoComplete="current-password"
                  className="w-full px-4 py-3 pr-10 bg-slate-700/50 border border-slate-600 rounded-xl focus:ring-2 focus:ring-orange-500/30 focus:border-orange-500 outline-none text-sm text-white placeholder-slate-500 transition-all"
                  placeholder="Admin password"
                  value={form.password}
                  onChange={e => setForm({ ...form, password: e.target.value })}
                />
                <button
                  type="button"
                  onClick={() => setShowPass(!showPass)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-200"
                >
                  {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
            </div>

            <button
              id="admin-login-btn"
              type="submit"
              disabled={loading}
              className="w-full bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 rounded-xl shadow-lg shadow-orange-500/20 hover:shadow-orange-500/30 transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-60 mt-2"
            >
              {loading ? <Loader2 size={18} className="animate-spin" /> : <LogIn size={18} />}
              {loading ? 'Signing in...' : 'Sign In to Dashboard'}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-slate-700 text-center space-y-2">
            <p className="text-sm text-slate-400">
              Need an admin account?{' '}
              <Link to="/admin/register" className="text-orange-400 font-bold hover:text-orange-300">
                Register here
              </Link>
            </p>
            <p className="text-xs text-slate-500">
              Customer?{' '}
              <Link to="/login" className="text-slate-400 hover:text-white font-semibold">
                Customer Login →
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminLogin;
