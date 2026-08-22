import { Link, useNavigate } from 'react-router-dom';
import { formatCurrency } from '../../utils/formatters';
import { useCart } from '../../contexts/CartContext';
import { useAuth } from '../../contexts/AuthContext';
import { HiShoppingCart, HiLightningBolt } from 'react-icons/hi';

export default function ProductCard({ product }) {
  const { addToCart } = useCart();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="bg-white rounded-2xl shadow-sm hover:shadow-lg transition-all duration-300 overflow-hidden group border border-gray-100">
      <Link to={`/products/${product.productId}`}>
        <div className="aspect-square overflow-hidden bg-gray-100">
          <img
            src={product.imageURL || '/placeholder.png'}
            alt={product.name}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
            onError={(e) => { e.target.src = 'https://via.placeholder.com/300x300?text=No+Image'; }}
          />
        </div>
      </Link>
      <div className="p-4">
        <Link to={`/products/${product.productId}`}>
          <h3 className="font-semibold text-gray-800 truncate hover:text-indigo-600 transition">{product.name}</h3>
        </Link>
        <p className="text-sm text-gray-500 mt-1 line-clamp-2">{product.description}</p>
        <div className="flex items-center justify-between mt-3">
          <div>
            <span className="text-lg font-bold text-indigo-600">{formatCurrency(product.price)}</span>
            {product.discountPrice > 0 && (
              <span className="text-sm text-gray-400 line-through ml-2">{formatCurrency(product.discountPrice)}</span>
            )}
          </div>
          {isAuthenticated && product.stock !== 0 && (
            <div className="flex gap-2">
              <button
                onClick={() => addToCart(product.productId)}
                className="p-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
                title="Add to cart"
              >
                <HiShoppingCart size={18} />
              </button>
              <button
                onClick={async () => { await addToCart(product.productId); navigate('/checkout'); }}
                className="p-2 bg-gradient-to-r from-orange-500 to-pink-500 text-white rounded-lg hover:from-orange-600 hover:to-pink-600 transition"
                title="Buy now"
              >
                <HiLightningBolt size={18} />
              </button>
            </div>
          )}
        </div>
        {product.stock !== undefined && product.stock <= 5 && product.stock > 0 && (
          <p className="text-xs text-orange-500 mt-2">Only {product.stock} left!</p>
        )}
        {product.stock === 0 && <p className="text-xs text-red-500 mt-2 font-semibold">Out of stock</p>}
      </div>
    </div>
  );
}

