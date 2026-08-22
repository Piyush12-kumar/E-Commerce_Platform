import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import API from '../api/axios';
import { formatCurrency, formatDate } from '../utils/formatters';
import { HiClipboardList } from 'react-icons/hi';

const statusColors = { PENDING: 'bg-yellow-100 text-yellow-700', PROCESSING: 'bg-blue-100 text-blue-700', SHIPPED: 'bg-purple-100 text-purple-700', DELIVERED: 'bg-green-100 text-green-700', COMPLETED: 'bg-green-100 text-green-700', CANCELLED: 'bg-red-100 text-red-700' };

export default function Orders() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get('/orders/getAll').then(r => setOrders(r.data || [])).catch(() => {}).finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" /></div>;

  if (orders.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-20 text-center">
        <HiClipboardList className="mx-auto text-gray-300" size={80} />
        <h2 className="text-2xl font-bold text-gray-700 mt-4">No orders yet</h2>
        <Link to="/products" className="inline-block mt-6 bg-indigo-600 text-white px-8 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold">Start Shopping</Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">My Orders</h1>
      <div className="space-y-4">
        {orders.map(order => (
          <Link key={order.orderId} to={`/orders/${order.orderId}`}
            className="block bg-white rounded-xl shadow-sm border border-gray-100 p-6 hover:shadow-md transition">
            <div className="flex flex-wrap justify-between items-center gap-4">
              <div>
                <p className="font-semibold text-gray-800">Order #{order.orderNumber || order.orderId}</p>
                <p className="text-sm text-gray-500">{formatDate(order.orderDate)}</p>
              </div>
              <div className="flex items-center gap-4">
                <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statusColors[order.status] || 'bg-gray-100 text-gray-700'}`}>{order.status}</span>
                <span className="text-lg font-bold text-indigo-600">{formatCurrency(order.totalAmount)}</span>
              </div>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

