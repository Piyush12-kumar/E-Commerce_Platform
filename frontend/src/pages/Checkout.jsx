import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';
import { useCart } from '../contexts/CartContext';
import { formatCurrency } from '../utils/formatters';
import toast from 'react-hot-toast';
import { HiCheckCircle } from 'react-icons/hi';

export default function Checkout() {
  const { items, total, fetchCart } = useCart();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [order, setOrder] = useState(null);

  const placeOrder = async () => {
    setLoading(true);
    try {
      const res = await API.post('/orders/create');
      setOrder(res.data);
      setSuccess(true);
      fetchCart();
      toast.success('Order placed successfully!');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to place order');
    } finally { setLoading(false); }
  };

  if (success) {
    return (
      <div className="max-w-lg mx-auto px-4 py-20 text-center">
        <HiCheckCircle className="mx-auto text-green-500" size={80} />
        <h1 className="text-3xl font-bold text-gray-800 mt-4">Order Placed!</h1>
        <p className="text-gray-500 mt-2">Order #{order?.orderNumber || order?.orderId}</p>
        <p className="text-gray-500 mt-1">Total: {formatCurrency(order?.totalAmount)}</p>
        <div className="flex gap-4 justify-center mt-8">
          <button onClick={() => navigate('/orders')} className="bg-indigo-600 text-white px-6 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold">View Orders</button>
          <button onClick={() => navigate('/products')} className="border border-gray-200 text-gray-700 px-6 py-3 rounded-xl hover:bg-gray-50 transition font-medium">Continue Shopping</button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">Checkout</h1>
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">Order Items</h2>
        <div className="space-y-3 mb-6">
          {items.map(item => (
            <div key={item.id} className="flex justify-between items-center py-2 border-b border-gray-50">
              <div>
                <p className="font-medium text-gray-700">{item.product?.name}</p>
                <p className="text-sm text-gray-400">Qty: {item.quantity} × {formatCurrency(item.price)}</p>
              </div>
              <span className="font-semibold">{formatCurrency(item.price * item.quantity)}</span>
            </div>
          ))}
        </div>
        <div className="border-t border-gray-200 pt-4 flex justify-between text-xl font-bold">
          <span>Total</span><span className="text-indigo-600">{formatCurrency(total)}</span>
        </div>
        <button onClick={placeOrder} disabled={loading || items.length === 0}
          className="w-full mt-6 bg-indigo-600 text-white py-3 rounded-xl hover:bg-indigo-700 transition font-semibold text-lg disabled:opacity-50">
          {loading ? 'Placing Order...' : 'Place Order'}
        </button>
      </div>
    </div>
  );
}

