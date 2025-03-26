// src/stores/UserStore.js
import { defineStore } from "pinia";
import axiosInstance from "@/axios/axiosInstance";
import { useAuthStore } from "./AuthStore"; // Thêm import AuthStore

export const useUserStore = defineStore("user", {
  state: () => ({
    user: null, // Lưu thông tin người dùng
    error: null, // Lưu thông báo lỗi nếu có
  }),

  actions: {
    async registerUser({ email, password, fullname }) {
      try {
        const response = await axiosInstance.post("/user/register", {
          email,
          password,
          fullname,
        });

        if (response.data.status === 200) {
          this.user = response.data.data; // Lưu thông tin người dùng
          this.error = null;
          return { success: true, message: "Đăng ký thành công!" };
        } else {
          this.error = response.data.message;
          return { success: false, message: response.data.message };
        }
      } catch (error) {
        console.error("❌ Lỗi khi đăng ký:", error);
        this.error = error.response?.data?.message || "Đăng ký thất bại!";
        return { success: false, message: this.error };
      }
    },

    async updateUser({ email, fullname, phone, image }) {
      try {
        const formData = new FormData();
        formData.append("email", email);
        formData.append("fullname", fullname);
        formData.append("phone", phone);
        if (image) {
          formData.append("image", image);
        }

        const response = await axiosInstance.put("/user/update", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        // Cập nhật state với dữ liệu từ backend
        this.user = response.data;
        this.error = null;
        return { success: true, message: "Cập nhật hồ sơ thành công!" };
      } catch (error) {
        console.error("❌ Lỗi khi cập nhật hồ sơ:", error);
        this.error = error.response?.data?.message || "Cập nhật thất bại!";
        return { success: false, message: this.error };
      }
    },

    async addAddress({ addressLine, ward, district, city, isDefault }) {
      const authStore = useAuthStore();
      try {
        const email = authStore.user?.email;
        if (!email) {
          throw new Error("Không tìm thấy thông tin người dùng đăng nhập!");
        }
        const payload = {
          addressLine,
          ward,
          district,
          city,
          isDefault,
          email,
        };
        console.log("Dữ liệu gửi lên backend:", payload); // Debug ở đây
        const response = await axiosInstance.post("/address/create", payload);
        this.user.addresses = this.user.addresses || [];
        this.user.addresses.push(response.data);
        return { success: true, message: "Thêm địa chỉ thành công!" };
      } catch (error) {
        console.error("❌ Lỗi khi thêm địa chỉ:", error);
        this.error = error.response?.data?.message || "Thêm địa chỉ thất bại!";
        return { success: false, message: this.error };
      }
    },

    clearError() {
      this.error = null;
    },

    setUser(userData) {
      this.user = userData;
    },
  },

  getters: {
    getUser: (state) => state.user,
  },
});