import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { menuApi, categoryApi } from '../../api/menuApi';
import type { MenuItem, Category } from '../../types';
import { useCart } from '../../store/CartContext';
import { ShoppingCart, Plus, Minus, Search, Flame, Leaf, Utensils, Star, Trash2 } from 'lucide-react';

const MenuBrowser: React.FC = () => {
  const [items, setItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCat, setSelectedCat] = useState<number | null>(null);
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [vegOnly, setVegOnly] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);

  const { cart, addToCart, updateQuantity, removeFromCart, totalAmount } = useCart();
  const navigate = useNavigate();

  const loadData = async () => {
    setLoading(true);
    try {
      const [m, c] = await Promise.all([menuApi.getAvailable(), categoryApi.getAll()]);
      setItems(m || []);
      setCategories(c || []);
    } catch (error) {
      console.error("Failed to load menu data:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  // Filter items based on selected category, search query, and veg-only flag
  const filteredItems = items.filter(item => {
    const matchesCategory = selectedCat === null || item.categoryId === selectedCat;
    const matchesSearch = item.itemName.toLowerCase().includes(searchQuery.toLowerCase()) || 
                          item.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesVeg = !vegOnly || item.isVeg;
    return matchesCategory && matchesSearch && matchesVeg;
  });

  const getCartQuantity = (id: number) => {
    const item = cart.find(i => i.menuId === id);
    return item ? item.quantity : 0;
  };

  return (
    <div className="flex flex-col gap-8">
      {/* Hero Banner Section */}
      <div className="relative rounded-3xl overflow-hidden bg-gradient-to-r from-orange-600 to-amber-500 text-white p-8 md:p-12 shadow-lg">
        <div className="absolute right-0 bottom-0 opacity-10 pointer-events-none transform translate-y-6 translate-x-6">
          <Utensils size={320} />
        </div>
        <div className="max-w-2xl relative z-10 space-y-4">
          <span className="bg-white/20 backdrop-blur-md text-xs font-bold uppercase tracking-widest px-3 py-1 rounded-full flex items-center gap-1.5 w-fit">
            <Flame size={12} className="text-yellow-300 fill-yellow-300" /> Hot & Fresh
          </span>
          <h1 className="text-4xl md:text-5xl font-black tracking-tight leading-tight">
            Delicious food, delivered right to your doorstep
          </h1>
          <p className="text-orange-100 text-sm md:text-base leading-relaxed">
            Choose from a wide variety of gourmet dishes crafted by our expert chefs. Prepared with fresh, premium ingredients.
          </p>

          {/* Search and Filter Panel */}
          <div className="flex flex-col sm:flex-row gap-4 pt-4">
            <div className="relative flex-grow">
              <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 text-orange-600/70" size={18} />
              <input
                type="text"
                placeholder="Search for your favorite dishes..."
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-3 bg-white text-gray-800 rounded-2xl shadow-inner focus:outline-none focus:ring-2 focus:ring-orange-300 placeholder-gray-400 font-medium"
              />
            </div>
            <button
              onClick={() => setVegOnly(!vegOnly)}
              className={`px-5 py-3 rounded-2xl font-bold flex items-center justify-center gap-2 border transition-all duration-200 ${
                vegOnly 
                  ? 'bg-green-600 border-green-600 text-white shadow-md' 
                  : 'bg-white/10 border-white/20 text-white hover:bg-white/20'
              }`}
            >
              <Leaf size={16} className={vegOnly ? 'fill-white' : 'text-green-300'} />
              <span>Veg Only</span>
            </button>
          </div>
        </div>
      </div>

      {/* Category Horizontal Filter List */}
      <div>
        <h2 className="text-xl font-bold text-gray-900 mb-4 flex items-center gap-2">
          Explore Categories
        </h2>
        <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-none">
          <button
            onClick={() => setSelectedCat(null)}
            className={`px-6 py-2.5 rounded-full font-bold text-sm shrink-0 shadow-sm transition-all duration-200 ${
              selectedCat === null 
                ? 'bg-orange-500 text-white shadow-orange-500/20' 
                : 'bg-white text-gray-600 border border-gray-100 hover:bg-gray-50'
            }`}
          >
            All Items
          </button>
          {categories.map(cat => (
            <button
              key={cat.categoryId}
              onClick={() => setSelectedCat(cat.categoryId)}
              className={`px-6 py-2.5 rounded-full font-bold text-sm shrink-0 shadow-sm transition-all duration-200 ${
                selectedCat === cat.categoryId 
                  ? 'bg-orange-500 text-white shadow-orange-500/20' 
                  : 'bg-white text-gray-600 border border-gray-100 hover:bg-gray-50'
              }`}
            >
              {cat.categoryName}
            </button>
          ))}
        </div>
      </div>

      {/* Main Grid View: Menu Items on left, Cart summary on right */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        
        {/* Menu Items List */}
        <div className="lg:col-span-3">
          {loading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[...Array(6)].map((_, i) => (
                <div key={i} className="bg-white p-4 rounded-3xl shadow-sm border border-gray-100 animate-pulse space-y-4">
                  <div className="w-full h-48 bg-gray-200 rounded-2xl" />
                  <div className="h-6 bg-gray-200 rounded w-2/3" />
                  <div className="h-4 bg-gray-200 rounded w-full" />
                  <div className="flex justify-between items-center">
                    <div className="h-6 bg-gray-200 rounded w-1/4" />
                    <div className="h-8 w-8 bg-gray-200 rounded-full" />
                  </div>
                </div>
              ))}
            </div>
          ) : filteredItems.length === 0 ? (
            <div className="text-center py-20 bg-white rounded-3xl border border-dashed border-gray-200">
              <Utensils size={48} className="text-gray-300 mx-auto mb-3" />
              <h3 className="text-lg font-bold text-gray-700">No dishes match your filters</h3>
              <p className="text-gray-400 mt-1">Try clearing your search query or selecting a different category.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredItems.map(item => {
                const cartQty = getCartQuantity(item.menuId);
                const hasDiscount = item.discountPercent > 0;
                
                return (
                  <div 
                    key={item.menuId} 
                    className="bg-white rounded-3xl border border-gray-100 hover:border-orange-200 shadow-sm hover:shadow-lg transition-all duration-300 flex flex-col overflow-hidden group"
                  >
                    {/* Food Image Wrapper */}
                    <div className="relative overflow-hidden aspect-[4/3]">
                      <img 
                        src={item.imageUrl} 
                        alt={item.itemName} 
                        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" 
                      />
                      {/* Discount Pill */}
                      {hasDiscount && (
                        <span className="absolute left-3 top-3 bg-red-500 text-white text-[10px] font-black uppercase tracking-wider px-2 py-1 rounded-full shadow">
                          {item.discountPercent}% OFF
                        </span>
                      )}
                      {/* Veg / Non-Veg Indicator */}
                      <span className={`absolute right-3 top-3 p-1.5 rounded-full shadow backdrop-blur-md flex items-center justify-center ${
                        item.isVeg ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                      }`}>
                        <Leaf size={14} className={item.isVeg ? 'fill-green-600' : 'fill-red-600'} />
                      </span>
                    </div>

                    {/* Food Info */}
                    <div className="p-5 flex-grow flex flex-col justify-between">
                      <div>
                        <div className="flex justify-between items-start mb-1">
                          <h3 className="text-base font-bold text-gray-900 group-hover:text-orange-600 transition-colors">
                            {item.itemName}
                          </h3>
                          <div className="flex items-center gap-0.5 text-amber-500 font-bold text-xs shrink-0 bg-amber-50 px-1.5 py-0.5 rounded-md">
                            <Star size={12} className="fill-amber-500" />
                            <span>{(4.5 + (item.menuId % 6) * 0.1).toFixed(1)}</span>
                          </div>
                        </div>
                        <p className="text-gray-500 text-xs leading-normal line-clamp-2">
                          {item.description}
                        </p>
                      </div>

                      <div className="mt-4 pt-3 border-t border-gray-50 flex justify-between items-center">
                        <div className="flex flex-col">
                          {hasDiscount && (
                            <span className="text-[11px] text-gray-400 line-through">
                              ₹{item.price.toFixed(2)}
                            </span>
                          )}
                          <span className="text-lg font-extrabold text-orange-600">
                            ₹{(item.discountedPrice || item.price).toFixed(2)}
                          </span>
                        </div>

                        {/* Add to Cart Actions */}
                        {cartQty > 0 ? (
                          <div className="flex items-center bg-orange-500 text-white rounded-full p-1 shadow-sm font-bold text-xs border border-orange-600">
                            <button 
                              onClick={() => updateQuantity(item.menuId, cartQty - 1)}
                              className="w-7 h-7 flex items-center justify-center hover:bg-orange-600 rounded-full transition-colors"
                            >
                              <Minus size={14} />
                            </button>
                            <span className="w-8 text-center font-bold">{cartQty}</span>
                            <button 
                              onClick={() => updateQuantity(item.menuId, cartQty + 1)}
                              className="w-7 h-7 flex items-center justify-center hover:bg-orange-600 rounded-full transition-colors"
                            >
                              <Plus size={14} />
                            </button>
                          </div>
                        ) : (
                          <button
                            onClick={() => addToCart(item)}
                            className="bg-orange-500 hover:bg-orange-600 text-white font-bold p-2 px-3 rounded-2xl shadow-md transition-all duration-200 text-xs flex items-center gap-1.5"
                          >
                            <Plus size={14} /> Add to Cart
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        {/* Sidebar Shopping Cart */}
        <div className="lg:col-span-1">
          <div className="bg-white p-5 rounded-3xl border border-gray-100 shadow-sm h-fit sticky top-6">
            <h2 className="text-lg font-bold text-gray-900 border-b border-gray-100 pb-3 flex items-center gap-2">
              <ShoppingCart className="text-orange-500" /> Your Order
            </h2>
            
            <div className="flex flex-col gap-4 max-h-[50vh] overflow-y-auto mt-4 pr-1 divide-y divide-gray-50">
              {cart.length === 0 ? (
                <div className="text-center py-8 text-gray-400 flex flex-col items-center">
                  <ShoppingCart size={32} className="text-gray-200 mb-2" />
                  <p className="text-xs">Your basket is empty.</p>
                  <p className="text-[10px] text-gray-300 mt-1">Add items to configure checkout.</p>
                </div>
              ) : (
                cart.map(item => (
                  <div key={item.menuId} className="flex justify-between items-start pt-3 first:pt-0">
                    <div className="pr-2">
                      <p className="font-bold text-xs text-gray-900 line-clamp-1">{item.itemName}</p>
                      <p className="text-[10px] text-gray-400 mt-0.5">
                        ₹{(item.discountedPrice || item.price).toFixed(2)} each
                      </p>
                    </div>
                    <div className="flex flex-col items-end gap-1.5 shrink-0">
                      <span className="font-bold text-xs text-gray-800 font-mono">
                        ₹{((item.discountedPrice || item.price) * item.quantity).toFixed(2)}
                      </span>
                      
                      {/* Cart plus/minus modifiers */}
                      <div className="flex items-center bg-gray-100 text-gray-800 rounded-lg p-0.5 border text-[10px]">
                        <button 
                          onClick={() => updateQuantity(item.menuId, item.quantity - 1)}
                          className="w-5 h-5 flex items-center justify-center hover:bg-gray-200 rounded"
                        >
                          <Minus size={10} />
                        </button>
                        <span className="w-5 text-center font-bold">{item.quantity}</span>
                        <button 
                          onClick={() => updateQuantity(item.menuId, item.quantity + 1)}
                          className="w-5 h-5 flex items-center justify-center hover:bg-gray-200 rounded"
                        >
                          <Plus size={10} />
                        </button>
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>

            {cart.length > 0 && (
              <div className="mt-4 pt-4 border-t border-gray-100 flex flex-col gap-3">
                <div className="flex justify-between font-bold text-gray-900">
                  <span>Subtotal:</span>
                  <span className="font-mono text-orange-600">₹{totalAmount.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-xs text-gray-500">
                  <span>Delivery Charge:</span>
                  <span className="text-green-600 font-medium">FREE</span>
                </div>
                <button
                  onClick={() => navigate('/checkout')}
                  className="w-full bg-orange-500 hover:bg-orange-600 text-white py-3 rounded-2xl font-bold shadow-md hover:shadow-lg transition-all duration-200 flex items-center justify-center gap-2 mt-2 text-sm"
                >
                  Proceed to Checkout <ArrowRight size={16} />
                </button>
              </div>
            )}
          </div>
        </div>
        
      </div>
    </div>
  );
};

// Simple Arrow helper
const ArrowRight: React.FC<{ size?: number }> = ({ size = 16 }) => (
  <svg xmlns="http://www.w3.org/2000/svg" width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg>
);

export default MenuBrowser;
