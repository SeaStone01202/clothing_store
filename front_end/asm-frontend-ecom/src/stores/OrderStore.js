import { defineStore } from 'pinia';
import axiosInstance from '@/axios/axiosInstance';
import { useAuthStore } from '@/stores/AuthStore';

export const useOrderStore = defineStore('order', {
  state: () => ({
    orders: [],
    loading: false,
    error: null,
  }),

  actions: {
    async createOrder(orderData) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !authStore.user) {
        this.error = 'Vui lòng đăng nhập để đặt hàng';
        return { success: false, message: this.error };
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.post('/orders/create', orderData);
        if (response.data.status === 200) {
          return { success: true, message: response.data.data || 'Đặt hàng thành công!' };
        } else {
          throw new Error(response.data.message || 'Không thể tạo đơn hàng');
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message || 'Đã có lỗi xảy ra khi tạo đơn hàng';
        console.error('Error creating order:', error);
        return { success: false, message: this.error };
      } finally {
        this.loading = false;
      }
    },

    async fetchOrders() { // Xóa tham số email vì backend lấy từ token
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !authStore.user) {
        this.error = 'Vui lòng đăng nhập để xem đơn hàng';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/orders/me'); // Gọi đúng endpoint
        if (response.data.status === 200) {
          this.orders = response.data.data || [];
        } else {
          throw new Error(response.data.message || 'Không thể lấy danh sách đơn hàng');
        }
      } catch (error) {
        this.error = error.response?.data?.message || error.message || 'Đã có lỗi xảy ra khi lấy danh sách đơn hàng';
        console.error('Error fetching orders:', error);
        this.orders = [];
      } finally {
        this.loading = false;
      }
    },
  },

  getters: {
    getOrders(state) {
      return state.orders;
    },
  },
});