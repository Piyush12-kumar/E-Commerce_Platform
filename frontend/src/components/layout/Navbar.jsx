import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { useCart } from '../../contexts/CartContext';
import { HiShoppingCart, HiUser, HiMenu, HiX } from 'react-icons/hi';
import { useState } from 'react';

export default function Navbar() {
  const { isAuthenticated, isAdmin, user, logout } = useAuth();
  const { itemCount } = useCart();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <nav className="bg-white shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16 items-center">
          <Link to="/" className="text-2xl font-bold bg-gradient-to-r from-indigo-600 to-purple-600 bg-clip-text text-transparent">
            ShopVerse
          </Link>

          {/* Desktop Nav */}
          <div className="hidden md:flex items-center gap-6">
            <Link to="/products" className="text-gray-600 hover:text-indigo-600 font-medium transition">Products</Link>
            {isAuthenticated && (
              <>
                <Link to="/orders" className="text-gray-600 hover:text-indigo-600 font-medium transition">Orders</Link>
                <Link to="/cart" className="relative text-gray-600 hover:text-indigo-600 transition">
                  <HiShoppingCart size={24} />
                  {itemCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">{itemCount}</span>
                  )}
                </Link>
                {isAdmin && <Link to="/admin" className="text-amber-600 hover:text-amber-700 font-semibold transition">Admin</Link>}
                <div className="flex items-center gap-3">
                  <Link to="/profile" className="flex items-center gap-1 text-gray-600 hover:text-indigo-600 transition">
                    <HiUser size={20} /> <span className="text-sm">{user?.username}</span>
                  </Link>
                  <button onClick={handleLogout} className="text-sm text-red-500 hover:text-red-700 font-medium">Logout</button>
                </div>
              </>
            )}
            {!isAuthenticated && (
              <div className="flex gap-3">
                <Link to="/login" className="px-4 py-2 text-indigo-600 border border-indigo-600 rounded-lg hover:bg-indigo-50 transition font-medium text-sm">Login</Link>
                <Link to="/register" className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition font-medium text-sm">Register</Link>
              </div>
            )}
          </div>

          {/* Mobile menu button */}
          <button className="md:hidden text-gray-600" onClick={() => setOpen(!open)}>
            {open ? <HiX size={24} /> : <HiMenu size={24} />}
          </button>
        </div>
      </div>

      {/* Mobile Nav */}
      {open && (
        <div className="md:hidden border-t bg-white px-4 pb-4 space-y-2">
          <Link to="/products" className="block py-2 text-gray-700" onClick={() => setOpen(false)}>Products</Link>
          {isAuthenticated ? (
            <>
              <Link to="/cart" className="block py-2 text-gray-700" onClick={() => setOpen(false)}>Cart ({itemCount})</Link>
              <Link to="/orders" className="block py-2 text-gray-700" onClick={() => setOpen(false)}>Orders</Link>
              <Link to="/profile" className="block py-2 text-gray-700" onClick={() => setOpen(false)}>Profile</Link>
              {isAdmin && <Link to="/admin" className="block py-2 text-amber-600 font-semibold" onClick={() => setOpen(false)}>Admin Panel</Link>}
              <button onClick={() => { handleLogout(); setOpen(false); }} className="block py-2 text-red-500">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="block py-2 text-indigo-600" onClick={() => setOpen(false)}>Login</Link>
              <Link to="/register" className="block py-2 text-indigo-600" onClick={() => setOpen(false)}>Register</Link>
            </>
          )}
        </div>
      )}
    </nav>
  );
}

