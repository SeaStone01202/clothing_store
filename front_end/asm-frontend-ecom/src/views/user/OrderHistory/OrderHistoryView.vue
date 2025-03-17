<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">📜 Lịch sử mua hàng</h1>
    <div v-if="orders.length === 0" class="text-center text-muted">Chưa có đơn hàng nào.</div>
    <div v-else class="row">
      <div class="col-md-8 mx-auto">
        <div class="card mb-3" v-for="order in orders" :key="order.id">
          <div class="card-body">
            <h5 class="fw-bold">Đơn hàng #{{ order.id }}</h5>
            <p class="text-muted">Ngày đặt: {{ order.date }}</p>
            <p class="text-primary fw-bold">Tổng tiền: {{ order.total.toLocaleString() }} VND</p>
            <button class="btn btn-outline-primary btn-sm">🔍 Xem chi tiết</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const orders = ref([
  { id: 101, date: '2025-03-10', total: 750000 },
  { id: 102, date: '2025-03-12', total: 1200000 }
]);
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
