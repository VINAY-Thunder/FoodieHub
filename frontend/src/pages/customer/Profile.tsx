import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { customerApi } from '../../api/customerApi';
import { useAuth } from '../../store/AuthContext';
import type { Customer, CustomerUpdateRequest } from '../../types';
import { User, Mail, Phone, Calendar, Heart, ShieldCheck, Loader2 } from 'lucide-react';

const Profile: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [customer, setCustomer] = useState<Customer | null>(null);
  
  const [form, setForm] = useState<CustomerUpdateRequest>({
    name: '',
    email: '',
    phone: '',
    gender: 'MALE',
    dateOfBirth: '',
  });
  
  const [saving, setSaving] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!user) {
      navigate('/login');
      return;
    }
    customerApi.getById(user.id)
      .then(data => {
        setCustomer(data);
        if (data) {
          setForm({
            name: data.name || '',
            email: data.email || '',
            phone: data.phone || '',
            gender: data.gender || 'MALE',
            dateOfBirth: data.dateOfBirth || '',
          });
        }
      })
      .catch(err => {
        setError('Failed to load profile. Please try again.');
        console.error(err);
      })
      .finally(() => setLoading(false));
  }, [user, navigate]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    setSaving(true);
    try {
      const updated = await customerApi.update(user.id, form);
      setCustomer(updated);
      alert('Profile updated successfully!');
    } catch (err) {
      alert('Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[40vh] text-gray-400 gap-2">
        <Loader2 className="animate-spin" /> Loading profile details...
      </div>
    );
  }

  if (error) return <div className="text-center py-10 text-red-500">{error}</div>;

  if (!customer) return <div className="text-center py-10 text-gray-500">No profile data found. Please login again.</div>;

  return (
    <div className="max-w-4xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
      {/* Profile Overview Card */}
      <div className="md:col-span-1 space-y-6">
        <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm text-center">
          <div className="w-24 h-24 bg-gradient-to-tr from-orange-500 to-amber-500 rounded-full mx-auto flex items-center justify-center text-white text-3xl font-extrabold shadow-md mb-4">
            {customer.name ? customer.name.charAt(0) : 'U'}
          </div>
          <h2 className="text-xl font-bold text-gray-900">{customer.name}</h2>
          <p className="text-xs text-orange-600 font-bold uppercase tracking-wider mt-1">Valued Customer</p>
          <div className="mt-6 pt-6 border-t border-gray-50 text-left space-y-3.5 text-sm text-gray-600">
            <div className="flex items-center gap-3">
              <Mail size={16} className="text-gray-400 shrink-0" />
              <span className="truncate">{customer.email}</span>
            </div>
            <div className="flex items-center gap-3">
              <Phone size={16} className="text-gray-400 shrink-0" />
              <span>{customer.phone}</span>
            </div>
          </div>
        </div>

        <div className="bg-orange-50/50 border border-orange-100 p-5 rounded-3xl text-xs text-orange-800 leading-relaxed flex gap-3">
          <ShieldCheck size={20} className="text-orange-500 shrink-0 mt-0.5" />
          <div>
            <span className="font-bold text-orange-950">Security & Privacy</span>
            <p className="text-orange-900/80 mt-1">Your data is stored securely in our databases. We never share your phone number or email with delivery partners without encryption.</p>
          </div>
        </div>
      </div>

      {/* Profile Editing Form */}
      <div className="md:col-span-2">
        <div className="bg-white p-6 md:p-8 rounded-3xl border border-gray-100 shadow-sm">
          <h2 className="text-2xl font-bold text-gray-900 mb-6 flex items-center gap-2">
            <User className="text-orange-500" /> Account Settings
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Full Name</label>
                <input
                  type="text"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={form.name}
                  onChange={e => setForm({ ...form, name: e.target.value })}
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Email Address</label>
                <input
                  type="email"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={form.email}
                  onChange={e => setForm({ ...form, email: e.target.value })}
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Mobile Number</label>
                <input
                  type="tel"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm font-mono"
                  value={form.phone}
                  onChange={e => setForm({ ...form, phone: e.target.value })}
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Gender Identity</label>
                <select
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm bg-white"
                  value={form.gender}
                  onChange={e => setForm({ ...form, gender: e.target.value as any })}
                >
                  <option value="MALE">Male</option>
                  <option value="FEMALE">Female</option>
                  <option value="OTHER">Other</option>
                </select>
              </div>

              <div className="md:col-span-2">
                <label className="block text-xs font-semibold text-gray-600 mb-1">Date of Birth</label>
                <input
                  type="date"
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={form.dateOfBirth}
                  onChange={e => setForm({ ...form, dateOfBirth: e.target.value })}
                />
              </div>
            </div>

            {/* Addresses display */}
            <div className="border-t border-gray-50 pt-5 mt-6">
              <h3 className="font-bold text-sm text-gray-900 mb-3 flex items-center gap-1.5">
                <Heart size={16} className="text-orange-500" /> Saved Delivery Addresses
              </h3>
              <div className="space-y-2.5">
                {customer.addresses?.map(addr => (
                  <div key={addr.addressId} className="bg-gray-50 p-4 rounded-2xl border border-gray-100 flex items-center justify-between text-xs text-gray-700 leading-normal">
                    <span>{addr.street}, {addr.city}, {addr.state} - {addr.zipCode}</span>
                    <span className="font-bold uppercase text-[9px] bg-gray-200 text-gray-600 px-1.5 py-0.2 rounded">Primary</span>
                  </div>
                ))}
              </div>
            </div>

            <div className="pt-6 border-t border-gray-50 flex justify-end">
              <button
                type="submit"
                disabled={saving}
                className="bg-orange-500 hover:bg-orange-600 text-white font-bold px-6 py-2.5 rounded-xl shadow-md transition-all duration-200 text-sm flex items-center gap-2"
              >
                {saving && <Loader2 className="animate-spin" size={16} />}
                {saving ? 'Saving...' : 'Save Settings'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Profile;
