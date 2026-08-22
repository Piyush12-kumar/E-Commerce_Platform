import { createContext, useContext, useState, useEffect } from 'react';
import API from '../api/axios';
import { parseJwt } from '../utils/formatters';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [loading, setLoading] = useState(true);

  const roles = user?.roles || [];
  const isAdmin = roles.includes('ADMIN');
  const isAuthenticated = !!token;

  useEffect(() => {
    if (token) {
      const decoded = parseJwt(token);
      if (decoded && decoded.exp * 1000 > Date.now()) {
        setUser({ username: decoded.sub, roles: decoded.roles || [] });
      } else {
        logout();
      }
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    const res = await API.post('/users/login', { username, password });
    const jwt = res.data;
    localStorage.setItem('token', jwt);
    setToken(jwt);
    const decoded = parseJwt(jwt);
    setUser({ username: decoded.sub, roles: decoded.roles || [] });
    return decoded;
  };

  const register = async (data) => {
    const res = await API.post('/users/register', data);
    return res.data;
  };

  const logout = async () => {
    try { await API.get('/users/logout'); } catch {}
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, roles, isAdmin, isAuthenticated, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

