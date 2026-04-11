import httpClient from '../api/httpClient';

export const forgotPassword = (email) => httpClient.post('auth/forgot-password', { email });

export const resetPassword = (token, newPassword) =>
  httpClient.post('auth/reset-password', { token, newPassword });
