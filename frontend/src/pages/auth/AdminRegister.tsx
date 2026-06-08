import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Shield, UserPlus, Eye, EyeOff, Loader2 } from 'lucide-react';
import { authApi } from '../../api/authApi';
import { adminApi } from '../../api/adminApi';
import { useAuth } from '../../store/AuthContext';

const AdminRegister: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [showPass, setShowPass] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    userName: '',
    password: '',
    email: '',
    name: '',
    phone: '',
    role: 'ADMIN',
  });

  const f = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setForm(prev => ({ ...prev, [field]: e.target.value }));

  const inputCls = "w-full px-4 py-3 bg-slate-700/50 border border-slate-600 rounded-xl focus:ring-2 focus:ring-orange-500/30 focus:border-orange-500 outline-none text-sm text-white placeholder-slate-500 transition-all";

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      // 1. Create admin profile in DB
      await adminApi.create({
        name: form.name,
        email: form.email,
        phone: form.phone,
        role: form.role as any,
      });

      // 2. Register user account (auth)
      await authApi.registerAdminAccount({
        userName: form.userName,
        password: form.password,
        email: form.email,
      });

      // 3. Auto-login
      const loginRes = await authApi.loginUser({ userName: form.userName, password: form.password });
      login({ id: loginRes.userId, role: loginRes.role, userName: loginRes.userName });
      navigate('/admin');
    } catch (err: any) {
      const msg = err?.response?.data?.message
        || (typeof err?.response?.data === 'string' ? err.response.data : null)
        || err.message
        || 'Registration failed.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 flex items-center justify-center p-4">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/3 right-1/4 w-64 h-64 bg-orange-500/5 rounded-full blur-3xl" />
      </div>

      <div className="w-full max-w-md relative z-10">
        <div className="text-center mb-8">
          <div className="inline-flex items-center gap-2 text-white">
            <div className="w-12 h-12 bg-orange-500 rounded-2xl flex items-center justify-center shadow-lg shadow-orange-500/30">
              <Shield size={24} className="text-white" />
            </div>
            <div className="text-left">
              <div className="text-2xl font-black">FoodieHub</div>
              <div className="text-orange-400 text-xs font-bold uppercase tracking-widest">Admin Registration</div>
            </div>
          </div>
        </div>

        <div className="bg-slate-800/80 backdrop-blur-xl rounded-3xl shadow-2xl border border-slate-700/50 p-8">
          <div className="flex items-center gap-3 mb-6">
            <div className="w-10 h-10 bg-orange-500/10 border border-orange-500/20 rounded-xl flex items-center justify-center">
              <UserPlus className="text-orange-400" size={18} />
            </div>
            <div>
              <h1 className="text-xl font-extrabold text-white">Create Admin Account</h1>
              <p className="text-xs text-slate-400">Fill in the details to get admin access</p>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-900/30 border border-red-700/50 rounded-xl text-red-300 text-sm font-medium">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-semibold text-slate-300 mb-1">Full Name *</label>
                <input id="areg-name" type="text" required className={inputCls} placeholder="Admin Name" value={form.name} onChange={f('name')} />
              </div>
              <div>
                <label className="block text-sm font-semibold text-slate-300 mb-1">Username *</label>
                <input id="areg-username" type="text" required className={inputCls} placeholder="admin_vinay" value={form.userName} onChange={f('userName')} />
              </div>
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Email *</label>
              <input id="areg-email" type="email" required className={inputCls} placeholder="admin@company.com" value={form.email} onChange={f('email')} />
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Phone (10 digits) *</label>
              <input id="areg-phone" type="tel" required className={inputCls} placeholder="9876543210" value={form.phone} onChange={f('phone')} />
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Password *</label>
              <div className="relative">
                <input
                  id="areg-password"
                  type={showPass ? 'text' : 'password'}
                  required
                  className={`${inputCls} pr-10`}
                  placeholder="Secure password"
                  value={form.password}
                  onChange={f('password')}
                />
                <button type="button" onClick={() => setShowPass(!showPass)} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400">
                  {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
            </div>

            <div>
              <label className="block text-sm font-semibold text-slate-300 mb-1">Admin Role *</label>
              <select id="areg-role" className={inputCls} value={form.role} onChange={f('role')}>
                <option value="ADMIN">Admin</option>
                <option value="SUPER_ADMIN">Super Admin</option>
              </select>
            </div>

            <button
              id="areg-submit-btn"
              type="submit"
              disabled={loading}
              className="w-full bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 rounded-xl shadow-lg shadow-orange-500/20 transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-60 mt-2"
            >
              {loading ? <Loader2 size={16} className="animate-spin" /> : <UserPlus size={16} />}
              {loading ? 'Creating Account...' : 'Create Admin Account'}
            </button>
          </form>

          <div className="mt-6 pt-5 border-t border-slate-700 text-center">
            <p className="text-sm text-slate-400">
              Already have an admin account?{' '}
              <Link to="/admin/login" className="text-orange-400 font-bold hover:text-orange-300">
                Sign in
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminRegister;
