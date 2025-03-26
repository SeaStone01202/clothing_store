// src/stores/UserStore.js
import { defineStore } from "pinia";
import axiosInstance from "@/axios/axiosInstance";

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