import { getApiBase } from '../api/httpClient';

export function productImageUrlById(productId) {
  return `${getApiBase()}/catalog/products/${productId}/image`;
}

export function productImageUrlByName(stockItemName) {
  const enc = encodeURIComponent(stockItemName);
  return `${getApiBase()}/catalog/products/by-name/${enc}/image`;
}
