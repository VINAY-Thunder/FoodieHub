import React, { useEffect, useState } from 'react';
import { purchaseOrderApi } from '../../api/purchaseOrderApi';
import { supplierApi } from '../../api/supplierApi';
import { inventoryApi } from '../../api/adminApi';
import type { PurchaseOrder, Supplier, InventoryItem } from '../../types';
import { Plus, ClipboardList, CheckCircle2, XCircle, Calendar, DollarSign, Loader2, ArrowRight } from 'lucide-react';

const PurchaseOrderManager: React.FC = () => {
  const [purchaseOrders, setPurchaseOrders] = useState<PurchaseOrder[]>([]);
  const [suppliers, setSuppliers] = useState<Supplier[]>([]);
  const [inventoryItems, setInventoryItems] = useState<InventoryItem[]>([]);
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  // New PO Form State
  const [supplierId, setSupplierId] = useState<number>(0);
  const [deliveryDate, setDeliveryDate] = useState<string>('');
  const [poItems, setPoItems] = useState<{ inventoryId: number; quantity: number; unitPrice: number }[]>([]);
  
  // Single Item Draft State
  const [draftItemId, setDraftItemId] = useState<number>(0);
  const [draftQty, setDraftQty] = useState<number>(1);
  const [draftPrice, setDraftPrice] = useState<number>(0.1);

  const loadData = async () => {
    setLoading(true);
    try {
      const [pos, sups, invs] = await Promise.all([
        purchaseOrderApi.getAll(),
        supplierApi.getAll(),
        inventoryApi.getAll()
      ]);
      setPurchaseOrders(pos || []);
      setSuppliers(sups || []);
      setInventoryItems(invs || []);
      
      if (sups && sups.length > 0) setSupplierId(sups[0].supplierId);
      if (invs && invs.length > 0) setDraftItemId(invs[0].inventoryId);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddItemToDraft = () => {
    if (draftItemId === 0) return;
    
    // Check if item already exists in draft list
    const existing = poItems.find(item => item.inventoryId === draftItemId);
    if (existing) {
      alert("This inventory item is already in the list. Adjust its quantity or delete it first.");
      return;
    }

    setPoItems([...poItems, { inventoryId: draftItemId, quantity: draftQty, unitPrice: draftPrice }]);
    // Reset drafts
    setDraftQty(1);
    setDraftPrice(0.1);
  };

  const handleRemoveDraftItem = (index: number) => {
    setPoItems(poItems.filter((_, i) => i !== index));
  };

  const handleCreatePO = async (e: React.FormEvent) => {
    e.preventDefault();

    if (poItems.length === 0) {
      alert("You must add at least one inventory item to the purchase order.");
      return;
    }

    if (!deliveryDate) {
      alert("Please specify a delivery date.");
      return;
    }

    const today = new Date();
    const selectedDate = new Date(deliveryDate);
    if (selectedDate <= today) {
      alert("Delivery date must be in the future.");
      return;
    }

    try {
      const payload = {
        supplierId,
        deliveryDate: new Date(deliveryDate).toISOString(), // Convert to LocalDateTime ISO string
        purchaseItems: poItems
      };
      await purchaseOrderApi.create(payload);
      setIsModalOpen(false);
      
      // Reset Form
      setPoItems([]);
      setDeliveryDate('');
      
      // Reload lists
      loadData();
    } catch (err) {
      alert("Failed to submit purchase order");
    }
  };

  const handleUpdateStatus = async (id: number, status: 'RECEIVED' | 'CANCELLED') => {
    if (confirm(`Are you sure you want to mark this purchase order as ${status}?`)) {
      try {
        await purchaseOrderApi.updateStatus(id, status);
        loadData();
      } catch (err) {
        alert("Failed to update status");
      }
    }
  };

  return (
    <div className="flex flex-col gap-6 p-6 bg-white rounded-2xl shadow-sm border border-gray-100">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight flex items-center gap-2">
            <ClipboardList className="text-orange-500" /> Purchase Orders
          </h1>
          <p className="text-gray-500 mt-1">Order ingredients and update warehouse inventory stock.</p>
        </div>
        <button
          onClick={() => {
            if (suppliers.length === 0) {
              alert("You must register a Supplier before placing a purchase order.");
              return;
            }
            if (inventoryItems.length === 0) {
              alert("You must register inventory stock items in the system first.");
              return;
            }
            setIsModalOpen(true);
          }}
          className="bg-orange-500 hover:bg-orange-600 text-white font-semibold px-4 py-2.5 rounded-xl shadow-md transition-all duration-200 flex items-center gap-2"
        >
          <Plus size={20} /> Create Purchase Order
        </button>
      </div>

      {loading ? (
        <div className="flex items-center justify-center p-12 text-gray-400 gap-2">
          <Loader2 className="animate-spin" /> Loading procurement data...
        </div>
      ) : (
        <div className="space-y-6">
          {purchaseOrders.length === 0 ? (
            <div className="text-center py-12 text-gray-400 border border-dashed rounded-2xl">
              No procurement records found. Placed orders will appear here.
            </div>
          ) : (
            <div className="overflow-x-auto rounded-xl border border-gray-200">
              <table className="w-full text-left border-collapse bg-white">
                <thead>
                  <tr className="bg-gray-50 border-b border-gray-200 text-gray-700 font-semibold text-sm">
                    <th className="p-4">PO ID</th>
                    <th className="p-4">Supplier</th>
                    <th className="p-4">Order & Delivery Dates</th>
                    <th className="p-4">Items Ordered</th>
                    <th className="p-4">Total Cost</th>
                    <th className="p-4">Status</th>
                    <th className="p-4">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100 text-gray-600 text-sm">
                  {purchaseOrders.map(po => (
                    <tr key={po.purchaseOrderId} className="hover:bg-gray-50/50 transition-colors">
                      <td className="p-4 font-mono font-bold text-gray-900">#{po.purchaseOrderId}</td>
                      <td className="p-4 font-medium text-gray-900">{po.supplierName}</td>
                      <td className="p-4 text-xs space-y-1">
                        <div>Ordered: <span className="font-semibold text-gray-700">{po.orderDate ? new Date(po.orderDate).toLocaleDateString() : 'N/A'}</span></div>
                        <div className="flex items-center gap-1 text-orange-600">
                          <Calendar size={12} /> Expected: <span className="font-semibold">{new Date(po.deliveryDate).toLocaleDateString()}</span>
                        </div>
                      </td>
                      <td className="p-4 max-w-xs">
                        <div className="flex flex-col gap-1 text-xs">
                          {po.purchaseItems?.map((item, idx) => (
                            <div key={idx} className="bg-gray-100 px-2 py-1 rounded text-gray-700">
                              {item.itemName} <span className="font-bold">x{item.quantity}</span> @ ₹{item.unitPrice}
                            </div>
                          ))}
                        </div>
                      </td>
                      <td className="p-4 font-bold text-gray-900 font-mono">₹{po.totalAmount?.toFixed(2)}</td>
                      <td className="p-4">
                        <span className={`px-2.5 py-0.5 rounded-full text-xs font-semibold uppercase ${
                          po.status === 'PENDING' ? 'bg-amber-100 text-amber-700' :
                          po.status === 'RECEIVED' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                        }`}>
                          {po.status}
                        </span>
                      </td>
                      <td className="p-4">
                        {po.status === 'PENDING' && (
                          <div className="flex gap-2">
                            <button
                              onClick={() => handleUpdateStatus(po.purchaseOrderId, 'RECEIVED')}
                              className="bg-green-600 hover:bg-green-700 text-white px-2.5 py-1.5 rounded-lg text-xs font-bold transition-colors flex items-center gap-1"
                            >
                              <CheckCircle2 size={14} /> Receive Stock
                            </button>
                            <button
                              onClick={() => handleUpdateStatus(po.purchaseOrderId, 'CANCELLED')}
                              className="border border-red-200 text-red-600 hover:bg-red-50 px-2.5 py-1.5 rounded-lg text-xs font-bold transition-colors flex items-center gap-1"
                            >
                              <XCircle size={14} /> Cancel
                            </button>
                          </div>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50 animate-fade-in">
          <div className="bg-white rounded-2xl shadow-xl border border-gray-100 max-w-2xl w-full overflow-hidden">
            <div className="px-6 py-4 bg-gray-50 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-xl font-bold text-gray-900">Procure Inventory stock</h2>
              <button onClick={() => setIsModalOpen(false)} className="text-gray-400 hover:text-gray-600 font-semibold">✕</button>
            </div>
            <form onSubmit={handleCreatePO} className="p-6 flex flex-col gap-4 overflow-y-auto max-h-[80vh]">
              
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Select Supplier</label>
                  <select
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white text-sm"
                    value={supplierId}
                    onChange={e => setSupplierId(parseInt(e.target.value))}
                  >
                    {suppliers.map(s => <option key={s.supplierId} value={s.supplierId}>{s.supplierName}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Delivery Target Date</label>
                  <input
                    type="date"
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none text-sm"
                    value={deliveryDate}
                    onChange={e => setDeliveryDate(e.target.value)}
                    required
                  />
                </div>
              </div>

              {/* Items draft adder */}
              <div className="bg-gray-50 p-4 rounded-xl border border-gray-100 mt-2">
                <h3 className="font-bold text-xs text-gray-700 uppercase tracking-wide mb-3">Add Order Items</h3>
                <div className="grid grid-cols-3 gap-3 items-end">
                  <div className="col-span-1">
                    <label className="block text-xs font-semibold text-gray-600 mb-1">Inventory Item</label>
                    <select
                      className="w-full p-2 border border-gray-200 rounded-lg outline-none bg-white text-xs"
                      value={draftItemId}
                      onChange={e => setDraftItemId(parseInt(e.target.value))}
                    >
                      {inventoryItems.map(item => (
                        <option key={item.inventoryId} value={item.inventoryId}>
                          {item.itemName} ({item.unit})
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs font-semibold text-gray-600 mb-1">Quantity</label>
                    <input
                      type="number"
                      min="1"
                      className="w-full p-2 border border-gray-200 rounded-lg outline-none text-xs"
                      value={draftQty}
                      onChange={e => setDraftQty(parseInt(e.target.value) || 1)}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-semibold text-gray-600 mb-1">Unit Price (₹)</label>
                    <input
                      type="number"
                      step="0.01"
                      min="0.1"
                      className="w-full p-2 border border-gray-200 rounded-lg outline-none text-xs"
                      value={draftPrice}
                      onChange={e => setDraftPrice(parseFloat(e.target.value) || 0.1)}
                    />
                  </div>
                  <div className="col-span-3 flex justify-end">
                    <button
                      type="button"
                      onClick={handleAddItemToDraft}
                      className="bg-orange-500 hover:bg-orange-600 text-white text-xs font-bold px-4 py-2 rounded-lg flex items-center gap-1.5 transition-colors"
                    >
                      Add to PO list <ArrowRight size={14} />
                    </button>
                  </div>
                </div>
              </div>

              {/* Items summary */}
              <div className="mt-2">
                <h3 className="font-bold text-sm text-gray-900 mb-2">Purchase List</h3>
                <div className="border border-gray-200 rounded-xl overflow-hidden text-xs">
                  <div className="bg-gray-50 p-2.5 font-bold text-gray-700 grid grid-cols-4 border-b">
                    <span>Item</span>
                    <span className="text-center">Quantity</span>
                    <span className="text-right">Unit Price</span>
                    <span className="text-right">Action</span>
                  </div>
                  <div className="divide-y divide-gray-100 max-h-[160px] overflow-y-auto">
                    {poItems.length === 0 ? (
                      <div className="p-4 text-center text-gray-400">No items added to purchase list yet.</div>
                    ) : (
                      poItems.map((item, idx) => {
                        const inv = inventoryItems.find(i => i.inventoryId === item.inventoryId);
                        return (
                          <div key={idx} className="p-2.5 grid grid-cols-4 items-center">
                            <span className="font-medium text-gray-900">{inv ? inv.itemName : `Item #${item.inventoryId}`}</span>
                            <span className="text-center font-mono">{item.quantity}</span>
                            <span className="text-right font-mono">₹{item.unitPrice.toFixed(2)}</span>
                            <span className="text-right">
                              <button
                                type="button"
                                onClick={() => handleRemoveDraftItem(idx)}
                                className="text-red-500 hover:underline"
                              >
                                Delete
                              </button>
                            </span>
                          </div>
                        );
                      })
                    )}
                  </div>
                  <div className="bg-orange-50/50 p-3 border-t font-bold text-sm text-gray-900 flex justify-between items-center">
                    <span>Total Purchase Cost:</span>
                    <span className="font-mono text-orange-600">
                      ₹{poItems.reduce((sum, item) => sum + (item.quantity * item.unitPrice), 0).toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>

              <div className="flex justify-end gap-3 mt-4 border-t border-gray-100 pt-4">
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
                  Place Order
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PurchaseOrderManager;
