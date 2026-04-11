import httpClient from '../api/httpClient';

const addToCart = (id, qty) => {
  return httpClient.post(`user/addtocart/${id}`, null, { params: { qty } });
};

const checkOut = () => {
  return httpClient.get('user/cart');
};

const removeItem = (lineId) => {
  return httpClient.delete(`user/cart/lines/${lineId}`);
};

const placeOrder = (paid = true) => {
  return httpClient.post('user/placeorder', null, { params: { paid } });
};

const orders = () => {
  return httpClient.get('user/orders');
};

const getProfile = () => {
  return httpClient.get('user/profile');
};

const updateProfile = (data) => {
  return httpClient.put('user/profile', data);
};

const requestPhoneVerificationCode = () => {
  return httpClient.post('user/phone/request-code');
};

const confirmPhoneVerification = (code) => {
  return httpClient.post('user/phone/confirm', { code });
};

export default {
  addToCart,
  checkOut,
  removeItem,
  placeOrder,
  orders,
  getProfile,
  updateProfile,
  requestPhoneVerificationCode,
  confirmPhoneVerification,
};
