import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div className="bg-white p-6 rounded-lg shadow text-center">
        <h3 className="text-gray-500 font-medium">Total Orders</h3>
        <p className="text-3xl font-bold">1,234</p>
        <Link to="/admin/orders" className="text-orange-500 text-sm hover:underline">View All</Link>
      </div>
      <div className="bg-white p-6 rounded-lg shadow text-center">
        <h3 className="text-gray-500 font-medium">Active Menu Items</h3>
        <p className="text-3xl font-bold">85</p>
        <Link to="/admin/menu" className="text-orange-500 text-sm hover:underline">Manage Menu</Link>
      </div>
      <div className="bg-white p-6 rounded-lg shadow text-center">
        <h3 className="text-gray-500 font-medium">Low Stock Items</h3>
        <p className="text-3xl font-bold text-red-500">12</p>
        <Link to="/admin/inventory" className="text-orange-500 text-sm hover:underline">Check Inventory</Link>
      </div>
    </div>
  );
};

export default Dashboard;
