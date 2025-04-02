<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">📜 Lịch sử mua hàng</h1>

    <!-- Hiển thị trạng thái loading -->
    <div v-if="orderStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <!-- Hiển thị lỗi nếu có -->
    <div v-if="orderStore.error" class="alert alert-danger text-center" role="alert">
      {{ orderStore.error }}
    </div>

    <!-- Hiển thị danh sách đơn hàng -->
    <div v-if="!orderStore.loading && orderStore.getOrders.length === 0" class="text-center text-muted">
      Chưa có đơn hàng nào.
    </div>
    <div v-else-if="orderStore.getOrders.length > 0" class="row">
      <div class="col-md-8 mx-auto">
        <div class="card mb-3" v-for="order in orderStore.getOrders" :key="order.id">
          <div class="card-body">
            <h5 class="fw-bold">Đơn hàng #{{ order.id }}</h5>
            <p class="text-muted">Ngày đặt: {{ formatDate(order.createdAt) }}</p>
            <p class="text-primary fw-bold">Tổng tiền: {{ calculateTotal(order).toLocaleString() }} VND</p>
            <p class="text-muted">Phương thức thanh toán: {{ order.paymentMethod === 'COD' ? 'Thanh toán COD' : 'Thanh toán trước' }}</p>
            <p class="text-muted">Trạng thái: {{ getStatusText(order.status) }}</p>
            <button class="btn btn-outline-primary btn-sm" @click="toggleDetails(order.id)">
              {{ selectedOrderId === order.id ? 'Ẩn chi tiết' : '🔍 Xem chi tiết' }}
            </button>

            <!-- Hiển thị chi tiết đơn hàng -->
            <div v-if="selectedOrderId === order.id" class="mt-3">
              <hr />
              <h6 class="fw-bold">Chi tiết đơn hàng</h6>
              <div v-if="order.orderDetails && order.orderDetails.length > 0">
                <div class="row mb-2" v-for="item in order.orderDetails" :key="item.id">
                  <div class="col-6">
                    <p class="mb-0">Sản phẩm: {{ item.productName || `Sản phẩm #${item.productId || item.id}` }}</p>
                    <p class="mb-0 text-muted">Số lượng: {{ item.quantity }}</p>
                  </div>
                  <div class="col-6 text-end">
                    <p class="mb-0 text-primary fw-bold">
                      {{ (item.price * item.quantity).toLocaleString() }} VND
                    </p>
                  </div>
                </div>
              </div>
              <div v-else class="text-muted">
                Không có sản phẩm nào trong đơn hàng.
              </div>

              <!-- Thông tin giao hàng -->
              <h6 class="fw-bold mt-3">Thông tin giao hàng</h6>
              <!-- <p class="mb-0">Tên người nhận: {{ order.address?.addressLine || 'Không có thông tin' }}, {{ order.address?.ward || '' }}, {{ order.address?.district || '' }}, {{ order.address?.city || '' }}</p> -->
              <p class="mb-0">Địa chỉ: {{ order.address?.addressLine || 'Không có thông tin' }}, {{ order.address?.ward || '' }}, {{ order.address?.district || '' }}, {{ order.address?.city || '' }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useOrderStore } from '@/stores/OrderStore';
import { useAuthStore } from '@/stores/AuthStore';

const orderStore = useOrderStore();
const authStore = useAuthStore();

const selectedOrderId = ref(null);

// Object ánh xạ trạng thái sang tiếng Việt
const statusMap = {
  reception: 'Tiếp nhận',
  prepare: 'Chuẩn bị',
  delivering: 'Đang giao',
  delivered: 'Đã giao',
  canceled: 'Đã hủy'
};

// Hàm lấy text trạng thái
const getStatusText = (status) => {
  return statusMap[status?.toLowerCase()] || 'Đang xử lý'; // Fallback nếu status không khớp
};

onMounted(async () => {
  if (authStore.isAuthenticated() && authStore.user) {
    await orderStore.fetchOrders();
  } else {
    orderStore.error = 'Vui lòng đăng nhập để xem lịch sử mua hàng';
  }
});

const formatDate = (dateString) => {
  if (!dateString) return 'Không có thông tin';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
  });
};

const calculateTotal = (order) => {
  if (!order.orderDetails || order.orderDetails.length === 0) return 0;
  return order.orderDetails.reduce((sum, item) => sum + (item.price * item.quantity), 0);
};

const toggleDetails = (orderId) => {
  if (selectedOrderId.value === orderId) {
    selectedOrderId.value = null;
  } else {
    selectedOrderId.value = orderId;
  }
};
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
.card-body {
  padding: 1rem;
}
.btn-sm {
  font-size: 0.8rem;
  padding: 0.25rem 0.5rem;
}
</style>