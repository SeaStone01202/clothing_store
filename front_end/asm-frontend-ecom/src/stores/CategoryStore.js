import { defineStore } from "pinia";
import axiosInstance from "@/axios/axios";

export const useCategories = defineStore("categories", {
  state: () => ({
    categories: [],
    loading: false,
    error: null,
    totalPage: 1,
  }),
  actions: {
    async getAll() {
      try {
        const response = await axiosInstance.get(`/store/categories`);
        if (response.data.code === 0) return response.data.result;
      } catch (error) {
        console.warn("Lỗi API {}", error);
        return null;
      }
    },

    async retrieveCategories(page = 1) {
      // Đảm bảo page luôn bắt đầu từ 1
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get(
          `/store/categories/pages?page=${page}`,
          {
            params: {
              page: page,
            },
          }
        );
        if (response.data) {
          this.categories = response.data.content;
          this.totalPages = response.data.totalPages;
          return true;
        } else {
          this.error = "Không có danh mục";
          return false;
        }
      } catch (error) {
        console.error("Lỗi API:", error);
        this.error = "Lỗi kết nối đến máy chủ!";
        return false;
      } finally {
        this.loading = false;
      }
    },
    async createCategories(data) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.post("/store/categories", data);
        if (response.data.code === 0) {
          return { success: true, message: "Thêm danh mục thành công!" };
        } else {
          return {
            success: false,
            message: response.data.message || "Lỗi không xác định!",
          };
        }
      } catch (error) {
        if (error.response && error.response.status === 500) {
          return { success: false, message: "Danh mục đã tồn tại!" };
        }
        return { success: false, message: "Lỗi kết nối đến máy chủ!" };
      } finally {
        this.loading = false;
      }
    },
    async getCategoryById(categoryId) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get(
          `/store/categories/${categoryId}`
        );
        if (response.data.code === 0) {
          return response.data.result; // Trả về category tìm được
        } else {
          this.error = response.data.message;
          return null;
        }
      } catch (error) {
        console.error("Lỗi API: {}", error);
        this.error = "Lỗi kết nối đến máy chủ!";
        return null;
      } finally {
        this.loading = false;
      }
    },
    async updateCategory(categoryId, data) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.put(
          `/store/categories/${categoryId}`,
          data
        );
        if (response.data.code === 0) {
          return response.data.result;
        } else {
          this.error = response.data.message;
          return null;
        }
      } catch (error) {
        console.error("Lỗi API:", error);
        this.error = "Lỗi kết nối đến máy chủ!";
        return null;
      } finally {
        this.loading = false;
      }
    },
  },
});
