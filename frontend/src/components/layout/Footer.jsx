import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="bg-gray-900 text-gray-300 mt-auto">
      <div className="max-w-7xl mx-auto px-4 py-12 grid grid-cols-1 md:grid-cols-3 gap-8">
        <div>
          <h3 className="text-xl font-bold text-white mb-3">ShopVerse</h3>
          <p className="text-sm">Your one-stop destination for the best products at amazing prices.</p>
        </div>
        <div>
          <h4 className="font-semibold text-white mb-3">Quick Links</h4>
          <div className="space-y-2 text-sm">
            <Link to="/products" className="block hover:text-white transition">All Products</Link>
            <Link to="/cart" className="block hover:text-white transition">Cart</Link>
            <Link to="/orders" className="block hover:text-white transition">My Orders</Link>
          </div>
        </div>
        <div>
          <h4 className="font-semibold text-white mb-3">Contact</h4>
          <p className="text-sm">support@shopverse.com</p>
          <p className="text-sm mt-1">+91 98765 43210</p>
        </div>
      </div>
      <div className="border-t border-gray-800 text-center py-4 text-xs text-gray-500">
        © 2026 ShopVerse. All rights reserved.
      </div>
    </footer>
  );
}

