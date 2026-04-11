import httpClient from '../api/httpClient';

const farmersList = () => {
  return httpClient.get('farmer/list');
};

const removeFarmer = (id) => {
  return httpClient.delete(`admin/farmers/${id}`);
};

const addFarmer = (data) => {
  return httpClient.post('admin/newfarmer', data);
};

const updateFarmer = (data, farmerid) => {
  return httpClient.post(`admin/updatefarmer/${farmerid}`, data);
};

const getFarmerDetails = (farmerid) => {
  return httpClient.get(`farmer/farmerdetails/${farmerid}`);
};

const getProductList = () => {
  return httpClient.get('farmer/allproducts');
};

const removeProduct = (id) => {
  return httpClient.delete(`admin/products/${id}`);
};

const getSpecificProductDetails = (farmerid, productid) => {
  return httpClient.get(`farmer/products/${farmerid}/${productid}`);
};

export default {
  farmersList,
  removeFarmer,
  addFarmer,
  updateFarmer,
  getFarmerDetails,
  getProductList,
  removeProduct,
  getSpecificProductDetails,
};
