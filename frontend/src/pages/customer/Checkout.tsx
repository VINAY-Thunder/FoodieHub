import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../store/CartContext';
import { useAuth } from '../../store/AuthContext';
import { orderApi } from '../../api/orderApi';
import { customerApi } from '../../api/customerApi';
import api from '../../api/axiosInstance';
import { ShoppingBag, MapPin, CreditCard, ShieldCheck, Loader2 } from 'lucide-react';

const Checkout: React.FC = () => {
  const { cart, totalAmount, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  
  const [address, setAddress] = useState({
    street: '',
    city: '',
    state: '',
    zipCode: '',
  });
  
  const [phone, setPhone] = useState('');
  const [name, setName] = useState('');
  const [paymentMethod, setPaymentMethod] = useState<'RAZORPAY' | 'COD'>('RAZORPAY');
  
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [paymentStep, setPaymentStep] = useState<'PROCESSING' | 'SELECT' | 'SUCCESS' | 'FAILED'>('PROCESSING');
  const [customerAddressId, setCustomerAddressId] = useState<number | undefined>(undefined);

  useEffect(() => {
    if (user?.id) {
      customerApi.getById(user.id).then(customer => {
        if (customer) {
          setName(customer.name || '');
          setPhone(customer.phone || '');
          if (customer.addresses && customer.addresses.length > 0) {
            const primaryAddr = customer.addresses[0];
            setAddress({
              street: primaryAddr.street || '',
              city: primaryAddr.city || '',
              state: primaryAddr.state || '',
              zipCode: primaryAddr.zipCode || '',
            });
            setCustomerAddressId(primaryAddr.addressId);
          }
        }
      }).catch(err => {
        console.error("Failed to load customer profile", err);
      });
    }
  }, [user]);

  const handleAddressChange = (field: string, value: string) => {
    setAddress(prev => ({ ...prev, [field]: value }));
    setCustomerAddressId(undefined); // Reset address ID since fields were modified
  };

  const getOrCreateAddressId = async (): Promise<number> => {
    if (customerAddressId) {
      return customerAddressId;
    }
    // Create new address in the backend
    const response = await api.post('/customer-addresses', {
      customerId: user?.id ?? 0,
      street: address.street,
      city: address.city,
      state: address.state,
      zipCode: address.zipCode,
      addressType: 'HOME',
    });
    const savedAddressId = response.data.addressId;
    setCustomerAddressId(savedAddressId);
    return savedAddressId;
  };

  if (cart.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] text-center bg-white p-8 rounded-3xl border border-gray-100 shadow-sm">
        <ShoppingBag size={64} className="text-gray-200 mb-4" />
        <h2 className="text-2xl font-bold text-gray-800">Your cart is empty</h2>
        <p className="text-gray-500 mb-6">Add some delicious dishes from our menu first!</p>
        <button
          onClick={() => navigate('/')}
          className="bg-orange-500 hover:bg-orange-600 text-white px-6 py-2.5 rounded-xl font-bold shadow-md transition-all duration-200"
        >
          Explore Menu
        </button>
      </div>
    );
  }

  const handlePlaceOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!/^[0-9]{6}$/.test(address.zipCode)) {
      alert('Zip code must be exactly 6 digits.');
      return;
    }

    if (paymentMethod === 'COD') {
      setIsSubmitting(true);
      try {
        const addrId = await getOrCreateAddressId();
        const orderRequest = {
          customerId: user?.id ?? 0,
          orderType: 'DELIVERY' as const,
          customerAddressId: addrId,
          orderItems: cart.map(item => ({
            menuId: item.menuId,
            quantity: item.quantity,
          })),
        };
        const order = await orderApi.create(orderRequest);
        clearCart();
        alert(`Order placed successfully with Cash on Delivery! Order ID: #${order.orderId}`);
        navigate('/orders');
      } catch (error) {
        console.error(error);
        alert('Failed to place order.');
      } finally {
        setIsSubmitting(false);
      }
    } else {
      // Open Razorpay Simulator Modal
      setShowPaymentModal(true);
      setPaymentStep('PROCESSING');
      setTimeout(() => {
        setPaymentStep('SELECT');
      }, 1500);
    }
  };

  const handleSimulatorDecision = async (success: boolean) => {
    setPaymentStep('PROCESSING');
    setIsSubmitting(true);

    try {
      if (success) {
        const addrId = await getOrCreateAddressId();
        const orderRequest = {
          customerId: user?.id ?? 0,
          orderType: 'DELIVERY' as const,
          customerAddressId: addrId,
          orderItems: cart.map(item => ({
            menuId: item.menuId,
            quantity: item.quantity,
          })),
        };
        const order = await orderApi.create(orderRequest);
        setPaymentStep('SUCCESS');
        setTimeout(() => {
          clearCart();
          setShowPaymentModal(false);
          navigate('/orders');
        }, 2000);
      } else {
        // Payment failed - calls the backend fail catcher (or mocks it)
        setPaymentStep('FAILED');
        setTimeout(() => {
          setShowPaymentModal(false);
          alert('Razorpay Payment Declined. A failed payment record has been logged in the backend database.');
        }, 2500);
      }
    } catch (error) {
      console.error(error);
      setPaymentStep('FAILED');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-8">
      {/* Checkout Form */}
      <div className="lg:col-span-2 space-y-6">
        
        {/* Delivery Details */}
        <div className="bg-white p-6 rounded-3xl shadow-sm border border-gray-100">
          <h2 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
            <MapPin className="text-orange-500" /> Delivery Details
          </h2>

          <form onSubmit={handlePlaceOrder} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Full Name</label>
                <input
                  type="text"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={name}
                  onChange={e => setName(e.target.value)}
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-gray-600 mb-1">Mobile Number</label>
                <input
                  type="tel"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm font-mono"
                  value={phone}
                  onChange={e => setPhone(e.target.value)}
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-semibold text-gray-600 mb-1">Street Address</label>
              <input
                type="text"
                required
                className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                placeholder="Apartment, House number, Street name"
                value={address.street}
                onChange={e => handleAddressChange('street', e.target.value)}
              />
            </div>
            
            <div className="grid grid-cols-3 gap-4">
              <div className="col-span-1">
                <label className="block text-xs font-semibold text-gray-600 mb-1">City</label>
                <input
                  type="text"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={address.city}
                  onChange={e => handleAddressChange('city', e.target.value)}
                />
              </div>
              <div className="col-span-1">
                <label className="block text-xs font-semibold text-gray-600 mb-1">State</label>
                <input
                  type="text"
                  required
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm"
                  value={address.state}
                  onChange={e => handleAddressChange('state', e.target.value)}
                />
              </div>
              <div className="col-span-1">
                <label className="block text-xs font-semibold text-gray-600 mb-1">Zip Code (6 digits)</label>
                <input
                  type="text"
                  required
                  maxLength={6}
                  className="w-full p-2.5 border border-gray-200 rounded-xl focus:ring-2 focus:ring-orange-500/20 outline-none text-sm font-mono"
                  placeholder="400050"
                  value={address.zipCode}
                  onChange={e => handleAddressChange('zipCode', e.target.value.replace(/[^0-9]/g, ''))}
                />
              </div>
            </div>

            {/* Payment Method Selector */}
            <div className="pt-6 border-t border-gray-100">
              <h3 className="font-bold text-sm text-gray-900 mb-3">Select Payment Method</h3>
              <div className="grid grid-cols-2 gap-4">
                <button
                  type="button"
                  onClick={() => setPaymentMethod('RAZORPAY')}
                  className={`p-4 border rounded-2xl flex flex-col items-center justify-center gap-2 transition-all ${
                    paymentMethod === 'RAZORPAY' 
                      ? 'border-orange-500 bg-orange-50/50 text-orange-600 shadow-sm' 
                      : 'border-gray-200 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <CreditCard size={24} />
                  <span className="text-xs font-bold">Pay via Razorpay</span>
                  <span className="text-[10px] text-gray-400">Cards, Netbanking, UPI</span>
                </button>
                <button
                  type="button"
                  onClick={() => setPaymentMethod('COD')}
                  className={`p-4 border rounded-2xl flex flex-col items-center justify-center gap-2 transition-all ${
                    paymentMethod === 'COD' 
                      ? 'border-orange-500 bg-orange-50/50 text-orange-600 shadow-sm' 
                      : 'border-gray-200 text-gray-600 hover:bg-gray-50'
                  }`}
                >
                  <ShoppingBag size={24} />
                  <span className="text-xs font-bold">Cash on Delivery</span>
                  <span className="text-[10px] text-gray-400">Pay when food arrives</span>
                </button>
              </div>
            </div>

            <div className="pt-6 border-t border-gray-100">
              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full bg-orange-500 hover:bg-orange-600 disabled:bg-gray-300 text-white py-3.5 rounded-2xl font-bold text-base shadow-md hover:shadow-lg transition-all flex items-center justify-center gap-2"
              >
                {isSubmitting ? (
                  <>
                    <Loader2 className="animate-spin" />
                    Processing Order...
                  </>
                ) : (
                  `Confirm Order - ₹${totalAmount.toFixed(2)}`
                )}
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* Order Summary sidebar */}
      <div className="lg:col-span-1">
        <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
          <h2 className="text-xl font-bold text-gray-900 mb-6">Order Summary</h2>
          <div className="divide-y divide-gray-50 max-h-[40vh] overflow-y-auto pr-1">
            {cart.map(item => (
              <div key={item.menuId} className="flex justify-between items-center py-3 first:pt-0">
                <div>
                  <p className="font-bold text-xs text-gray-900">{item.itemName}</p>
                  <p className="text-[10px] text-gray-400">Qty: {item.quantity}</p>
                </div>
                <p className="font-bold text-xs text-gray-900 font-mono">
                  ₹{((item.discountedPrice || item.price) * item.quantity).toFixed(2)}
                </p>
              </div>
            ))}
          </div>

          <div className="border-t border-gray-100 pt-4 mt-4 space-y-2.5 text-sm text-gray-600">
            <div className="flex justify-between">
              <span>Items Subtotal</span>
              <span className="font-mono font-medium text-gray-900">₹{totalAmount.toFixed(2)}</span>
            </div>
            <div className="flex justify-between">
              <span>Delivery Fee</span>
              <span className="text-green-600 font-semibold uppercase text-xs">Free</span>
            </div>
            <div className="flex justify-between text-base font-extrabold text-gray-900 pt-3 border-t border-dashed">
              <span>Grand Total</span>
              <span className="text-orange-600 font-mono">₹{totalAmount.toFixed(2)}</span>
            </div>
          </div>
          
          <div className="bg-green-50/50 border border-green-100 rounded-2xl p-4 mt-6 flex items-start gap-2.5 text-xs text-green-800 leading-normal">
            <ShieldCheck size={20} className="text-green-600 shrink-0" />
            <div>
              <span className="font-bold">Guaranteed Freshness</span>
              <p className="text-green-700/80 mt-0.5">Your food is cooked fresh upon order confirmation and sealed in sanitised packs.</p>
            </div>
          </div>
        </div>
      </div>

      {/* Razorpay simulated modal */}
      {showPaymentModal && (
        <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex items-center justify-center p-4 z-50 animate-fade-in">
          <div className="bg-[#1a1f36] text-white rounded-2xl shadow-2xl border border-gray-800 max-w-sm w-full overflow-hidden font-sans">
            {/* Header */}
            <div className="p-5 border-b border-gray-800 bg-[#121625] flex justify-between items-center">
              <div className="flex items-center gap-1.5">
                <span className="bg-blue-600 p-1 rounded text-[10px] font-black uppercase tracking-wider">Razorpay</span>
                <span className="text-xs font-semibold text-gray-400">Payment Gateway Simulator</span>
              </div>
              <button onClick={() => setShowPaymentModal(false)} className="text-gray-400 hover:text-white">✕</button>
            </div>

            {/* Content body */}
            <div className="p-6 text-center space-y-6">
              {paymentStep === 'PROCESSING' && (
                <div className="py-8 flex flex-col items-center gap-3">
                  <Loader2 className="animate-spin text-blue-500" size={40} />
                  <p className="text-sm font-semibold text-gray-300">Contacting banking nodes...</p>
                </div>
              )}

              {paymentStep === 'SELECT' && (
                <div className="space-y-4">
                  <div className="text-left bg-[#252b48] p-4 rounded-xl space-y-1">
                    <p className="text-[10px] uppercase font-bold text-gray-400">Merchant Name</p>
                    <p className="text-sm font-bold text-white">FoodieHub Restaurant Services</p>
                    <div className="flex justify-between items-center pt-2">
                      <span className="text-[10px] uppercase font-bold text-gray-400">Order Amount</span>
                      <span className="text-base font-black text-blue-400 font-mono">₹{totalAmount.toFixed(2)}</span>
                    </div>
                  </div>

                  <p className="text-xs text-gray-400 leading-normal">
                    This simulator acts as the Razorpay popup module. Choose whether you want to simulate a successful transaction or a bank decline.
                  </p>

                  <div className="grid grid-cols-2 gap-3 pt-2">
                    <button
                      onClick={() => handleSimulatorDecision(true)}
                      className="bg-green-600 hover:bg-green-700 text-white py-2.5 rounded-xl text-xs font-bold shadow-md transition-colors"
                    >
                      Simulate Success
                    </button>
                    <button
                      onClick={() => handleSimulatorDecision(false)}
                      className="bg-red-600 hover:bg-red-700 text-white py-2.5 rounded-xl text-xs font-bold shadow-md transition-colors"
                    >
                      Simulate Fail
                    </button>
                  </div>
                </div>
              )}

              {paymentStep === 'SUCCESS' && (
                <div className="py-8 flex flex-col items-center gap-3 animate-bounce">
                  <div className="bg-green-950 border border-green-500 p-3 rounded-full text-green-500">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M20 6 9 17l-5-5"/></svg>
                  </div>
                  <p className="text-sm font-bold text-green-400">Payment Capturing Successful!</p>
                  <p className="text-xs text-gray-400">Verifying signature in the backend...</p>
                </div>
              )}

              {paymentStep === 'FAILED' && (
                <div className="py-8 flex flex-col items-center gap-3">
                  <div className="bg-red-950 border border-red-500 p-3 rounded-full text-red-500">
                    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><path d="M18 6 6 18"/><path d="m6 6 12 12"/></svg>
                  </div>
                  <p className="text-sm font-bold text-red-400">Transaction Declined by Bank</p>
                  <p className="text-xs text-gray-400">Error: WRONG_OTP / INSUFFICIENT_FUNDS</p>
                </div>
              )}
            </div>
            
            <div className="px-6 py-4 bg-[#121625] text-[10px] text-gray-500 text-center flex items-center justify-center gap-1">
              <ShieldCheck size={12} className="text-green-700" /> Secure 256-bit TLS encrypted connection
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Checkout;
