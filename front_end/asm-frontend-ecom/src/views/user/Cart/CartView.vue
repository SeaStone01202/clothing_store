<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">🛒 Giỏ hàng của bạn</h1>

    <!-- Hiển thị trạng thái loading -->
    <div v-if="cartStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <!-- Hiển thị lỗi nếu có -->
    <div v-if="cartStore.error" class="alert alert-danger text-center" role="alert">
      {{ cartStore.error }}
    </div>

    <!-- Hiển thị giỏ hàng -->
    <div v-if="!cartStore.loading && (!cartStore.cart || !cartStore.cart.cartDetails || cartStore.cart.cartDetails.length === 0)" class="text-center text-muted">
      Chưa có sản phẩm nào trong giỏ hàng.
    </div>
    <div v-else-if="cartStore.cart && cartStore.cart.cartDetails && cartStore.cart.cartDetails.length > 0" class="row">
      <div class="col-md-8">
        <div class="card mb-3 mx-auto" v-for="item in cartStore.cart.cartDetails" :key="item.id" style="max-width: 250px;">
          <div class="text-center">
            <img
              :src="item.productImageUrl || 'https://placehold.co/300x300?text=No+Image'"
              class="img-fluid rounded"
              alt="Sản phẩm"
              @error="handleImageError"
              style="max-width: 120px; max-height: 120px;"
            />
          </div>
          <div class="card-body">
            <h6 class="card-title text-center">{{ item.productName }}</h6>
            <p class="card-text text-primary fw-bold text-center">
              {{ item.productPrice ? item.productPrice.toLocaleString() : '0' }} VND
            </p>
            <div class="d-flex justify-content-end align-items-center">
              <button class="btn btn-outline-secondary btn-sm me-1" @click="decreaseQuantity(item.id)" :disabled="item.quantity <= 1">-</button>
              <span class="me-1">Số lượng: {{ item.quantity }}</span>
              <button class="btn btn-outline-secondary btn-sm me-1" @click="increaseQuantity(item.id)">+</button>
              <button class="btn btn-danger btn-sm" @click="cartStore.removeFromCart(item.id)">🗑</button>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card p-3">
          <h5 class="fw-bold">Tổng tiền: {{ cartStore.totalPrice ? cartStore.totalPrice.toLocaleString() : '0' }} VND</h5>
          <button class="btn btn-success w-100">🛍 Thanh toán</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useCartStore } from '@/stores/CartStore';
import { useAuthStore } from '@/stores/AuthStore';

const cartStore = useCartStore();
const authStore = useAuthStore();

onMounted(async () => {
  if (authStore.isAuthenticated() && authStore.user) {
    await cartStore.fetchCart();
  } else {
    cartStore.error = 'Vui lòng đăng nhập để xem giỏ hàng';
  }
});

const handleImageError = (event) => {
  event.target.src = 'https://placehold.co/300x300?text=No+Image';
};

const decreaseQuantity = async (cartDetailId) => {
  await cartStore.decreaseQuantity(cartDetailId);
};

const increaseQuantity = async (cartDetailId) => {
  await cartStore.increaseQuantity(cartDetailId);
};
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
.card-body {
  padding: 0.75rem;
}
.card-title {
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}
.card-text {
  font-size: 0.8rem;
  margin-bottom: 0.25rem;
}
.btn-sm {
  font-size: 0.7rem;
  padding: 0.2rem 0.4rem;
}
</style>