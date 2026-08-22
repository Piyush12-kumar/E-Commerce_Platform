import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import API from '../api/axios';
import ProductCard from '../components/product/ProductCard';
import { HiSearch, HiAdjustments } from 'react-icons/hi';

export default function ProductList() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [showFilters, setShowFilters] = useState(false);

  const page = parseInt(searchParams.get('page') || '0');
  const search = searchParams.get('search') || '';
  const category = searchParams.get('category') || '';
  const sort = searchParams.get('sort') || 'name,asc';

  useEffect(() => {
    API.get('/categories/getAll').then(r => setCategories(r.data || [])).catch(() => {});
  }, []);

  useEffect(() => {
    setLoading(true);
    const params = { page, size: 12, sort };
    if (search) params.name = search;
    if (category) {
      const cat = categories.find(c => c.name === category);
      if (cat) params.categoryId = cat.categoryId;
    }
    API.get('/products/allProducts', { params })
      .then(r => { setProducts(r.data.products || []); setTotalPages(r.data.totalPages || 0); })
      .catch(() => setProducts([]))
      .finally(() => setLoading(false));
  }, [page, search, category, sort, categories]);

  const updateParam = (key, value) => {
    const p = new URLSearchParams(searchParams);
    if (value) p.set(key, value); else p.delete(key);
    if (key !== 'page') p.set('page', '0');
    setSearchParams(p);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-6">All Products</h1>

      {/* Search & Filter Bar */}
      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <HiSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={20} />
          <input
            type="text" placeholder="Search products..."
            value={search}
            onChange={(e) => updateParam('search', e.target.value)}
            className="w-full pl-10 pr-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none"
          />
        </div>
        <select value={category} onChange={(e) => updateParam('category', e.target.value)}
          className="px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none bg-white">
          <option value="">All Categories</option>
          {categories.map(c => <option key={c.categoryId} value={c.name}>{c.name}</option>)}
        </select>
        <select value={sort} onChange={(e) => updateParam('sort', e.target.value)}
          className="px-4 py-3 border border-gray-200 rounded-xl focus:ring-2 focus:ring-indigo-500 outline-none bg-white">
          <option value="name,asc">Name A-Z</option>
          <option value="name,desc">Name Z-A</option>
          <option value="price,asc">Price Low-High</option>
          <option value="price,desc">Price High-Low</option>
          <option value="createdAt,desc">Newest</option>
        </select>
      </div>

      {loading ? (
        <div className="flex justify-center py-20"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" /></div>
      ) : products.length === 0 ? (
        <div className="text-center py-20 text-gray-500">
          <p className="text-xl">No products found</p>
          <p className="text-sm mt-2">Try adjusting your search or filters</p>
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {products.map(p => <ProductCard key={p.productId} product={p} />)}
          </div>
          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center gap-2 mt-10">
              <button disabled={page === 0} onClick={() => updateParam('page', String(page - 1))}
                className="px-4 py-2 rounded-lg border border-gray-200 disabled:opacity-40 hover:bg-gray-100 transition">Prev</button>
              {[...Array(totalPages)].map((_, i) => (
                <button key={i} onClick={() => updateParam('page', String(i))}
                  className={`px-4 py-2 rounded-lg transition ${i === page ? 'bg-indigo-600 text-white' : 'border border-gray-200 hover:bg-gray-100'}`}>{i + 1}</button>
              ))}
              <button disabled={page >= totalPages - 1} onClick={() => updateParam('page', String(page + 1))}
                className="px-4 py-2 rounded-lg border border-gray-200 disabled:opacity-40 hover:bg-gray-100 transition">Next</button>
            </div>
          )}
        </>
      )}
    </div>
  );
}

