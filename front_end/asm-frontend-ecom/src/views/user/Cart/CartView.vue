<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">🛒 Giỏ hàng của bạn</h1>
    <div v-if="cart.length === 0" class="text-center text-muted">Chưa có sản phẩm nào trong giỏ hàng.</div>
    <div v-else class="row">
      <div class="col-md-8">
        <div class="card mb-3" v-for="item in cart" :key="item.id">
          <div class="row g-0">
            <div class="col-md-4">
              <img :src="item.image" class="img-fluid rounded-start" alt="Sản phẩm">
            </div>
            <div class="col-md-8">
              <div class="card-body">
                <h5 class="card-title">{{ item.name }}</h5>
                <p class="card-text text-primary fw-bold">{{ item.price.toLocaleString() }} VND</p>
                <button class="btn btn-danger btn-sm" @click="removeFromCart(item.id)">🗑 Xóa</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card p-3">
          <h5 class="fw-bold">Tổng tiền: {{ totalPrice.toLocaleString() }} VND</h5>
          <button class="btn btn-success w-100">🛍 Thanh toán</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const cart = ref([
  { id: 1, name: 'Áo thun cao cấp', price: 250000, image: 'https://via.placeholder.com/300' },
  { id: 2, name: 'Quần jean nam', price: 500000, image: 'https://via.placeholder.com/300' }
]);

const totalPrice = computed(() => cart.value.reduce((sum, item) => sum + item.price, 0));

const removeFromCart = (id) => {
  cart.value = cart.value.filter(item => item.id !== id);
};
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
