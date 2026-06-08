import React from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate, useNavigate } from 'react-router-dom';
import { CartProvider } from './store/CartContext';
import { AuthProvider, useAuth } from './store/AuthContext';
import ErrorBoundary from './components/common/ErrorBoundary';

// Customer pages
import MenuBrowser from './pages/customer/MenuBrowser';
import OrderHistory from './pages/customer/OrderHistory';
import Profile from './pages/customer/Profile';
import Checkout from './pages/customer/Checkout';

// Admin pages
import MenuManager from './pages/admin/MenuManager';
import OrderManager from './pages/admin/OrderManager';
import InventoryManager from './pages/admin/InventoryManager';
import Dashboard from './pages/admin/Dashboard';
import CategoryManager from './pages/admin/CategoryManager';
import SupplierManager from './pages/admin/SupplierManager';
import PurchaseOrderManager from './pages/admin/PurchaseOrderManager';

// Auth pages
import CustomerLogin from './pages/auth/CustomerLogin';
import CustomerRegister from './pages/auth/CustomerRegister';
import AdminLogin from './pages/auth/AdminLogin';
import AdminRegister from './pages/auth/AdminRegister';

import { ChefHat, ShoppingBag, User, Settings, ClipboardList, LogOut, LogIn } from 'lucide-react';

// ─── Guard components ────────────────────────────────────────────────
const RequireCustomer: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role !== 'USER') return <Navigate to="/admin/login" replace />;
  return <>{children}</>;
};

const RequireAdmin: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/admin/login" replace />;
  if (user.role !== 'ADMIN') return <Navigate to="/login" replace />;
  return <>{children}</>;
};

// ─── Navbar ──────────────────────────────────────────────────────────
const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-gradient-to-r from-orange-500 to-red-600 shadow-md text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <Link to="/" className="flex items-center gap-2 text-2xl font-extrabold tracking-tight hover:opacity-90 transition-opacity">
            <ChefHat size={28} className="animate-bounce" />
            <span>FoodieHub</span>
          </Link>
          <div className="flex items-center gap-4 font-semibold">
            <Link to="/" className="hover:text-orange-200 transition-colors flex items-center gap-1.5 py-2">
              <ShoppingBag size={18} /> Menu
            </Link>
            {user?.role === 'USER' && (
              <>
                <Link to="/orders" className="hover:text-orange-200 transition-colors flex items-center gap-1.5 py-2">
                  <ClipboardList size={18} /> My Orders
                </Link>
                <Link to="/profile" className="hover:text-orange-200 transition-colors flex items-center gap-1.5 py-2">
                  <User size={18} /> Profile
                </Link>
              </>
            )}
            {user?.role === 'ADMIN' && (
              <Link to="/admin" className="bg-white hover:bg-orange-50 text-orange-600 px-4 py-2 rounded-xl text-sm font-bold shadow-sm transition-all duration-200 flex items-center gap-1">
                <Settings size={16} /> Admin Portal
              </Link>
            )}
            {user ? (
              <div className="flex items-center gap-3">
                <span className="text-orange-100 text-sm hidden sm:block">Hi, {user.userName}!</span>
                <button
                  onClick={handleLogout}
                  className="flex items-center gap-1.5 bg-white/10 hover:bg-white/20 px-3 py-2 rounded-xl text-sm font-semibold transition-colors"
                >
                  <LogOut size={16} /> Logout
                </button>
              </div>
            ) : (
              <Link
                to="/login"
                className="flex items-center gap-1.5 bg-white/10 hover:bg-white/20 px-3 py-2 rounded-xl text-sm font-semibold transition-colors"
              >
                <LogIn size={16} /> Login
              </Link>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

// ─── Admin Side Nav ───────────────────────────────────────────────────
const AdminNav = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  return (
    <div className="bg-slate-900 text-white p-3 rounded-2xl flex flex-wrap gap-4 text-sm font-semibold items-center shadow-md">
      <span className="text-orange-500 font-bold uppercase tracking-wider text-[11px] border border-orange-500/30 px-2 py-0.5 rounded mr-2">Admin Panel</span>
      <Link to="/admin" className="hover:text-orange-400 transition-colors py-1">Dashboard</Link>
      <Link to="/admin/menu" className="hover:text-orange-400 transition-colors py-1">Menu</Link>
      <Link to="/admin/categories" className="hover:text-orange-400 transition-colors py-1">Categories</Link>
      <Link to="/admin/orders" className="hover:text-orange-400 transition-colors py-1">Orders</Link>
      <Link to="/admin/inventory" className="hover:text-orange-400 transition-colors py-1">Inventory</Link>
      <Link to="/admin/suppliers" className="hover:text-orange-400 transition-colors py-1">Suppliers</Link>
      <Link to="/admin/purchase-orders" className="hover:text-orange-400 transition-colors py-1">Purchase Orders</Link>
      <button
        onClick={() => { logout(); navigate('/admin/login'); }}
        className="ml-auto text-slate-400 hover:text-red-400 transition-colors flex items-center gap-1 py-1 text-xs"
      >
        <LogOut size={14} /> Logout
      </button>
    </div>
  );
};

// ─── Admin wrapper helper ─────────────────────────────────────────────
const AdminPage: React.FC<{ children: React.ReactNode }> = ({ children }) => (
  <RequireAdmin>
    <div className="flex flex-col gap-6">
      <AdminNav />
      {children}
    </div>
  </RequireAdmin>
);

// ─── Main App ─────────────────────────────────────────────────────────
function AppInner() {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col font-sans">
      <Routes>
        {/* Auth pages — full-screen, no navbar */}
        <Route path="/login" element={<CustomerLogin />} />
        <Route path="/register" element={<CustomerRegister />} />
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/admin/register" element={<AdminRegister />} />

        {/* All other pages — show navbar */}
        <Route path="*" element={
          <>
            <Navbar />
            <main className="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
              <Routes>
                {/* Customer public page */}
                <Route path="/" element={<MenuBrowser />} />

                {/* Customer protected pages */}
                <Route path="/checkout" element={<RequireCustomer><Checkout /></RequireCustomer>} />
                <Route path="/orders" element={<RequireCustomer><OrderHistory /></RequireCustomer>} />
                <Route path="/profile" element={<RequireCustomer><Profile /></RequireCustomer>} />

                {/* Admin protected pages */}
                <Route path="/admin" element={<AdminPage><Dashboard /></AdminPage>} />
                <Route path="/admin/menu" element={<AdminPage><MenuManager /></AdminPage>} />
                <Route path="/admin/categories" element={<AdminPage><CategoryManager /></AdminPage>} />
                <Route path="/admin/orders" element={<AdminPage><OrderManager /></AdminPage>} />
                <Route path="/admin/inventory" element={<AdminPage><InventoryManager /></AdminPage>} />
                <Route path="/admin/suppliers" element={<AdminPage><SupplierManager /></AdminPage>} />
                <Route path="/admin/purchase-orders" element={<AdminPage><PurchaseOrderManager /></AdminPage>} />
              </Routes>
            </main>
            <footer className="bg-white border-t border-gray-100 py-6 text-center text-sm text-gray-500">
              <div className="max-w-7xl mx-auto px-4">
                &copy; {new Date().getFullYear()} FoodieHub Restaurant. All rights reserved.
              </div>
            </footer>
          </>
        } />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <ErrorBoundary>
      <AuthProvider>
        <CartProvider>
          <BrowserRouter>
            <AppInner />
          </BrowserRouter>
        </CartProvider>
      </AuthProvider>
    </ErrorBoundary>
  );
}

export default App;
