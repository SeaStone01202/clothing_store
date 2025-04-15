// src/stores/UserStore.js
import { defineStore } from "pinia";
import axiosInstance from "@/axios/axiosInstance";
import { useAuthStore } from "./AuthStore";

export const useUserStore = defineStore("user", {
  state: () => ({
    user: null, // Lưu thông tin người dùng
    addresses: [], // Lưu danh sách địa chỉ
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
          this.user = response.data.data;
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
      const authStore = useAuthStore();
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

        if (response.data.status === 200) {
          this.user = response.data.data;
          authStore.user = this.user;
          this.error = null;
          return { success: true, message: "Cập nhật hồ sơ thành công!" };
        } else {
          this.error = response.data.message;
          return { success: false, message: response.data.message };
        }
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
        console.log("Dữ liệu gửi lên backend:", payload);
        const response = await axiosInstance.post("/address/create", payload);

        if (response.data.status === 200) {
          this.error = null;
          return { success: true, message: "Thêm địa chỉ thành công!" };
        } else {
          this.error = response.data.message;
          return { success: false, message: response.data.message };
        }
      } catch (error) {
        console.error("❌ Lỗi khi thêm địa chỉ:", error);
        this.error = error.response?.data?.message || "Thêm địa chỉ thất bại!";
        return { success: false, message: this.error };
      }
    },

    async fetchAddresses(email) {
      try {
        const response = await axiosInstance.get(`/address?email=${email}`);
        if (response.data.status === 200) {
          this.addresses = response.data.data;
          this.error = null;
        } else {
          this.error = response.data.message;
        }
      } catch (error) {
        console.error("❌ Lỗi khi lấy danh sách địa chỉ:", error);
        this.error = error.response?.data?.message || "Lấy danh sách địa chỉ thất bại!";
      }
    },

    async forgotPassword(email) {
      try {
        const response = await axiosInstance.post("/user/forgot_password", { email });
        if (response.data.status === 200) {
          this.error = null;
          return { success: true, message: "Đã gửi email khôi phục mật khẩu!" };
        } else {
          this.error = response.data.message;
          return { success: false, message: response.data.message };
        }
      } catch (error) {
        console.error("❌ Lỗi khi gửi email khôi phục mật khẩu:", error);
        this.error = error.response?.data?.message || "Gửi email khôi phục mật khẩu thất bại!";
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
    getAddresses: (state) => state.addresses,
  },
});