import { defineStore } from 'pinia';
import axiosInstance from '@/axios/axiosInstance';
import { useAuthStore } from '@/stores/AuthStore';

export const useAdminStore = defineStore('admin', {
  state: () => ({
    users: [],
    orders: [],
    totalOrders: 0,
    totalProductStock: 0,
    totalPages: 0,
    currentPage: 1, // 1-based
    loading: false,
    error: null,
  }),

  actions: {
    async fetchUsers() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || authStore.user?.role !== 'DIRECTOR') {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/user/all');
        if (response.data.status === 200) {
          this.users = response.data.data;
          console.log('Fetched users:', this.users);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải danh sách người dùng';
        console.error('Error fetching users:', error);
        this.users = [];
      } finally {
        this.loading = false;
      }
    },

    async updateUserRole(email, role) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || authStore.user?.role !== 'DIRECTOR') {
        this.error = 'Bạn không có quyền cập nhật vai trò';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { emailUserChangeRole: email, role };
        const response = await axiosInstance.put('/user/role', payload);
        if (response.data.status === 200 && response.data.data === true) {
          const user = this.users.find(u => u.email === email);
          if (user) user.role = role;
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi cập nhật vai trò';
        console.error('Error updating role:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async updateUserStatus(email, status) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || authStore.user?.role !== 'DIRECTOR') {
        this.error = 'Bạn không có quyền xóa người dùng';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { emailUserChangeRole: email, status };
        const response = await axiosInstance.put('/user/status', payload);
        if (response.data.status === 200 && response.data.data === true) {
          const user = this.users.find(u => u.email === email);
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi đổi trạng thái người dùng';
        console.error('Error deleting user:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async fetchOrders(page = 1) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get(`/orders/all?page=${page}`);
        if (response.data.status === 200) {
          this.orders = response.data.data.content;
          this.totalOrders = response.data.data.totalElements; // Có thể bỏ nếu dùng fetchOrderCount
          this.totalPages = response.data.data.totalPages;
          this.currentPage = page;
          console.log('Fetched orders:', this.orders);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải danh sách đơn hàng';
        console.error('Error fetching orders:', error);
        this.orders = [];
      } finally {
        this.loading = false;
      }
    },

    async fetchOrderCount() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/orders/countOrder');
        if (response.data.status === 200) {
          this.totalOrders = response.data.data;
          console.log('Total orders:', this.totalOrders);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải tổng số đơn hàng';
        console.error('Error fetching order count:', error);
        this.totalOrders = 0;
      } finally {
        this.loading = false;
      }
    },

    async fetchProductStock() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/product/countStock');
        if (response.data.status === 200) {
          this.totalProductStock = response.data.data;
          console.log('Total orders:', this.totalProductStock);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải tổng số hàng tồn kho';
        console.error('Error fetching order count:', error);
        this.totalOrders = 0;
      } finally {
        this.loading = false;
      }
    },

    async updateOrderStatus(orderId, status) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền cập nhật trạng thái đơn hàng';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { orderId, status };
        const response = await axiosInstance.put('/orders/edit', payload);
        if (response.data.status === 200 && response.data.data === true) {
          const order = this.orders.find(o => o.id === orderId);
          if (order) order.status = status;
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi cập nhật trạng thái đơn hàng';
        console.error('Error updating order status:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async fetchTodayRevenue() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/orders/countPrice');
        if (response.data.status === 200) {
          this.todayRevenue = response.data.data; // Lưu doanh thu từ API
          console.log('Today revenue:', this.todayRevenue);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải doanh thu hôm nay';
        console.error('Error fetching today revenue:', error);
        this.todayRevenue = 0;
      } finally {
        this.loading = false;
      }
    },

    async fetchCategories() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền truy cập chức năng này';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/category');
        if (response.data.status === 200) {
          this.categories = response.data.data;
          console.log('Fetched categories:', this.categories);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải danh sách danh mục';
        console.error('Error fetching categories:', error);
        this.categories = [];
      } finally {
        this.loading = false;
      }
    },

    async createCategory(name) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền thêm danh mục';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { name };
        const response = await axiosInstance.post('/category/create', payload);
        if (response.data.status === 200) {
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi thêm danh mục';
        console.error('Error creating category:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async updateCategory(id, name) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
        this.error = 'Bạn không có quyền cập nhật danh mục';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { id, name };
        const response = await axiosInstance.put('/category/update', payload);
        if (response.data.status === 200) {
          const category = this.categories.find(c => c.id === id);
          if (category) {
            category.name = name;
          }
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi cập nhật danh mục';
        console.error('Error updating category:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async changeCategoryStatus(id, name, status) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || authStore.user?.role !== 'DIRECTOR') {
        this.error = 'Bạn không có quyền thay đổi trạng thái danh mục';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const payload = { id, name, status };
        const response = await axiosInstance.put('/category/status', payload);
        if (response.data.status === 200) {
          const category = this.categories.find(c => c.id === id);
          if (category) {
            category.status = status;
          }
          return true;
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi thay đổi trạng thái danh mục';
        console.error('Error changing category status:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },


  },

  getters: {
    getUsers: (state) => state.users,
    getOrders: (state) => state.orders,
    getTotalOrders: (state) => state.totalOrders, // Thêm getter cho totalOrders
    getTotalProductStock: (state) => state.totalProductStock, // Thêm getter cho totalProductStock
    getTodayRevenue: (state) => state.todayRevenue,
    getCategories: (state) => state.categories,
  },
});