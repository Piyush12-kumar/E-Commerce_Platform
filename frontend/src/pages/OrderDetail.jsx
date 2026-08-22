import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api/axios';
import { formatCurrency, formatDate } from '../utils/formatters';
import toast from 'react-hot-toast';

export default function OrderDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get(`/orders/${id}`).then(r => setOrder(r.data)).catch(() => toast.error('Order not found')).finally(() => setLoading(false));
  }, [id]);

  const cancelOrder = async () => {
    if (!window.confirm('Are you sure you want to cancel this order?')) return;
    try {
      const res = await API.put(`/orders/${id}/cancel`);
      setOrder(res.data);
      toast.success('Order cancelled');
    } catch (err) { toast.error(err.response?.data?.message || 'Cannot cancel order'); }
  };

  if (loading) return <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" /></div>;
  if (!order) return <div className="text-center py-20 text-gray-500">Order not found</div>;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <button onClick={() => navigate('/orders')} className="text-indigo-600 hover:underline mb-4 inline-block">← Back to Orders</button>
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <div className="flex flex-wrap justify-between items-start gap-4 mb-6">
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Order #{order.orderNumber || order.orderId}</h1>
            <p className="text-gray-500 text-sm">{formatDate(order.orderDate)}</p>
          </div>
          <span className={`px-4 py-1.5 rounded-full text-sm font-semibold ${order.status === 'CANCELLED' ? 'bg-red-100 text-red-700' : order.status === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'}`}>
            {order.status}
          </span>
        </div>

        <h2 className="font-semibold text-gray-700 mb-3">Items</h2>
        <div className="space-y-3 mb-6">
          {order.orderItems?.map(item => (
            <div key={item.orderItemId} className="flex justify-between items-center py-3 border-b border-gray-50">
              <div>
                <p className="font-medium text-gray-700">{item.product?.name}</p>
                <p className="text-sm text-gray-400">{item.quantity} × {formatCurrency(item.pricePerUnit)}</p>
              </div>
              <span className="font-semibold">{formatCurrency(item.totalPrice)}</span>
            </div>
          ))}
        </div>

        <div className="border-t border-gray-200 pt-4 flex justify-between text-xl font-bold">
          <span>Total</span><span className="text-indigo-600">{formatCurrency(order.totalAmount)}</span>
        </div>

        {order.status === 'PENDING' && (
          <button onClick={cancelOrder} className="mt-6 bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition font-medium">
            Cancel Order
          </button>
        )}
      </div>
    </div>
  );
}

