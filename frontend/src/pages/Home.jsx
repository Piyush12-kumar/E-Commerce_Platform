import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import API from '../api/axios';
import ProductCard from '../components/product/ProductCard';
import { HiArrowRight, HiShoppingBag, HiTruck, HiShieldCheck } from 'react-icons/hi';

export default function Home() {
  const [featured, setFeatured] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    API.get('/products/featured').then(r => setFeatured(r.data)).catch(() => {});
    API.get('/categories/getAll').then(r => setCategories(r.data || [])).catch(() => {});
  }, []);

  return (
    <div>
      {/* Hero */}
      <section className="bg-gradient-to-br from-indigo-600 via-purple-600 to-pink-500 text-white">
        <div className="max-w-7xl mx-auto px-4 py-20 md:py-32 text-center">
          <h1 className="text-4xl md:text-6xl font-extrabold mb-6 leading-tight">
            Discover <span className="text-yellow-300">Amazing</span> Products
          </h1>
          <p className="text-lg md:text-xl text-indigo-100 mb-8 max-w-2xl mx-auto">
            Shop the latest trends with unbeatable prices and lightning-fast delivery
          </p>
          <Link to="/products" className="inline-flex items-center gap-2 bg-white text-indigo-600 px-8 py-3 rounded-full font-semibold hover:bg-gray-100 transition text-lg shadow-lg">
            Shop Now <HiArrowRight />
          </Link>
        </div>
      </section>

      {/* Features */}
      <section className="max-w-7xl mx-auto px-4 py-12">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[
            { icon: HiTruck, title: 'Free Shipping', desc: 'On orders over ₹500' },
            { icon: HiShieldCheck, title: 'Secure Payment', desc: '100% secure checkout' },
            { icon: HiShoppingBag, title: 'Easy Returns', desc: '30-day return policy' },
          ].map((f, i) => (
            <div key={i} className="flex items-center gap-4 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
              <div className="p-3 bg-indigo-100 rounded-lg"><f.icon className="text-indigo-600" size={24} /></div>
              <div><h3 className="font-semibold text-gray-800">{f.title}</h3><p className="text-sm text-gray-500">{f.desc}</p></div>
            </div>
          ))}
        </div>
      </section>

      {/* Categories */}
      {categories.length > 0 && (
        <section className="max-w-7xl mx-auto px-4 py-12">
          <h2 className="text-2xl md:text-3xl font-bold text-gray-800 mb-8 text-center">Shop by Category</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {categories.map((cat) => (
              <Link key={cat.categoryId} to={`/products?category=${cat.name}`}
                className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 text-center hover:shadow-md hover:border-indigo-200 transition group">
                <h3 className="font-semibold text-gray-700 group-hover:text-indigo-600 transition">{cat.name}</h3>
                {cat.description && <p className="text-xs text-gray-400 mt-1">{cat.description}</p>}
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Featured Products */}
      {featured.length > 0 && (
        <section className="max-w-7xl mx-auto px-4 py-12">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-2xl md:text-3xl font-bold text-gray-800">Featured Products</h2>
            <Link to="/products" className="text-indigo-600 hover:text-indigo-800 font-medium flex items-center gap-1">
              View All <HiArrowRight />
            </Link>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {featured.slice(0, 8).map((p) => <ProductCard key={p.productId} product={p} />)}
          </div>
        </section>
      )}
    </div>
  );
}

