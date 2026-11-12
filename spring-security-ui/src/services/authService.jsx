import axios from "axios";

const API_BASE_URL = "http://localhost:8181";
const TOKEN_KEY = "token";

export const getToken = () => localStorage.getItem(TOKEN_KEY);

export const setToken = (token) => {
  localStorage.setItem(TOKEN_KEY, token);
};

export const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY);
};

export const login = async (username, password) => {
  clearToken();
  const response = await axios.post(`${API_BASE_URL}/api/auth/login`, {
    username,
    password,
  });

  const token = response?.data?.token;
  if (token) {
    setToken(token);
  }

  return response.data;
};

export const register = async (user) => {
  const response = await axios.post(`${API_BASE_URL}/api/auth/register`, user);
  return response.data;
};

axios.interceptors.request.use(
  (config) => {
    const token = getToken();
    const isAuthEndpoint =
      config.url?.includes("/api/auth/login") ||
      config.url?.includes("/api/auth/register");

    if (token && !isAuthEndpoint) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
