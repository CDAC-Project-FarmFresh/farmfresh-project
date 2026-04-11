import axios from 'axios';

export function getApiBase() {
  const fromEnv = import.meta.env.VITE_API_BASE_URL;
  if (fromEnv && String(fromEnv).trim()) {
    return String(fromEnv).trim().replace(/\/$/, '');
  }
  return 'http://localhost:8080/FarmFresh';
}

const httpClient = axios.create({
  baseURL: `${getApiBase()}/`,
  headers: {
    'Content-Type': 'application/json',
  },
});

httpClient.interceptors.request.use((config) => {
  const raw = localStorage.getItem('farmfresh-auth');
  if (raw) {
    try {
      const { accessToken } = JSON.parse(raw);
      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    } catch {
      /* ignore */
    }
  }
  if (config.data instanceof FormData) {
    delete config.headers['Content-Type'];
  }
  return config;
});

httpClient.interceptors.response.use(
  (r) => r,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && original && !original._retry) {
      original._retry = true;
      const raw = localStorage.getItem('farmfresh-auth');
      if (raw) {
        try {
          const { refreshToken } = JSON.parse(raw);
          if (refreshToken) {
            const { data } = await axios.post(`${getApiBase()}/auth/refresh`, { refreshToken });
            localStorage.setItem('farmfresh-auth', JSON.stringify(data));
            original.headers.Authorization = `Bearer ${data.accessToken}`;
            return httpClient(original);
          }
        } catch {
          localStorage.removeItem('farmfresh-auth');
        }
      }
    }
    return Promise.reject(error);
  }
);

export default httpClient;
