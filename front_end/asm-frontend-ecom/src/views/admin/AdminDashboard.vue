<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">📊 Bảng điều khiển Admin</h1>

    <!-- Thông báo lỗi hoặc loading -->
    <div v-if="adminStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>
    <div v-if="adminStore.error" class="alert alert-danger text-center" role="alert">
      {{ adminStore.error }}
    </div>

    <!-- Chào mừng và vai trò -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="fw-bold">Xin chào, {{ user.fullName || 'Admin' }}</h5>
        <p class="text-muted">Vai trò: {{ user.role === 'DIRECTOR' ? 'Giám đốc' : 'Nhân viên' }}</p>
      </div>
    </div>

    <!-- Thống kê nhanh -->
    <div class="row">
      <div class="col-md-4 mb-3">
        <div class="card text-center">
          <div class="card-body">
            <h6 class="fw-bold text-primary">Tổng đơn hàng</h6>
            <p class="display-6">{{ adminStore.getTotalOrders }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-3">
        <div class="card text-center">
          <div class="card-body">
            <h6 class="fw-bold text-success">Doanh thu hôm nay</h6>
            <p class="display-6">{{ adminStore.getTodayRevenue }} VND</p>
          </div>
        </div>
      </div>
      <div class="col-md-4 mb-3">
        <div class="card text-center">
          <div class="card-body">
            <h6 class="fw-bold text-warning">Sản phẩm tồn kho</h6>
            <p class="display-6">{{ adminStore.getTotalProductStock }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Điều hướng nhanh -->
    <div class="row mt-4">
      <div class="col-md-6 mb-3">
        <router-link to="/AdminOrderView" class="btn btn-primary w-100">
          Quản lý đơn hàng
        </router-link>
      </div>
      <div class="col-md-6 mb-3">
        <router-link to="/AdminProductView" class="btn btn-primary w-100">
          Quản lý sản phẩm
        </router-link>
      </div>
      <div class="col-md-6 mb-3" v-if="user.role === 'DIRECTOR'">
        <router-link to="/AdminUserView" class="btn btn-primary w-100">
          Quản lý người dùng
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/AuthStore';
import { useAdminStore } from '@/stores/AdminStore';

const authStore = useAuthStore();
const adminStore = useAdminStore();
const user = authStore.user || {};
const stats = { revenueToday: 0, stock: 0 }; // Giữ revenueToday và stock tĩnh tạm thời

onMounted(async () => {
  if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
    adminStore.error = 'Bạn không có quyền truy cập trang này';
    return;
  }

  await adminStore.fetchOrderCount(); // Lấy tổng số đơn hàng từ API
  await adminStore.fetchProductStock(); // Lấy tổng số đơn hàng từ API
  adminStore.fetchTodayRevenue(); // Lấy doanh thu hôm nay từ API
});
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
</style>