import { defineStore } from "pinia";
import axiosInstance from "@/axios/axiosInstance";
import Cookies from "js-cookie";
import getDeviceFingerprint from "@/utils/device";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    accessToken: localStorage.getItem("accessToken") || null,
    user: null,
  }),

  actions: {
    setAccessToken(token) {
      this.accessToken = token;
      localStorage.setItem("accessToken", token);
      axiosInstance.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      this.fetchUserInfo(); // Gọi API lấy thông tin user
    },

    async login(email, password) {
      try {
        const deviceId = await getDeviceFingerprint();
        const response = await axiosInstance.post("/auth/system/login", { email, password, deviceId });

        if (response.data.status === 200) {
          this.setAccessToken(response.data.data.accessToken);
          return { success: true, message: "Đăng nhập thành công!" };
        } else {
          return { success: false, message: response.data.message };
        }
      } catch (error) {
        console.error("❌ Lỗi khi gửi request đăng nhập:", error);
        return { success: false, message: "Sai tài khoản hoặc mật khẩu!" };
      }
    },

    async refreshAccessToken() {
      try {
        const response = await axiosInstance.post("/auth/system/refresh", {}, { withCredentials: true });
        if (response.data.status === 200) {
          this.setAccessToken(response.data.data.accessToken);
        } else {
          console.error("❌ Refresh thất bại:", response.data);
          this.logout();
        }
      } catch (error) {
        console.error("❌ Làm mới Access Token thất bại:", error);
        this.logout();
      }
    },

    async logout() {
      try {
        const response = await axiosInstance.post("/auth/system/logout", {}, { withCredentials: true });
        if (response.data.status !== 200) {
          console.error("❌ Đăng xuất thất bại từ server:", response.data.message);
        }
      } catch (error) {
        console.error("⚠️ Lỗi khi gửi request logout:", error);
      } finally {
        this.clearAuthData();
      }
    },

    async fetchUserInfo() {
      if (this.accessToken === null) {
        await this.refreshAccessToken();
      }
      if (!this.accessToken) {
        this.logout();
        return;
      }

      try {
        const response = await axiosInstance.get("/auth/system/me");
        if (response.data.status === 200) {
          this.user = response.data.data;
        } else {
          console.error("❌ Lấy thông tin user thất bại:", response.data.message);
          this.logout();
        }
      } catch (error) {
        console.error("❌ Lỗi khi lấy thông tin user:", error);
        this.logout();
      }
    },

    clearAuthData() {
      this.accessToken = null;
      this.user = null;
      localStorage.removeItem("accessToken");
      Cookies.remove("refreshToken");
      delete axiosInstance.defaults.headers.common["Authorization"];
    },

    isAuthenticated() {
      const refreshToken = Cookies.get("refreshToken");
      const hasToken = !!this.accessToken || !!refreshToken;
      return hasToken && !!this.user; // Kiểm tra cả user
    },
  },
});