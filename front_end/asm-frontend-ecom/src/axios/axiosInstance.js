import axios from "axios";
import { useAuthStore } from "@/stores/AuthStore";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  // timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // 🔥 Đảm bảo gửi cookie chứa Refresh Token
});

let isRefreshing = false; // 🔥 Kiểm soát trạng thái refresh token
let failedQueue = []; // 🔥 Hàng đợi các request bị chặn khi refresh

// ✅ Interceptor: Thêm Access Token vào headers trước khi gửi request
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Interceptor: Xử lý lỗi 401 và tự động refresh token
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const authStore = useAuthStore();
    const originalRequest = error.config;

    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      if (!isRefreshing) {
        isRefreshing = true;
        try {
          await authStore.refreshAccessToken(); // ✅ Gọi API refresh token
          isRefreshing = false;

          // ✅ Retry lại tất cả các request bị chặn
          failedQueue.forEach((req) => req.resolve(axiosInstance(req.config)));
          failedQueue = [];

          // ✅ Gửi lại request ban đầu với token mới
          originalRequest.headers.Authorization = `Bearer ${authStore.accessToken}`;
          return axiosInstance(originalRequest);
        } catch (refreshError) {
          isRefreshing = false;
          failedQueue.forEach((req) => req.reject(refreshError));
          failedQueue = [];
          authStore.logout(); // ❌ Nếu refresh token lỗi, logout user
          return Promise.reject(refreshError);
        }
      } else {
        // Nếu đã có request đang refresh token, chờ nó hoàn thành
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject, config: originalRequest });
        });
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
