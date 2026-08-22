import { useCart } from '../contexts/CartContext';
import { Link, useNavigate } from 'react-router-dom';
import { formatCurrency } from '../utils/formatters';
import { HiTrash, HiShoppingBag, HiPlus, HiMinus } from 'react-icons/hi';

export default function Cart() {
  const { items, total, removeFromCart, removeItemFromCart, clearCart, addToCart, loading } = useCart();
  const navigate = useNavigate();

  if (loading) return <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" /></div>;

  if (items.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-20 text-center">
        <HiShoppingBag className="mx-auto text-gray-300" size={80} />
        <h2 className="text-2xl font-bold text-gray-700 mt-4">Your cart is empty</h2>
        <p className="text-gray-500 mt-2">Start shopping to add items to your cart</p>
        <Link to="/products" className="inline-block mt-6 bg-indigo-600 text-white px-8 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold">
          Browse Products
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Shopping Cart</h1>
        <span className="text-sm text-gray-500">{items.length} item{items.length > 1 ? 's' : ''}</span>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-4">
          {items.map((item) => (
            <div key={item.id} className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 flex gap-4 items-center">
              <Link to={`/products/${item.product?.productId}`}>
                <img src={item.product?.imageURL || 'https://via.placeholder.com/100'} alt={item.product?.name}
                  className="w-24 h-24 object-cover rounded-lg hover:opacity-80 transition"
                  onError={(e) => { e.target.src = 'https://via.placeholder.com/100x100?text=No+Image'; }} />
              </Link>
              <div className="flex-1 min-w-0">
                <Link to={`/products/${item.product?.productId}`} className="font-semibold text-gray-800 hover:text-indigo-600 transition block truncate">
                  {item.product?.name}
                </Link>
                <p className="text-sm text-gray-400 mt-0.5">{item.product?.category?.name}</p>
                <p className="text-sm text-gray-500 mt-1">Unit price: {formatCurrency(item.price)}</p>

                {/* Quantity Controls */}
                <div className="flex items-center gap-3 mt-3">
                  <div className="flex items-center border border-gray-200 rounded-lg overflow-hidden">
                    <button
                      onClick={() => removeFromCart(item.product?.productId)}
                      className="p-2 hover:bg-gray-100 transition text-gray-600"
                      title="Decrease quantity"
                    >
                      <HiMinus size={14} />
                    </button>
                    <span className="px-4 py-1 text-sm font-semibold text-gray-800 min-w-[40px] text-center">
                      {item.quantity}
                    </span>
                    <button
                      onClick={() => addToCart(item.product?.productId)}
                      className="p-2 hover:bg-gray-100 transition text-gray-600"
                      title="Increase quantity"
                    >
                      <HiPlus size={14} />
                    </button>
                  </div>
                </div>
              </div>

              <div className="text-right flex flex-col items-end gap-2">
                <p className="text-lg font-bold text-indigo-600">{formatCurrency(item.price * item.quantity)}</p>
                <button onClick={() => removeItemFromCart(item.product?.productId)} className="text-red-400 hover:text-red-600 transition p-2" title="Remove item">
                  <HiTrash size={18} />
                </button>
              </div>
            </div>
          ))}

          <div className="flex justify-between items-center pt-2">
            <button onClick={clearCart} className="text-red-500 hover:text-red-700 text-sm font-medium transition">
              Clear Entire Cart
            </button>
            <Link to="/products" className="text-indigo-600 hover:text-indigo-700 text-sm font-medium transition">
              ← Continue Shopping
            </Link>
          </div>
        </div>

        {/* Order Summary */}
        <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 h-fit sticky top-24">
          <h2 className="text-xl font-bold text-gray-800 mb-4">Order Summary</h2>
          <div className="space-y-3 text-sm">
            <div className="flex justify-between">
              <span className="text-gray-500">Subtotal ({items.reduce((s, i) => s + i.quantity, 0)} items)</span>
              <span className="font-medium">{formatCurrency(total)}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-500">Shipping</span>
              <span className="text-green-600 font-medium">Free</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-500">Tax</span>
              <span className="text-gray-600">Calculated at checkout</span>
            </div>
            <hr className="my-3" />
            <div className="flex justify-between text-lg font-bold">
              <span>Total</span>
              <span className="text-indigo-600">{formatCurrency(total)}</span>
            </div>
          </div>
          <button onClick={() => navigate('/checkout')}
            className="w-full mt-6 bg-indigo-600 text-white py-3 rounded-xl hover:bg-indigo-700 transition font-semibold text-lg shadow-md">
            Proceed to Checkout
          </button>
          <p className="text-xs text-gray-400 text-center mt-3">Secure checkout powered by ShopVerse</p>
        </div>
      </div>
    </div>
  );
}
