import React, { useEffect, useState } from 'react';
import { supplierApi } from '../../api/supplierApi';
import type { Supplier, SupplierRequest } from '../../types';
import { Trash2, Edit, Plus, Truck, Mail, Phone, MapPin, ShieldAlert } from 'lucide-react';

const SupplierManager: React.FC = () => {
  const [suppliers, setSuppliers] = useState<Supplier[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingSupplier, setEditingSupplier] = useState<Supplier | null>(null);

  const [formData, setFormData] = useState({
    supplierName: '',
    contactPerson: 'Manager' as 'Manager' | 'assisantManager',
    email: '',
    phone: '',
    status: 'ACTIVE' as 'ACTIVE' | 'INACTIVE',
    street: '',
    city: '',
    state: '',
    zipCode: '',
    addressType: 'WAREHOUSE' as 'WAREHOUSE' | 'OFFICE' | 'OTHER',
  });

  const loadSuppliers = async () => {
    try {
      const data = await supplierApi.getAll();
      setSuppliers(data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadSuppliers();
  }, []);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();

    // Validation: zipCode must be exactly 6 digits (as required by backend regex)
    if (!/^[0-9]{6}$/.test(formData.zipCode)) {
      alert('Address Zip Code must be exactly 6 digits (numbers only) as per procurement regulations.');
      return;
    }

    // Validation: phone must be 10 digits
    if (!/^[0-9]{10}$/.test(formData.phone)) {
      alert('Phone number must be exactly 10 digits.');
      return;
    }

    const payload: SupplierRequest = {
      supplierName: formData.supplierName,
      contactPerson: formData.contactPerson,
      email: formData.email,
      phone: formData.phone,
      status: formData.status,
      addresses: {
        street: formData.street,
        city: formData.city,
        state: formData.state,
        zipCode: formData.zipCode,
        addressType: formData.addressType,
      },
    };

    try {
      if (editingSupplier) {
        await supplierApi.update(editingSupplier.supplierId, payload);
      } else {
        await supplierApi.create(payload);
      }
      setIsModalOpen(false);
      setEditingSupplier(null);
      loadSuppliers();
    } catch (err) {
      alert('Error saving supplier profile');
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to permanently delete this supplier profile? This action cannot be undone.')) {
      try {
        await supplierApi.delete(id);
        setSuppliers(suppliers.filter(s => s.supplierId !== id));
      } catch (err) {
        alert('Error deleting supplier');
      }
    }
  };

  return (
    <div className="flex flex-col gap-6 p-6 bg-white rounded-2xl shadow-sm border border-gray-100">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight flex items-center gap-2">
            <Truck className="text-orange-500" /> Supplier Directory
          </h1>
          <p className="text-gray-500 mt-1">Manage vendor profiles and supply chain nodes.</p>
        </div>
        <button
          onClick={() => {
            setEditingSupplier(null);
            setFormData({
              supplierName: '',
              contactPerson: 'Manager',
              email: '',
              phone: '',
              status: 'ACTIVE',
              street: '',
              city: '',
              state: '',
              zipCode: '',
              addressType: 'WAREHOUSE',
            });
            setIsModalOpen(true);
          }}
          className="bg-orange-500 hover:bg-orange-600 text-white font-semibold px-4 py-2.5 rounded-xl shadow-md transition-all duration-200 flex items-center gap-2"
        >
          <Plus size={20} /> Register Supplier
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {suppliers.length === 0 ? (
          <div className="col-span-full text-center py-12 text-gray-400">
            No suppliers registered yet. Click "Register Supplier" to add your first supplier.
          </div>
        ) : (
          suppliers.map(s => {
            const mainAddr = s.addresses?.[0];
            return (
              <div key={s.supplierId} className="bg-gray-50/50 hover:bg-gray-50 border border-gray-100 hover:border-orange-200 rounded-2xl p-5 flex flex-col justify-between transition-all duration-300 group shadow-sm hover:shadow-md">
                <div>
                  <div className="flex justify-between items-start">
                    <span className={`px-2.5 py-0.5 rounded-full text-xs font-semibold uppercase ${
                      s.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-gray-200 text-gray-600'
                    }`}>
                      {s.status}
                    </span>
                    <span className="text-xs text-gray-400 font-mono">ID: #{s.supplierId}</span>
                  </div>

                  <h3 className="text-lg font-bold text-gray-900 mt-2 group-hover:text-orange-600 transition-colors">
                    {s.supplierName}
                  </h3>
                  
                  <p className="text-xs text-gray-500 mt-1">
                    Contact: <span className="font-semibold text-gray-700">{s.contactPerson === 'assisantManager' ? 'Assistant Manager' : 'Manager'}</span>
                  </p>

                  <div className="mt-4 space-y-2 text-sm text-gray-600 border-t pt-4 border-gray-100">
                    <div className="flex items-center gap-2">
                      <Mail size={16} className="text-gray-400" />
                      <a href={`mailto:${s.email}`} className="hover:underline">{s.email}</a>
                    </div>
                    <div className="flex items-center gap-2">
                      <Phone size={16} className="text-gray-400" />
                      <span>{s.phone}</span>
                    </div>
                    {mainAddr && (
                      <div className="flex items-start gap-2 pt-1">
                        <MapPin size={16} className="text-gray-400 shrink-0 mt-0.5" />
                        <span className="text-xs leading-normal">
                          <span className="font-semibold uppercase text-[10px] bg-orange-100 text-orange-700 px-1.5 py-0.2 rounded mr-1">
                            {mainAddr.addressType}
                          </span>
                          {mainAddr.street}, {mainAddr.city}, {mainAddr.state} - {mainAddr.zipCode}
                        </span>
                      </div>
                    )}
                  </div>
                </div>

                <div className="flex gap-2.5 mt-6 border-t pt-4 border-gray-100 justify-end">
                  <button
                    onClick={() => {
                      setEditingSupplier(s);
                      setFormData({
                        supplierName: s.supplierName,
                        contactPerson: s.contactPerson,
                        email: s.email,
                        phone: s.phone,
                        status: s.status,
                        street: mainAddr?.street || '',
                        city: mainAddr?.city || '',
                        state: mainAddr?.state || '',
                        zipCode: mainAddr?.zipCode || '',
                        addressType: mainAddr?.addressType || 'WAREHOUSE',
                      });
                      setIsModalOpen(true);
                    }}
                    className="flex items-center gap-1.5 px-3 py-1.5 text-xs text-blue-600 hover:bg-blue-50 border border-blue-100 rounded-lg transition-colors font-medium"
                  >
                    <Edit size={14} /> Edit
                  </button>
                  <button
                    onClick={() => handleDelete(s.supplierId)}
                    className="flex items-center gap-1.5 px-3 py-1.5 text-xs text-red-600 hover:bg-red-50 border border-red-100 rounded-lg transition-colors font-medium"
                  >
                    <Trash2 size={14} /> Delete
                  </button>
                </div>
              </div>
            );
          })
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50 animate-fade-in">
          <div className="bg-white rounded-2xl shadow-xl border border-gray-100 max-w-lg w-full overflow-hidden">
            <div className="px-6 py-4 bg-gray-50 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-xl font-bold text-gray-900">
                {editingSupplier ? 'Modify Supplier Profile' : 'Register New Supplier'}
              </h2>
              <button onClick={() => setIsModalOpen(false)} className="text-gray-400 hover:text-gray-600 font-semibold">
                ✕
              </button>
            </div>
            <form onSubmit={handleSave} className="p-6 flex flex-col gap-4 overflow-y-auto max-h-[80vh]">
              <div className="grid grid-cols-2 gap-4">
                <div className="col-span-2">
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Company / Supplier Name</label>
                  <input
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                    value={formData.supplierName}
                    onChange={e => setFormData({ ...formData, supplierName: e.target.value })}
                    placeholder="e.g. Organic Produce Growers Ltd."
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Contact Person Title</label>
                  <select
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                    value={formData.contactPerson}
                    onChange={e => setFormData({ ...formData, contactPerson: e.target.value as any })}
                  >
                    <option value="Manager">Manager</option>
                    <option value="assisantManager">Assistant Manager</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Status</label>
                  <select
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                    value={formData.status}
                    onChange={e => setFormData({ ...formData, status: e.target.value as any })}
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Email Address</label>
                  <input
                    type="email"
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                    value={formData.email}
                    onChange={e => setFormData({ ...formData, email: e.target.value })}
                    placeholder="e.g. orders@company.com"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Phone Number (10 digits)</label>
                  <input
                    type="tel"
                    maxLength={10}
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none font-mono"
                    value={formData.phone}
                    onChange={e => setFormData({ ...formData, phone: e.target.value.replace(/[^0-9]/g, '') })}
                    placeholder="e.g. 9876543210"
                    required
                  />
                </div>
              </div>

              {/* Address details */}
              <div className="border-t border-gray-100 pt-4 mt-2">
                <h3 className="font-bold text-sm text-gray-900 mb-3 uppercase tracking-wide flex items-center gap-1.5">
                  <MapPin size={16} className="text-orange-500" /> Operational Address
                </h3>
                <div className="grid grid-cols-2 gap-4">
                  <div className="col-span-2">
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Street Address</label>
                    <input
                      className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                      value={formData.street}
                      onChange={e => setFormData({ ...formData, street: e.target.value })}
                      placeholder="Street name, industrial park, warehouse number"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">City</label>
                    <input
                      className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                      value={formData.city}
                      onChange={e => setFormData({ ...formData, city: e.target.value })}
                      placeholder="e.g. Mumbai"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">State</label>
                    <input
                      className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                      value={formData.state}
                      onChange={e => setFormData({ ...formData, state: e.target.value })}
                      placeholder="e.g. Maharashtra"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Postal Zip Code (6 digits)</label>
                    <input
                      type="text"
                      maxLength={6}
                      className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none font-mono"
                      value={formData.zipCode}
                      onChange={e => setFormData({ ...formData, zipCode: e.target.value.replace(/[^0-9]/g, '') })}
                      placeholder="e.g. 400001"
                      required
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-semibold text-gray-700 mb-1">Address Location Node</label>
                    <select
                      className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                      value={formData.addressType}
                      onChange={e => setFormData({ ...formData, addressType: e.target.value as any })}
                    >
                      <option value="WAREHOUSE">Warehouse Depot</option>
                      <option value="OFFICE">Corporate Office</option>
                      <option value="OTHER">Other Facility</option>
                    </select>
                  </div>
                </div>
              </div>

              <div className="flex justify-end gap-3 mt-6 border-t border-gray-100 pt-4">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-4 py-2 border border-gray-200 rounded-xl text-gray-500 font-semibold hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 bg-orange-500 text-white rounded-xl font-bold shadow-md hover:bg-orange-600 transition-colors"
                >
                  Save Supplier
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default SupplierManager;
