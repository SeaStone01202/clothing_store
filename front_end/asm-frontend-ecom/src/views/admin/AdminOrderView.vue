<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">📦 Quản lý đơn hàng</h1>

    <div v-if="adminStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>
    <div v-if="adminStore.error" class="alert alert-danger text-center" role="alert">
      {{ adminStore.error }}
    </div>

    <div class="card" v-if="!adminStore.loading && !adminStore.error">
      <div class="card-body">
        <!-- Vùng hiển thị chi tiết inline -->
        <div v-if="selectedOrder" class="mb-4 card">
          <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Chi tiết đơn hàng #{{ selectedOrder.id }}</h5>
          </div>
          <div class="card-body">
            <p><strong>Ngày đặt:</strong> {{ formatDate(selectedOrder.createdAt) }}</p>
            <p><strong>Phương thức thanh toán:</strong> {{ selectedOrder.paymentMethod }}</p>
            <p><strong>Trạng thái:</strong> {{ selectedOrder.status }}</p>
            <p><strong>Địa chỉ giao hàng:</strong> {{ formatAddress(selectedOrder.address) }}</p>
            <h6>Chi tiết sản phẩm:</h6>
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th>Tên sản phẩm</th>
                  <th>Số lượng</th>
                  <th>Giá</th>
                  <th>Tổng</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="detail in selectedOrder.orderDetails" :key="detail.id">
                  <td>{{ detail.productName }}</td>
                  <td>{{ detail.quantity }}</td>
                  <td>{{ detail.price.toLocaleString() }} VND</td>
                  <td>{{ (detail.price * detail.quantity).toLocaleString() }} VND</td>
                </tr>
              </tbody>
            </table>
            <p><strong>Tổng tiền:</strong> {{ calculateTotal(selectedOrder).toLocaleString() }} VND</p>
          </div>
        </div>

        <!-- Bảng danh sách đơn hàng -->
        <table class="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Ngày đặt</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in adminStore.getOrders" :key="order.id">
              <td>{{ order.id }}</td>
              <td>{{ formatDate(order.createdAt) }}</td>
              <td>{{ calculateTotal(order).toLocaleString() }} VND</td>
              <td>
                <select v-model="order.status" @change="updateOrderStatus(order)" class="form-select">
                  <option value="reception">Tiếp nhận</option>
                  <option value="prepare">Chuẩn bị</option>
                  <option value="delivering">Đang giao</option>
                  <option value="delivered">Đã giao</option>
                  <option value="canceled">Đã hủy</option>
                </select>
              </td>
              <td>
                <button
                  class="btn btn-outline-primary btn-sm"
                  @click="toggleDetails(order)"
                >
                  {{ selectedOrder && selectedOrder.id === order.id ? 'Ẩn chi tiết' : 'Xem chi tiết' }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Phân trang -->
        <nav aria-label="Page navigation" class="mt-3">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: adminStore.currentPage === 1 }">
              <button class="page-link" @click="changePage(adminStore.currentPage - 1)">Trước</button>
            </li>
            <li v-for="p in adminStore.totalPages" :key="p" class="page-item" :class="{ active: adminStore.currentPage === p }">
              <button class="page-link" @click="changePage(p)">{{ p }}</button>
            </li>
            <li class="page-item" :class="{ disabled: adminStore.currentPage === adminStore.totalPages }">
              <button class="page-link" @click="changePage(adminStore.currentPage + 1)">Sau</button>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/AuthStore';
import { useAdminStore } from '@/stores/AdminStore';

const authStore = useAuthStore();
const adminStore = useAdminStore();
const selectedOrder = ref(null);

onMounted(async () => {
  if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
    adminStore.error = 'Bạn không có quyền truy cập trang này';
    return;
  }

  await adminStore.fetchOrders(1); // Tải trang đầu tiên (1-based)
});

const formatDate = (dateString) => {
  if (!dateString) return 'Không có thông tin';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN');
};

const formatAddress = (address) => {
  if (!address) return 'Không có thông tin';
  return `${address.addressLine}, ${address.ward}, ${address.district}, ${address.city}`;
};

const calculateTotal = (order) => {
  return order.orderDetails.reduce((sum, item) => sum + (item.price * item.quantity), 0);
};

const updateOrderStatus = async (order) => {
  const success = await adminStore.updateOrderStatus(order.id, order.status);
  if (success) {
    console.log(`Cập nhật trạng thái đơn hàng ${order.id} thành ${order.status}`);
  }
};

const toggleDetails = (order) => {
  if (selectedOrder.value && selectedOrder.value.id === order.id) {
    selectedOrder.value = null;
  } else {
    selectedOrder.value = order;
  }
};

const changePage = async (page) => {
  if (page >= 1 && page <= adminStore.totalPages) {
    selectedOrder.value = null;
    await adminStore.fetchOrders(page);
  }
};
</script>

<style scoped>
.table {
  width: 100%;
}
.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
}
.card-header {
  background-color: #007bff;
}
</style>