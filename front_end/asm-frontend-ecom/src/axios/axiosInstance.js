import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // ✅ Để gửi Cookie chứa Refresh Token kèm request
});

// ✅ Interceptor: Tự động thêm Access Token từ localStorage vào headers
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken"); // 🔥 Lấy token từ localStorage
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Interceptor: Xử lý lỗi 401 và refresh token tự động
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      try {
        // 🚀 Gửi request làm mới Access Token
        const refreshResponse = await axios.post("http://localhost:8080/auth/system/refresh", {}, { withCredentials: true });

        if (refreshResponse.data.status === 200) {
          const newAccessToken = refreshResponse.data.data.accessToken;

          // ✅ Cập nhật Access Token mới vào localStorage
          localStorage.setItem("accessToken", newAccessToken);

          // ✅ Gửi lại request ban đầu với token mới
          error.config.headers.Authorization = `Bearer ${newAccessToken}`;
          return axiosInstance(error.config);
        }
      } catch (refreshError) {
        logout(); // 🔴 Xóa token & chuyển hướng đến trang login nếu refresh thất bại
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

// ✅ Hàm logout: Xóa token trong localStorage
const logout = () => {
  localStorage.removeItem("accessToken");
  window.location.href = "/login"; // 🔥 Chuyển hướng về trang đăng nhập
};

export default axiosInstance;
