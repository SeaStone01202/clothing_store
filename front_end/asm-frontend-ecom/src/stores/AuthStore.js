import { defineStore } from "pinia";
import axiosInstance from "@/axios/axiosInstance";
import Cookies from "js-cookie";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    accessToken: localStorage.getItem("accessToken") || null, // ✅ Chỉ lưu accessToken
    user: null, // ❌ Không lưu user vào localStorage
  }),

  actions: {
    /**
     * ✅ Cập nhật Access Token vào localStorage & axios header
     */
    setAccessToken(token) {
      this.accessToken = token;
      localStorage.setItem("accessToken", token);
      axiosInstance.defaults.headers.common["Authorization"] = `Bearer ${token}`;
      this.fetchUserInfo(); // 🔥 Gọi API lấy thông tin user ngay khi có token
    },

    /**
     * ✅ API đăng nhập: `/auth/system/login`
     */
    async login(email, password) {
      try {
        const response = await axiosInstance.post("/auth/system/login", { email, password });

        if (response.data.status === 200) {
          this.setAccessToken(response.data.data.accessToken);
          return { success: true, message: "Đăng nhập thành công!" };
        } else {
          return { success: false, message: response.data.message };
        }
      } catch (error) {
        return { success: false, message: "Sai tài khoản hoặc mật khẩu!" };
      }
    },

    /**
     * ✅ API làm mới Access Token: `/auth/system/refresh`
     */
    async refreshAccessToken() {
      try {
        const response = await axiosInstance.post("/auth/system/refresh", {}, { withCredentials: true });

        if (response.data.status === 200) {
          this.setAccessToken(response.data.data.accessToken);
        } else {
          this.logout();
        }
      } catch (error) {
        this.logout();
      }
    },

    /**
     * ✅ API đăng xuất: `/auth/system/logout`
     */
    async logout() {
      try {
        const refreshToken = Cookies.get("refreshToken"); // ✅ Lấy Refresh Token từ Cookie

        if (!refreshToken) {
          console.warn("⚠️ Không tìm thấy Refresh Token, thực hiện logout local!");
          this.clearAuthData();
          return;
        }

        const response = await axiosInstance.post("/auth/system/logout", { refreshToken }, { withCredentials: true });

        if (response.data.status === 200) {
          console.log("✅ Đăng xuất thành công từ server!");
        } else {
          console.error("❌ Đăng xuất thất bại từ server:", response.data.message);
        }
      } catch (error) {
        console.error("⚠️ Lỗi khi gửi request logout:", error);
      } finally {
        this.clearAuthData();
      }
    },

    /**
     * ✅ Lấy thông tin user từ API: `/auth/system/me`
     */
    async fetchUserInfo() {
      if (!this.accessToken) return;
      try {
        const response = await axiosInstance.get("/auth/system/me");
        if (response.data.status === 200) {
          this.user = response.data.data; // ✅ Chỉ lưu user vào state, không lưu vào localStorage
          console.log("User info:", this.user);
        }
      } catch (error) {
        console.error("Không thể lấy thông tin user:", error);
      }
    },

    /**
     * ✅ Xóa Access Token khỏi localStorage & Pinia
     */
    clearAuthData() {
      this.accessToken = null;
      this.user = null;
      
      localStorage.removeItem("accessToken"); // ✅ Xóa accessToken
      Cookies.remove("refreshToken"); // ✅ Xóa refreshToken

      delete axiosInstance.defaults.headers.common["Authorization"];

      const router = useRouter();
      router.push("/login"); // ✅ Chuyển hướng về trang đăng nhập
    },

    /**
     * ✅ Kiểm tra trạng thái đăng nhập
     */
    isAuthenticated() {
      return !!this.accessToken; // ✅ Chỉ kiểm tra accessToken, không dùng localStorage nữa
    },
  },
});
