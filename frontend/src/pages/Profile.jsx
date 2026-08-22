import { useEffect, useState } from 'react';
import API from '../api/axios';
import toast from 'react-hot-toast';
import { HiUser, HiMail, HiPhone, HiPencil, HiCheck, HiX, HiLocationMarker, HiShoppingBag } from 'react-icons/hi';
import { Link } from 'react-router-dom';

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState({ username: '', email: '', phoneNumber: '' });
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      setLoading(true);
      const res = await API.get('/users/profile');
      setProfile(res.data);
      setForm({
        username: res.data.username || '',
        email: res.data.email || '',
        phoneNumber: res.data.phoneNumber || '',
      });
    } catch (err) {
      toast.error('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const res = await API.put('/users/update', form);
      setProfile(res.data);
      setEditing(false);
      toast.success('Profile updated successfully!');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  };

  const cancelEdit = () => {
    setForm({
      username: profile.username || '',
      email: profile.email || '',
      phoneNumber: profile.phoneNumber || '',
    });
    setEditing(false);
  };

  if (loading) {
    return (
      <div className="flex justify-center py-20">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600" />
      </div>
    );
  }

  if (!profile) {
    return <div className="text-center py-20 text-gray-500 text-xl">Failed to load profile</div>;
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-gray-800 mb-8">My Profile</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        {/* Profile Card */}
        <div className="md:col-span-1">
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 text-center">
            <div className="w-24 h-24 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-full mx-auto flex items-center justify-center">
              <span className="text-3xl font-bold text-white">
                {profile.username?.charAt(0)?.toUpperCase() || 'U'}
              </span>
            </div>
            <h2 className="text-xl font-bold text-gray-800 mt-4">{profile.username}</h2>
            <p className="text-sm text-gray-500 mt-1">{profile.email}</p>
            <div className="flex flex-wrap justify-center gap-2 mt-3">
              {profile.roles?.map(role => (
                <span key={role} className="text-xs bg-indigo-50 text-indigo-600 px-3 py-1 rounded-full font-medium">
                  {role}
                </span>
              ))}
            </div>

            <div className="mt-6 space-y-2">
              <Link to="/orders" className="flex items-center gap-2 text-gray-600 hover:text-indigo-600 transition text-sm justify-center">
                <HiShoppingBag size={16} /> My Orders
              </Link>
            </div>
          </div>
        </div>

        {/* Profile Details */}
        <div className="md:col-span-2">
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-xl font-bold text-gray-800">Profile Information</h2>
              {!editing ? (
                <button
                  onClick={() => setEditing(true)}
                  className="flex items-center gap-1 text-indigo-600 hover:text-indigo-700 font-medium text-sm transition"
                >
                  <HiPencil size={16} /> Edit
                </button>
              ) : (
                <button
                  onClick={cancelEdit}
                  className="flex items-center gap-1 text-gray-500 hover:text-gray-700 font-medium text-sm transition"
                >
                  <HiX size={16} /> Cancel
                </button>
              )}
            </div>

            {!editing ? (
              <div className="space-y-5">
                <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                  <div className="p-2 bg-indigo-100 rounded-lg">
                    <HiUser className="text-indigo-600" size={20} />
                  </div>
                  <div>
                    <p className="text-xs text-gray-400 uppercase tracking-wide">Username</p>
                    <p className="text-gray-800 font-medium">{profile.username}</p>
                  </div>
                </div>

                <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                  <div className="p-2 bg-indigo-100 rounded-lg">
                    <HiMail className="text-indigo-600" size={20} />
                  </div>
                  <div>
                    <p className="text-xs text-gray-400 uppercase tracking-wide">Email</p>
                    <p className="text-gray-800 font-medium">{profile.email || 'Not set'}</p>
                  </div>
                </div>

                <div className="flex items-center gap-4 p-4 bg-gray-50 rounded-xl">
                  <div className="p-2 bg-indigo-100 rounded-lg">
                    <HiPhone className="text-indigo-600" size={20} />
                  </div>
                  <div>
                    <p className="text-xs text-gray-400 uppercase tracking-wide">Phone Number</p>
                    <p className="text-gray-800 font-medium">{profile.phoneNumber || 'Not set'}</p>
                  </div>
                </div>

                {profile.addresses && profile.addresses.length > 0 && (
                  <div className="flex items-start gap-4 p-4 bg-gray-50 rounded-xl">
                    <div className="p-2 bg-indigo-100 rounded-lg">
                      <HiLocationMarker className="text-indigo-600" size={20} />
                    </div>
                    <div>
                      <p className="text-xs text-gray-400 uppercase tracking-wide">Addresses</p>
                      {profile.addresses.map((addr, idx) => (
                        <p key={idx} className="text-gray-800 font-medium text-sm mt-1">
                          {addr.street}, {addr.city}, {addr.state} {addr.zipCode}
                        </p>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            ) : (
              <form onSubmit={handleUpdate} className="space-y-5">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                  <input
                    type="text"
                    value={form.username}
                    onChange={(e) => setForm({ ...form, username: e.target.value })}
                    className="w-full border border-gray-200 rounded-xl px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    value={form.email}
                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                    className="w-full border border-gray-200 rounded-xl px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                  <input
                    type="tel"
                    value={form.phoneNumber}
                    onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
                    className="w-full border border-gray-200 rounded-xl px-4 py-3 outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition"
                    placeholder="Enter your phone number"
                  />
                </div>

                <button
                  type="submit"
                  disabled={saving}
                  className="flex items-center gap-2 bg-indigo-600 text-white px-6 py-3 rounded-xl hover:bg-indigo-700 transition font-semibold disabled:opacity-50"
                >
                  <HiCheck size={18} /> {saving ? 'Saving...' : 'Save Changes'}
                </button>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}



