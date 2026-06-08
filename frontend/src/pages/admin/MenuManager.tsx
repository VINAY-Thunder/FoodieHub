import React, { useEffect, useState } from 'react';
import { menuApi, categoryApi } from '../../api/menuApi';
import type { MenuItem, Category, MenuRequest, MenuStatus } from '../../types/menu';
import { Trash2, Edit, Plus, Image as ImageIcon, ToggleLeft, ToggleRight, UtensilsCrossed } from 'lucide-react';

const MenuManager: React.FC = () => {
  const [items, setItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<MenuItem | null>(null);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [togglingId, setTogglingId] = useState<number | null>(null);

  const [formData, setFormData] = useState<MenuRequest>({
    categoryId: 0,
    itemName: '',
    description: '',
    price: 0,
    discountPercent: 0,
    isVeg: true,
    imageUrl: '',
    inventoryIds: [],
    menuStatus: 'AVAILABLE',
  });

  const loadData = async () => {
    const [m, c] = await Promise.all([menuApi.getAll(), categoryApi.getAll()]);
    setItems(m);
    setCategories(c);
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    setImageFile(file);
    if (file) {
      setImagePreview(URL.createObjectURL(file));
    } else {
      setImagePreview(null);
    }
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    const formDataObj = new FormData();

    Object.entries(formData).forEach(([key, value]) => {
      if (key !== 'imageUrl' && key !== 'inventoryIds') {
        formDataObj.append(key, String(value));
      }
    });

    if (imageFile) {
      formDataObj.append('imageFile', imageFile);
    } else if (editingItem && formData.imageUrl) {
      formDataObj.append('imageUrl', formData.imageUrl);
    }

    try {
      if (editingItem) {
        await menuApi.update(editingItem.menuId, formDataObj);
      } else {
        await menuApi.create(formDataObj);
      }
      setIsModalOpen(false);
      setEditingItem(null);
      setImageFile(null);
      setImagePreview(null);
      const updated = await menuApi.getAll();
      setItems(updated);
    } catch (err) {
      alert('Error saving menu item');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this item?')) {
      try {
        await menuApi.delete(id);
        setItems(items.filter(i => i.menuId !== id));
      } catch (err) {
        alert('Failed to delete menu item');
      }
    }
  };

  const handleToggleStatus = async (item: MenuItem) => {
    const newStatus: MenuStatus = item.menuStatus === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE';
    setTogglingId(item.menuId);
    try {
      const updated = await menuApi.updateStatus(item.menuId, newStatus);
      setItems(prev => prev.map(i => i.menuId === item.menuId ? updated : i));
    } catch (err) {
      alert('Failed to update status');
    } finally {
      setTogglingId(null);
    }
  };

  const openEditModal = (item: MenuItem) => {
    setEditingItem(item);
    setFormData({
      categoryId: item.categoryId,
      itemName: item.itemName,
      description: item.description,
      price: item.price,
      discountPercent: item.discountPercent,
      isVeg: item.isVeg,
      imageUrl: item.imageUrl,
      inventoryIds: [],
      menuStatus: item.menuStatus,
    });
    setImagePreview(item.imageUrl || null);
    setImageFile(null);
    setIsModalOpen(true);
  };

  return (
    <div className="flex flex-col gap-6 p-6 bg-white rounded-2xl shadow-sm border border-gray-100">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight flex items-center gap-2">
            <UtensilsCrossed className="text-orange-500" /> Menu Management
          </h1>
          <p className="text-gray-500 mt-1">Add, update, and manage all restaurant menu items.</p>
        </div>
        <button
          onClick={() => {
            setEditingItem(null);
            setFormData({
              categoryId: categories[0]?.categoryId || 0,
              itemName: '',
              description: '',
              price: 0,
              discountPercent: 0,
              isVeg: true,
              imageUrl: '',
              inventoryIds: [],
              menuStatus: 'AVAILABLE',
            });
            setImageFile(null);
            setImagePreview(null);
            setIsModalOpen(true);
          }}
          className="bg-orange-500 hover:bg-orange-600 text-white font-semibold px-4 py-2.5 rounded-xl shadow-md transition-all duration-200 flex items-center gap-2"
        >
          <Plus size={20} /> Add Item
        </button>
      </div>

      <div className="overflow-x-auto rounded-xl border border-gray-200">
        <table className="w-full text-left border-collapse bg-white">
          <thead>
            <tr className="bg-gray-50 border-b border-gray-200 text-gray-700 font-semibold text-sm">
              <th className="p-4">Image</th>
              <th className="p-4">Item Name</th>
              <th className="p-4">Category</th>
              <th className="p-4">Price</th>
              <th className="p-4">Veg</th>
              <th className="p-4 text-center">Status</th>
              <th className="p-4">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 text-gray-600">
            {items.length === 0 ? (
              <tr>
                <td colSpan={7} className="text-center p-8 text-gray-400">
                  No menu items found. Click "Add Item" to create one.
                </td>
              </tr>
            ) : (
              items.map(item => (
                <tr key={item.menuId} className="hover:bg-gray-50/50 transition-colors">
                  <td className="p-4">
                    {item.imageUrl ? (
                      <img
                        src={item.imageUrl}
                        alt={item.itemName}
                        className="w-16 h-12 object-cover rounded-lg border border-gray-200"
                        onError={(e) => {
                          (e.target as HTMLImageElement).style.display = 'none';
                        }}
                      />
                    ) : (
                      <div className="w-16 h-12 bg-gray-100 rounded-lg flex items-center justify-center text-gray-400 border border-dashed border-gray-300">
                        <ImageIcon size={18} />
                      </div>
                    )}
                  </td>
                  <td className="p-4 font-semibold text-gray-900">{item.itemName}</td>
                  <td className="p-4 text-sm">{item.categoryName}</td>
                  <td className="p-4">
                    <div>
                      <span className="font-semibold">₹{item.discountedPrice ?? item.price}</span>
                      {item.discountPercent > 0 && (
                        <span className="text-xs text-gray-400 line-through ml-1.5">₹{item.price}</span>
                      )}
                    </div>
                  </td>
                  <td className="p-4">
                    <span className={`px-2 py-0.5 rounded text-xs font-bold ${item.isVeg ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                      {item.isVeg ? '🌿 Veg' : '🍖 Non-Veg'}
                    </span>
                  </td>
                  <td className="p-4 text-center">
                    <button
                      onClick={() => handleToggleStatus(item)}
                      disabled={togglingId === item.menuId}
                      title={`Click to set ${item.menuStatus === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE'}`}
                      className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold transition-all duration-200 disabled:opacity-50 ${
                        item.menuStatus === 'AVAILABLE'
                          ? 'bg-green-100 text-green-700 hover:bg-green-200'
                          : 'bg-red-100 text-red-700 hover:bg-red-200'
                      }`}
                    >
                      {item.menuStatus === 'AVAILABLE'
                        ? <><ToggleRight size={14} /> Available</>
                        : <><ToggleLeft size={14} /> Unavailable</>
                      }
                    </button>
                  </td>
                  <td className="p-4">
                    <div className="flex gap-2.5">
                      <button
                        onClick={() => openEditModal(item)}
                        className="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                        title="Edit Item"
                      >
                        <Edit size={18} />
                      </button>
                      <button
                        onClick={() => handleDelete(item.menuId)}
                        className="p-1.5 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Delete Item"
                      >
                        <Trash2 size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-2xl shadow-xl border border-gray-100 max-w-lg w-full overflow-y-auto max-h-[90vh]">
            <div className="px-6 py-4 bg-gray-50 border-b border-gray-100 flex justify-between items-center sticky top-0">
              <h2 className="text-xl font-bold text-gray-900">{editingItem ? 'Edit Menu Item' : 'Add New Menu Item'}</h2>
              <button
                onClick={() => { setIsModalOpen(false); setImagePreview(null); setImageFile(null); }}
                className="text-gray-400 hover:text-gray-600 font-semibold"
              >
                ✕
              </button>
            </div>
            <form onSubmit={handleSave} className="p-6 flex flex-col gap-4">
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Item Name *</label>
                <input
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                  value={formData.itemName}
                  onChange={e => setFormData({ ...formData, itemName: e.target.value })}
                  placeholder="e.g. Butter Chicken"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Category *</label>
                <select
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                  value={formData.categoryId}
                  onChange={e => setFormData({ ...formData, categoryId: parseInt(e.target.value) })}
                  required
                >
                  <option value={0} disabled>Select a category</option>
                  {categories.map(c => <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>)}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Price (₹) *</label>
                  <input
                    type="number"
                    min="0.01"
                    step="0.01"
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                    value={formData.price}
                    onChange={e => setFormData({ ...formData, price: parseFloat(e.target.value) })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Discount %</label>
                  <input
                    type="number"
                    min="0"
                    max="100"
                    step="0.01"
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                    value={formData.discountPercent}
                    onChange={e => setFormData({ ...formData, discountPercent: parseFloat(e.target.value) })}
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Description</label>
                <textarea
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none h-20 resize-none text-sm"
                  value={formData.description}
                  onChange={e => setFormData({ ...formData, description: e.target.value })}
                  placeholder="Short description of this item"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Status</label>
                  <select
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                    value={formData.menuStatus}
                    onChange={e => setFormData({ ...formData, menuStatus: e.target.value as MenuStatus })}
                  >
                    <option value="AVAILABLE">Available</option>
                    <option value="UNAVAILABLE">Unavailable</option>
                  </select>
                </div>
                <div className="flex items-end pb-1">
                  <label className="flex items-center gap-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={formData.isVeg}
                      onChange={e => setFormData({ ...formData, isVeg: e.target.checked })}
                      className="w-4 h-4 accent-green-600"
                    />
                    <span className="text-sm font-semibold text-gray-700">Vegetarian</span>
                  </label>
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Item Image</label>
                {imagePreview && (
                  <div className="mb-2">
                    <img src={imagePreview} alt="Preview" className="w-full h-36 object-cover rounded-xl border border-gray-200" />
                  </div>
                )}
                <label className="flex flex-col items-center justify-center w-full h-24 border-2 border-gray-200 border-dashed rounded-xl cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors">
                  <div className="flex flex-col items-center justify-center text-gray-400">
                    <ImageIcon size={24} className="mb-1" />
                    <p className="text-sm font-semibold">
                      {imageFile ? imageFile.name : editingItem ? 'Click to replace image' : 'Click to upload image'}
                    </p>
                    <p className="text-xs mt-0.5">PNG, JPG or WEBP (Max 5MB)</p>
                  </div>
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleImageChange}
                    className="hidden"
                  />
                </label>
              </div>

              <div className="flex justify-end gap-3 mt-2">
                <button
                  type="button"
                  onClick={() => { setIsModalOpen(false); setImagePreview(null); setImageFile(null); }}
                  className="px-4 py-2 border border-gray-200 rounded-xl text-gray-500 font-semibold hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={loading}
                  className="px-5 py-2 bg-orange-500 text-white rounded-xl font-bold shadow-md hover:bg-orange-600 transition-colors disabled:opacity-60"
                >
                  {loading ? 'Saving...' : 'Save Item'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default MenuManager;
