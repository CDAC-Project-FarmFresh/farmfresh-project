import httpClient from '../api/httpClient';

const login = (data) => {
  return httpClient.post('auth/login', data);
};

const register = (data) => {
  return httpClient.post('auth/register', data);
};

const getCategory = () => {
  return httpClient.get('admin/categorylist');
};

const removeCategory = (categoryid) => {
  return httpClient.delete(`admin/categories/${categoryid}`);
};

const addCategory = (categoryName) => {
  return httpClient.get(`admin/addcategory/${encodeURIComponent(categoryName)}`);
};

const addProduct = (data, farmerId) => {
  return httpClient.post(`admin/newproduct/${farmerId}`, data);
};

const addProductImage = (file, id) => {
  return httpClient.post(`admin/${id}/image`, file);
};

const updateProduct = (id, formData) => {
  const q = new URLSearchParams();
  for (const [k, v] of formData.entries()) {
    q.append(k, v);
  }
  return httpClient.put(`admin/updateproduct/${id}?${q.toString()}`);
};

const orderDetails = () => {
  return httpClient.get('admin/allorders');
};

const getUsersList = () => {
  return httpClient.get('admin/allusers');
};

const getSpecificUserDetails = (id) => {
  return httpClient.get(`admin/userdetails/${id}`);
};

const updateUser = (id, data) => {
  return httpClient.post(`admin/updateuser/${id}`, data);
};

export default {
  login,
  register,
  getCategory,
  removeCategory,
  addCategory,
  addProduct,
  addProductImage,
  updateProduct,
  orderDetails,
  getUsersList,
  getSpecificUserDetails,
  updateUser,
};
