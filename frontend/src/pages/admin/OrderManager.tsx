import React, { useEffect, useState } from 'react';
import { orderApi } from '../../api/orderApi';
import { Order, OrderStatus } from '../../types';
import { CheckCircle, XCircle, Clock } from 'lucide-react';

const OrderManager: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);

  useEffect(() => {
    orderApi.getAll().then(setOrders);
  }, []);

  const updateStatus = async (id: number, status: OrderStatus) => {
    try {
      await orderApi.updateStatus(id, status);
      const updated = await orderApi.getAll();
      setOrders(updated);
    } catch (err) {
      alert('Failed to update order status');
    }
  };

  return (
    <div className="flex flex-col gap-6">
      <h1 className="text-2xl font-bold">Order Management</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {['PENDING', 'IN_PROGRESS', 'DELIVERED'].map(status => (
          <div key={status} className="bg-gray-200 p-4 rounded-lg min-h-[500px]">
            <h3 className="font-bold text-lg mb-4 flex items-center gap-2">
              {status === 'PENDING' && <Clock size={20} />}
              {status === 'IN_PROGRESS' && <div className="w-5 h-5 bg-blue-500 animate-spin rounded-full" />}
              {status === 'DELIVERED' && <CheckCircle size={20} />}
              {status}
            </h3>
            <div className="flex flex-col gap-3">
              {orders.filter(o => o.orderStatus === status).map(order => (
                <div key={order.orderId} className="bg-white p-3 rounded shadow-sm flex flex-col gap-2">
                  <div className="flex justify-between items-start">
                    <p className="font-bold">Order #{order.orderId}</p>
                    <p className="text-sm font-bold text-orange-600">₹{order.totalAmount.toFixed(2)}</p>
                  </div>
                  <p className="text-xs text-gray-500">{order.customerName}</p>
                  <div className="flex gap-2 mt-2">
                    {status === 'PENDING' && (
                      <button
                        onClick={() => updateStatus(order.orderId, 'IN_PROGRESS')}
                        className="text-xs bg-blue-500 text-white px-2 py-1 rounded"
                      >
                        Start Prep
                      </button>
                    )}
                    {status === 'IN_PROGRESS' && (
                      <button
                        onClick={() => updateStatus(order.orderId, 'DELIVERED')}
                        className="text-xs bg-green-500 text-white px-2 py-1 rounded"
                      >
                        Mark Delivered
                      </button>
                    )}
                    <button
                      onClick={() => updateStatus(order.orderId, 'CANCELLED')}
                      className="text-xs bg-red-500 text-white px-2 py-1 rounded"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrderManager;
