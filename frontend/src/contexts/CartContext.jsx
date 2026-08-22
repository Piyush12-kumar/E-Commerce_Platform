import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import API from '../api/axios';
import { useAuth } from './AuthContext';
import toast from 'react-hot-toast';

const CartContext = createContext();
export const useCart = () => useContext(CartContext);

export function CartProvider({ children }) {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);

  const items = cart?.items || [];
  const itemCount = items.reduce((sum, i) => sum + i.quantity, 0);
  const total = items.reduce((sum, i) => sum + (i.price * i.quantity), 0);

  const fetchCart = useCallback(async () => {
    if (!isAuthenticated) return;
    try {
      setLoading(true);
      const res = await API.get('/cart/items');
      setCart(res.data);
    } catch { setCart(null); }
    finally { setLoading(false); }
  }, [isAuthenticated]);

  useEffect(() => { fetchCart(); }, [fetchCart]);

  const addToCart = async (productId) => {
    const res = await API.post(`/cart/add?productId=${productId}`);
    setCart(res.data);
    toast.success('Added to cart!');
  };

  const removeFromCart = async (productId) => {
    const res = await API.delete(`/cart/remove/${productId}`);
    setCart(res.data);
    toast.success('Quantity decreased');
  };

  const removeItemFromCart = async (productId) => {
    const res = await API.delete(`/cart/remove-item/${productId}`);
    setCart(res.data);
    toast.success('Removed from cart');
  };

  const clearCart = async () => {
    const res = await API.delete('/cart/clear');
    setCart(res.data);
  };

  return (
    <CartContext.Provider value={{ cart, items, itemCount, total, loading, addToCart, removeFromCart, removeItemFromCart, clearCart, fetchCart }}>
      {children}
    </CartContext.Provider>
  );
}

