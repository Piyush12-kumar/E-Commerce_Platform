import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api/axios';
import { useCart } from '../contexts/CartContext';
import { useAuth } from '../contexts/AuthContext';
import { formatCurrency, formatDate } from '../utils/formatters';
import StarRating from '../components/common/StarRating';
import { HiShoppingCart, HiStar, HiLightningBolt } from 'react-icons/hi';
import toast from 'react-hot-toast';

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { addToCart } = useCart();
  const { isAuthenticated } = useAuth();
  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reviewForm, setReviewForm] = useState({ rating: 5, comment: '' });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    setLoading(true);
    API.get(`/products/get/${id}`)
      .then(r => setProduct(r.data))
      .catch(() => toast.error('Product not found'))
      .finally(() => setLoading(false));
    API.get(`/reviews/product/${id}`).then(r => setReviews(r.data || [])).catch(() => {});
  }, [id]);

  const submitReview = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await API.post('/reviews/add', { ...reviewForm, product: { productId: parseInt(id) } });
      toast.success('Review submitted!');
      setReviewForm({ rating: 5, comment: '' });
      const r = await API.get(`/reviews/product/${id}`);
      setReviews(r.data || []);
    } catch (err) { toast.error(err.response?.data?.message || 'Failed to submit review'); }
    finally { setSubmitting(false); }
  };

  if (loading) return <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" /></div>;
  if (!product) return <div className="text-center py-20 text-gray-500 text-xl">Product not found</div>;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
        {/* Image */}
        <div className="bg-white rounded-2xl overflow-hidden shadow-sm border border-gray-100">
          <img src={product.imageURL || 'https://via.placeholder.com/600'} alt={product.name}
            className="w-full h-[500px] object-cover"
            onError={(e) => { e.target.src = 'https://via.placeholder.com/600x600?text=No+Image'; }} />
        </div>

        {/* Info */}
        <div className="space-y-6">
          <div>
            {product.category && <span className="text-sm text-indigo-600 font-medium bg-indigo-50 px-3 py-1 rounded-full">{product.category.name}</span>}
            <h1 className="text-3xl font-bold text-gray-800 mt-3">{product.name}</h1>
          </div>

          {product.averageRating > 0 && (
            <div className="flex items-center gap-2">
              <StarRating rating={product.averageRating} size={20} />
              <span className="text-sm text-gray-500">({product.averageRating.toFixed(1)})</span>
            </div>
          )}

          <div className="flex items-baseline gap-3">
            <span className="text-3xl font-bold text-indigo-600">{formatCurrency(product.price)}</span>
            {product.discountPrice > 0 && <span className="text-xl text-gray-400 line-through">{formatCurrency(product.discountPrice)}</span>}
          </div>

          <p className="text-gray-600 leading-relaxed">{product.description}</p>

          <div className="flex gap-4 text-sm text-gray-500">
            {product.sku && <span>SKU: {product.sku}</span>}
            <span>Stock: <span className={product.stock > 0 ? 'text-green-600 font-medium' : 'text-red-600 font-medium'}>{product.stock > 0 ? product.stock : 'Out of stock'}</span></span>
          </div>

          {product.tags?.length > 0 && (
            <div className="flex gap-2 flex-wrap">
              {product.tags.map(t => <span key={t.tagId || t.name} className="text-xs bg-gray-100 text-gray-600 px-3 py-1 rounded-full">{t.name}</span>)}
            </div>
          )}

          {isAuthenticated && product.stock > 0 && (
            <div className="flex gap-4 flex-wrap">
              <button onClick={() => addToCart(product.productId)}
                className="flex items-center gap-2 bg-indigo-600 text-white px-8 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold text-lg shadow-md">
                <HiShoppingCart size={22} /> Add to Cart
              </button>
              <button onClick={async () => { await addToCart(product.productId); navigate('/checkout'); }}
                className="flex items-center gap-2 bg-gradient-to-r from-orange-500 to-pink-500 text-white px-8 py-3 rounded-xl hover:from-orange-600 hover:to-pink-600 transition font-semibold text-lg shadow-md">
                <HiLightningBolt size={22} /> Buy Now
              </button>
            </div>
          )}
          {!isAuthenticated && product.stock > 0 && (
            <button onClick={() => navigate('/login')}
              className="flex items-center gap-2 bg-indigo-600 text-white px-8 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold text-lg shadow-md">
              <HiShoppingCart size={22} /> Login to Buy
            </button>
          )}
        </div>
      </div>

      {/* Reviews */}
      <section className="mt-16">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Customer Reviews ({reviews.length})</h2>

        {isAuthenticated && (
          <form onSubmit={submitReview} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
            <h3 className="font-semibold text-gray-700 mb-4">Write a Review</h3>
            <div className="flex items-center gap-4 mb-4">
              <label className="text-sm text-gray-600">Rating:</label>
              <div className="flex gap-1">
                {[1, 2, 3, 4, 5].map(s => (
                  <button type="button" key={s} onClick={() => setReviewForm({ ...reviewForm, rating: s })}>
                    <HiStar size={24} className={s <= reviewForm.rating ? 'text-yellow-400' : 'text-gray-300'} />
                  </button>
                ))}
              </div>
            </div>
            <textarea value={reviewForm.comment} onChange={(e) => setReviewForm({ ...reviewForm, comment: e.target.value })}
              placeholder="Share your experience..." rows={3}
              className="w-full border border-gray-200 rounded-lg p-3 outline-none focus:ring-2 focus:ring-indigo-500 mb-4" required />
            <button type="submit" disabled={submitting}
              className="bg-indigo-600 text-white px-6 py-2 rounded-lg hover:bg-indigo-700 transition disabled:opacity-50 font-medium">
              {submitting ? 'Submitting...' : 'Submit Review'}
            </button>
          </form>
        )}

        <div className="space-y-4">
          {reviews.length === 0 ? <p className="text-gray-500">No reviews yet. Be the first!</p> :
            reviews.map((r) => (
              <div key={r.id} className="bg-white p-5 rounded-xl shadow-sm border border-gray-100">
                <div className="flex justify-between items-start">
                  <div>
                    <StarRating rating={r.rating} />
                    <p className="font-medium text-gray-700 mt-1">{r.user?.username || 'Anonymous'}</p>
                  </div>
                  <span className="text-xs text-gray-400">{formatDate(r.createdAt)}</span>
                </div>
                <p className="text-gray-600 mt-2">{r.comment}</p>
              </div>
            ))
          }
        </div>
      </section>
    </div>
  );
}

