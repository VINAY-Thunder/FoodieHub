import React, { useEffect, useState } from 'react';
import { inventoryApi } from '../../api/adminApi';
import { InventoryItem, InventoryRequest } from '../../types';
import { AlertTriangle, Package } from 'lucide-react';

const InventoryManager: React.FC = () => {
  const [inventory, setInventory] = useState<InventoryItem[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [formData, setFormData] = useState<Partial<InventoryRequest>>({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadInventory = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await inventoryApi.getAll();
      setInventory(data);
    } catch (err) {
      console.error("Failed to load inventory:", err);
      setError("Failed to fetch inventory from the server. Please check if the backend is running and CORS is configured.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadInventory();
  }, []);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingId) return;
    try {
      await inventoryApi.update(editingId, formData as InventoryRequest);
      await loadInventory();
      setEditingId(null);
    } catch (err) {
      alert('Error updating inventory');
    }
  };

  return (
    <div className="flex flex-col gap-6">
      <h1 className="text-2xl font-bold">Inventory Management</h1>

      {loading && (
        <div className="bg-orange-50 text-orange-700 p-4 rounded-xl border border-orange-200">
          Loading inventory...
        </div>
      )}

      {error && (
        <div className="bg-red-50 text-red-700 p-4 rounded-xl border border-red-200">
          {error}
        </div>
      )}

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead className="bg-gray-50 border-b">
            <tr className="text-sm text-gray-600">
              <th className="p-4">Item</th>
              <th className="p-4">Stock</th>
              <th className="p-4">Unit</th>
              <th className="p-4">Min Stock</th>
              <th className="p-4">Action</th>
            </tr>
          </thead>
          <tbody>
            {inventory.map(item => (
              <tr key={item.inventoryId} className="border-b hover:bg-gray-50">
                <td className="p-4 font-medium">{item.itemName}</td>
                <td className={`p-4 ${item.currentStock <= item.minStock ? 'text-red-600 font-bold' : ''}`}>
                  {item.currentStock}
                </td>
                <td className="p-4">{item.unit}</td>
                <td className="p-4">{item.minStock}</td>
                <td className="p-4">
                  <button
                    onClick={() => {
                      setEditingId(item.inventoryId);
                      setFormData({
                        itemName: item.itemName,
                        currentStock: item.currentStock,
                        city: item.city,
                        state: item.state,
                        minStock: item.minStock,
                        unit: item.unit,
                        menuIds: [],
                      });
                    }}
                    className="text-blue-500 hover:text-blue-700"
                  >
                    Edit
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {editingId && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white p-6 rounded-lg max-w-md w-full">
            <h2 className="text-xl font-bold mb-4">Update Inventory</h2>
            <form onSubmit={handleSave} className="flex flex-col gap-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium">Stock</label>
                  <input
                    type="number"
                    className="w-full p-2 border rounded"
                    value={formData.currentStock}
                    onChange={e => setFormData({...formData, currentStock: parseInt(e.target.value)})}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium">Min Stock</label>
                  <input
                    type="number"
                    className="w-full p-2 border rounded"
                    value={formData.minStock}
                    onChange={e => setFormData({...formData, minStock: parseInt(e.target.value)})}
                  />
                </div>
              </div>
              <div className="flex justify-end gap-3 mt-4">
                <button type="button" onClick={() => setEditingId(null)} className="px-4 py-2 text-gray-500">Cancel</button>
                <button type="submit" className="px-4 py-2 bg-orange-500 text-white rounded font-bold">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default InventoryManager;
