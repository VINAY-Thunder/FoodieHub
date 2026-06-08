import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ChefHat, UserPlus, Eye, EyeOff, Loader2, MapPin } from 'lucide-react';
import { authApi } from '../../api/authApi';
import { customerApi } from '../../api/customerApi';
import { useAuth } from '../../store/AuthContext';

const CustomerRegister: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [showPass, setShowPass] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState<1 | 2>(1);

  const [form, setForm] = useState({
    // User account
    userName: '',
    password: '',
    email: '',
    // Customer profile
    name: '',
    phone: '',
    gender: 'MALE',
    dateOfBirth: '',
    // Address
    street: '',
    city: '',
    state: '',
    zipCode: '',
    addressType: 'HOME',
  });

  const f = (field: string) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setForm(prev => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      // 1. Register customer profile in DB
      await customerApi.register({
        name: form.name,
        email: form.email,
        phone: form.phone,
        password: form.password,
        gender: form.gender as any,
        dateOfBirth: form.dateOfBirth,
        address: {
          street: form.street,
          city: form.city,
          state: form.state,
          zipCode: form.zipCode,
          addressType: form.addressType as any,
        },
      });

      // 2. Register user account (auth)
      await authApi.registerUserAccount({
        userName: form.userName,
        password: form.password,
        email: form.email,
      });

      // 3. Login immediately
      const loginRes = await authApi.loginUser({ userName: form.userName, password: form.password });
      login({ id: loginRes.userId, role: loginRes.role, userName: loginRes.userName });
      navigate('/');
    } catch (err: any) {
      const msg = err?.response?.data?.message
        || (typeof err?.response?.data === 'string' ? err.response.data : null)
        || err.message
        || 'Registration failed. Please check your details.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const inputCls = "w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none text-sm transition-all";

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-amber-50 to-red-50 flex items-center justify-center p-4">
      <div className="w-full max-w-xl">
        <div className="text-center mb-8">
          <Link to="/" className="inline-flex items-center gap-2 text-3xl font-black text-orange-600 hover:text-orange-700 transition-colors">
            <ChefHat size={36} className="text-orange-500" />
            FoodieHub
          </Link>
          <p className="text-gray-500 mt-2 text-sm">Create your account and start ordering!</p>
        </div>

        <div className="bg-white rounded-3xl shadow-xl border border-gray-100 p-8">
          <div className="flex items-center gap-2 mb-6">
            <div className="w-9 h-9 bg-orange-100 rounded-xl flex items-center justify-center">
              <UserPlus className="text-orange-600" size={18} />
            </div>
            <div>
              <h1 className="text-xl font-extrabold text-gray-900">Customer Registration</h1>
              <p className="text-xs text-gray-400">Step {step} of 2 — {step === 1 ? 'Personal Details' : 'Delivery Address'}</p>
            </div>
          </div>

          {/* Step indicator */}
          <div className="flex gap-2 mb-6">
            <div className={`h-1.5 flex-1 rounded-full ${step >= 1 ? 'bg-orange-500' : 'bg-gray-200'}`} />
            <div className={`h-1.5 flex-1 rounded-full ${step >= 2 ? 'bg-orange-500' : 'bg-gray-200'}`} />
          </div>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm font-medium">
              {error}
            </div>
          )}

          <form onSubmit={step === 1 ? (e) => { e.preventDefault(); setStep(2); } : handleSubmit} className="space-y-4">
            {step === 1 && (
              <>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Full Name *</label>
                    <input id="reg-name" type="text" required className={inputCls} placeholder="Vinay Kumar" value={form.name} onChange={f('name')} />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Username *</label>
                    <input id="reg-username" type="text" required className={inputCls} placeholder="vinay123" value={form.userName} onChange={f('userName')} />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Email (Gmail) *</label>
                  <input id="reg-email" type="email" required className={inputCls} placeholder="vinay@gmail.com" value={form.email} onChange={f('email')} />
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Password *</label>
                  <div className="relative">
                    <input
                      id="reg-password"
                      type={showPass ? 'text' : 'password'}
                      required
                      className={`${inputCls} pr-10`}
                      placeholder="Min 8 chars, A-Z, 0-9, @$!%*?&"
                      value={form.password}
                      onChange={f('password')}
                    />
                    <button type="button" onClick={() => setShowPass(!showPass)} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400">
                      {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                    </button>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Phone (10 digits) *</label>
                    <input id="reg-phone" type="tel" required className={inputCls} placeholder="9876543210" value={form.phone} onChange={f('phone')} />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Date of Birth *</label>
                    <input id="reg-dob" type="date" required className={inputCls} value={form.dateOfBirth} onChange={f('dateOfBirth')} />
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Gender *</label>
                  <select id="reg-gender" className={inputCls} value={form.gender} onChange={f('gender')}>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>

                <button type="submit" className="w-full bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 rounded-xl shadow-md transition-all duration-200">
                  Next: Add Address →
                </button>
              </>
            )}

            {step === 2 && (
              <>
                <div className="flex items-center gap-2 mb-2">
                  <MapPin className="text-orange-500" size={18} />
                  <span className="font-bold text-gray-800 text-sm">Delivery Address</span>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Street / Flat No. *</label>
                  <input id="reg-street" type="text" required className={inputCls} placeholder="123 MG Road, Apt 4B" value={form.street} onChange={f('street')} />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">City *</label>
                    <input id="reg-city" type="text" required className={inputCls} placeholder="Mumbai" value={form.city} onChange={f('city')} />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">State *</label>
                    <input id="reg-state" type="text" required className={inputCls} placeholder="Maharashtra" value={form.state} onChange={f('state')} />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">ZIP Code (6 digits) *</label>
                    <input id="reg-zip" type="text" required className={inputCls} placeholder="400001" value={form.zipCode} onChange={f('zipCode')} />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Address Type *</label>
                    <select id="reg-addr-type" className={inputCls} value={form.addressType} onChange={f('addressType')}>
                      <option value="HOME">Home</option>
                      <option value="WORK">Work</option>
                      <option value="OTHER">Other</option>
                    </select>
                  </div>
                </div>

                <div className="flex gap-3">
                  <button
                    type="button"
                    onClick={() => setStep(1)}
                    className="flex-1 px-4 py-3 border border-gray-200 rounded-xl text-gray-600 font-semibold hover:bg-gray-50 transition-all text-sm"
                  >
                    ← Back
                  </button>
                  <button
                    type="submit"
                    disabled={loading}
                    id="reg-submit-btn"
                    className="flex-1 bg-orange-500 hover:bg-orange-600 text-white font-bold py-3 rounded-xl shadow-md transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-60"
                  >
                    {loading ? <Loader2 size={16} className="animate-spin" /> : <UserPlus size={16} />}
                    {loading ? 'Creating Account...' : 'Create Account'}
                  </button>
                </div>
              </>
            )}
          </form>

          <div className="mt-6 pt-5 border-t border-gray-100 text-center">
            <p className="text-sm text-gray-500">
              Already have an account?{' '}
              <Link to="/login" className="text-orange-600 font-bold hover:text-orange-700">
                Sign in
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomerRegister;
