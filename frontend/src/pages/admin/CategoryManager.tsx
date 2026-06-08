import React, { useEffect, useState } from 'react';
import { categoryApi } from '../../api/menuApi';
import type { Category, CategoryStatus } from '../../types';
import { Trash2, Edit, Plus, FolderHeart, Image as ImageIcon, ToggleLeft, ToggleRight } from 'lucide-react';

const CategoryManager: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCat, setEditingCat] = useState<Category | null>(null);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [togglingId, setTogglingId] = useState<number | null>(null);

  const [formData, setFormData] = useState({
    categoryName: '',
    description: '',
    displayOrder: 1,
    categoryStatus: 'ACTIVE' as CategoryStatus,
  });

  const loadCategories = async () => {
    try {
      const data = await categoryApi.getAll();
      setCategories(data || []);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadCategories();
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
    formDataObj.append('categoryName', formData.categoryName);
    formDataObj.append('description', formData.description);
    formDataObj.append('displayOrder', String(formData.displayOrder));
    formDataObj.append('categoryStatus', formData.categoryStatus);

    if (imageFile) {
      formDataObj.append('imageFile', imageFile);
    }

    try {
      if (editingCat) {
        await categoryApi.update(editingCat.categoryId, formDataObj);
      } else {
        await categoryApi.create(formDataObj);
      }
      setIsModalOpen(false);
      setEditingCat(null);
      setImageFile(null);
      setImagePreview(null);
      loadCategories();
    } catch (err) {
      alert('Error saving category');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this category? All menu items under it might be affected.')) {
      try {
        await categoryApi.delete(id);
        setCategories(categories.filter(c => c.categoryId !== id));
      } catch (err) {
        alert('Error deleting category');
      }
    }
  };

  const handleToggleStatus = async (cat: Category) => {
    const newStatus: CategoryStatus = cat.categoryStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    setTogglingId(cat.categoryId);
    try {
      const updated = await categoryApi.updateStatus(cat.categoryId, newStatus);
      setCategories(prev => prev.map(c => c.categoryId === cat.categoryId ? updated : c));
    } catch (err) {
      alert('Failed to update status');
    } finally {
      setTogglingId(null);
    }
  };

  return (
    <div className="flex flex-col gap-6 p-6 bg-white rounded-2xl shadow-sm border border-gray-100">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight flex items-center gap-2">
            <FolderHeart className="text-orange-500" /> Category Management
          </h1>
          <p className="text-gray-500 mt-1">Organize and structure the restaurant's menu items.</p>
        </div>
        <button
          onClick={() => {
            setEditingCat(null);
            setFormData({
              categoryName: '',
              description: '',
              displayOrder: categories.length + 1,
              categoryStatus: 'ACTIVE',
            });
            setImageFile(null);
            setImagePreview(null);
            setIsModalOpen(true);
          }}
          className="bg-orange-500 hover:bg-orange-600 text-white font-semibold px-4 py-2.5 rounded-xl shadow-md transition-all duration-200 flex items-center gap-2"
        >
          <Plus size={20} /> Add Category
        </button>
      </div>

      <div className="overflow-x-auto rounded-xl border border-gray-200">
        <table className="w-full text-left border-collapse bg-white">
          <thead>
            <tr className="bg-gray-50 border-b border-gray-200 text-gray-700 font-semibold text-sm">
              <th className="p-4">Image</th>
              <th className="p-4">Category Name</th>
              <th className="p-4">Description</th>
              <th className="p-4 text-center">Order</th>
              <th className="p-4 text-center">Status</th>
              <th className="p-4">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100 text-gray-600">
            {categories.length === 0 ? (
              <tr>
                <td colSpan={6} className="text-center p-8 text-gray-400">
                  No categories found. Click "Add Category" to create one.
                </td>
              </tr>
            ) : (
              categories.map(cat => (
                <tr key={cat.categoryId} className="hover:bg-gray-50/50 transition-colors">
                  <td className="p-4">
                    {cat.imageUrl ? (
                      <img
                        src={cat.imageUrl}
                        alt={cat.categoryName}
                        className="w-16 h-12 object-cover rounded-lg border border-gray-200"
                        onError={(e) => {
                          (e.target as HTMLImageElement).style.display = 'none';
                        }}
                      />
                    ) : (
                      <div className="w-16 h-12 bg-gray-100 rounded-lg flex items-center justify-center text-gray-400 border border-dashed border-gray-300">
                        <ImageIcon size={20} />
                      </div>
                    )}
                  </td>
                  <td className="p-4 font-semibold text-gray-900">{cat.categoryName}</td>
                  <td className="p-4 max-w-xs truncate text-sm">{cat.description || 'No description'}</td>
                  <td className="p-4 text-center font-mono font-medium">{cat.displayOrder}</td>
                  <td className="p-4 text-center">
                    <button
                      onClick={() => handleToggleStatus(cat)}
                      disabled={togglingId === cat.categoryId}
                      title={`Click to set ${cat.categoryStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}`}
                      className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold transition-all duration-200 disabled:opacity-50 ${
                        cat.categoryStatus === 'ACTIVE'
                          ? 'bg-green-100 text-green-700 hover:bg-green-200'
                          : 'bg-red-100 text-red-700 hover:bg-red-200'
                      }`}
                    >
                      {cat.categoryStatus === 'ACTIVE'
                        ? <><ToggleRight size={14} /> ACTIVE</>
                        : <><ToggleLeft size={14} /> INACTIVE</>
                      }
                    </button>
                  </td>
                  <td className="p-4">
                    <div className="flex gap-2.5">
                      <button
                        onClick={() => {
                          setEditingCat(cat);
                          setFormData({
                            categoryName: cat.categoryName,
                            description: cat.description,
                            displayOrder: cat.displayOrder,
                            categoryStatus: cat.categoryStatus || 'ACTIVE',
                          });
                          setImagePreview(cat.imageUrl || null);
                          setImageFile(null);
                          setIsModalOpen(true);
                        }}
                        className="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                        title="Edit Category"
                      >
                        <Edit size={18} />
                      </button>
                      <button
                        onClick={() => handleDelete(cat.categoryId)}
                        className="p-1.5 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Delete Category"
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
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4 z-50 animate-fade-in">
          <div className="bg-white rounded-2xl shadow-xl border border-gray-100 max-w-md w-full overflow-hidden">
            <div className="px-6 py-4 bg-gray-50 border-b border-gray-100 flex justify-between items-center">
              <h2 className="text-xl font-bold text-gray-900">{editingCat ? 'Edit Category' : 'Add New Category'}</h2>
              <button
                onClick={() => { setIsModalOpen(false); setImagePreview(null); setImageFile(null); }}
                className="text-gray-400 hover:text-gray-600 font-semibold"
              >
                ✕
              </button>
            </div>
            <form onSubmit={handleSave} className="p-6 flex flex-col gap-4">
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Category Name *</label>
                <input
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none"
                  value={formData.categoryName}
                  onChange={e => setFormData({ ...formData, categoryName: e.target.value })}
                  placeholder="e.g. Italian Classics"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Description</label>
                <textarea
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none h-20 resize-none text-sm"
                  value={formData.description}
                  onChange={e => setFormData({ ...formData, description: e.target.value })}
                  placeholder="A short summary of items in this category"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Display Order</label>
                  <input
                    type="number"
                    min="1"
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none font-mono"
                    value={formData.displayOrder}
                    onChange={e => setFormData({ ...formData, displayOrder: parseInt(e.target.value) || 1 })}
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-gray-700 mb-1">Status</label>
                  <select
                    className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 focus:border-orange-500 outline-none bg-white"
                    value={formData.categoryStatus}
                    onChange={e => setFormData({ ...formData, categoryStatus: e.target.value as CategoryStatus })}
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-1">Category Image</label>
                {imagePreview && (
                  <div className="mb-2">
                    <img src={imagePreview} alt="Preview" className="w-full h-36 object-cover rounded-xl border border-gray-200" />
                  </div>
                )}
                <div className="flex items-center justify-center w-full">
                  <label className="flex flex-col items-center justify-center w-full h-24 border-2 border-gray-200 border-dashed rounded-xl cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors">
                    <div className="flex flex-col items-center justify-center text-gray-400">
                      <ImageIcon size={24} className="mb-1" />
                      <p className="text-sm font-semibold">
                        {imageFile ? imageFile.name : editingCat ? 'Click to replace image' : 'Click to upload image'}
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
                  className="px-5 py-2 bg-orange-500 text-white rounded-xl font-bold shadow-md hover:bg-orange-600 transition-colors disabled:opacity-60 flex items-center gap-2"
                >
                  {loading ? 'Saving...' : 'Save Category'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CategoryManager;
