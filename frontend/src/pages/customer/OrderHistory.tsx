import React, { useEffect, useState } from 'react';
import { orderApi } from '../../api/orderApi';
import { useAuth } from '../../store/AuthContext';
import type { Order } from '../../types';
import { ClipboardList, Calendar, IndianRupee, Ban, RefreshCw, XCircle } from 'lucide-react';

const OrderHistory: React.FC = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  const loadOrders = async () => {
    if (!user) return;
    setLoading(true);
    try {
      const data = await orderApi.getByCustomer(user.id);
      const sorted = (data || []).sort((a, b) => {
        return new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime();
      });
      setOrders(sorted);
    } catch (err) {
      console.error(err);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, [user]);

  const handleCancelOrder = async (orderId: number) => {
    const reason = prompt("Please provide a reason for cancelling your order:");
    if (reason === null) return; // cancelled prompt
    
    if (!reason.trim()) {
      alert("A cancellation reason is required.");
      return;
    }

    try {
      await orderApi.cancel(orderId, reason);
      alert(`Order #${orderId} cancelled successfully.`);
      loadOrders();
    } catch (err) {
      alert("Failed to cancel order.");
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight flex items-center gap-2">
            <ClipboardList className="text-orange-500" /> My Orders
          </h1>
          <p className="text-gray-500 mt-1">Review your order pipeline and history.</p>
        </div>
        <button 
          onClick={loadOrders}
          className="p-2 border border-gray-200 hover:bg-gray-50 rounded-xl text-gray-500 transition-colors"
          title="Refresh List"
        >
          <RefreshCw size={18} />
        </button>
      </div>

      {loading ? (
        <div className="space-y-4">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm animate-pulse h-32" />
          ))}
        </div>
      ) : orders.length === 0 ? (
        <div className="text-center py-20 bg-white rounded-3xl border border-dashed border-gray-200">
          <ClipboardList size={48} className="text-gray-200 mx-auto mb-3" />
          <h3 className="text-lg font-bold text-gray-700">No orders found</h3>
          <p className="text-gray-400 mt-1">You haven't placed any orders yet. Go to the menu to grab some food!</p>
        </div>
      ) : (
        <div className="space-y-5">
          {orders.map(order => {
            const isPending = order.orderStatus === 'PENDING' || order.orderStatus === 'IN_PROGRESS';
            return (
              <div 
                key={order.orderId} 
                className="bg-white border border-gray-100 hover:border-gray-200 rounded-3xl p-6 shadow-sm hover:shadow-md transition-all duration-300 flex flex-col md:flex-row justify-between md:items-center gap-6"
              >
                <div className="space-y-3">
                  <div className="flex items-center gap-3">
                    <span className="text-base font-extrabold text-gray-900 font-mono">Order #{order.orderId}</span>
                    <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase ${
                      order.orderStatus === 'DELIVERED' ? 'bg-green-100 text-green-700' :
                      order.orderStatus === 'CANCELLED' ? 'bg-red-100 text-red-700' : 
                      order.orderStatus === 'PENDING' ? 'bg-amber-100 text-amber-700' : 'bg-blue-100 text-blue-700'
                    }`}>
                      {order.orderStatus}
                    </span>
                  </div>

                  <div className="flex flex-wrap items-center gap-x-4 gap-y-1 text-xs text-gray-400">
                    <span className="flex items-center gap-1">
                      <Calendar size={13} /> {new Date(order.orderDate).toLocaleString()}
                    </span>
                    <span className="flex items-center gap-1 font-semibold text-orange-600">
                      <IndianRupee size={13} /> Total: ₹{order.totalAmount.toFixed(2)}
                    </span>
                  </div>

                  {/* Order Items Breakdown */}
                  <div className="flex flex-wrap gap-2 pt-1">
                    {order.orderItems?.map(item => (
                      <span key={item.orderItemId} className="bg-gray-50 border border-gray-100 text-gray-700 text-[11px] font-medium px-2.5 py-1 rounded-xl">
                        {item.itemName} <span className="font-bold text-gray-900">x{item.quantity}</span>
                      </span>
                    ))}
                  </div>

                  {order.cancellationReason && (
                    <p className="text-xs text-red-500 bg-red-50 border border-red-100 px-3 py-1.5 rounded-xl flex items-center gap-1.5 w-fit">
                      <XCircle size={14} className="shrink-0" />
                      <span>Cancelled Reason: {order.cancellationReason}</span>
                    </p>
                  )}
                </div>

                <div className="flex items-center gap-3 self-end md:self-center">
                  {isPending && (
                    <button
                      onClick={() => handleCancelOrder(order.orderId)}
                      className="flex items-center gap-1 px-4 py-2 border border-red-200 hover:bg-red-50 text-red-600 font-bold rounded-xl text-xs transition-colors"
                    >
                      <Ban size={14} /> Cancel Order
                    </button>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default OrderHistory;
