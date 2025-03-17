<template>
  <div class="container mt-5">
    <div v-if="product" class="row">
      <div class="col-md-6">
        <img :src="product.image" class="img-fluid rounded">
      </div>
      <div class="col-md-6">
        <h2 class="fw-bold text-dark">{{ product.name }}</h2>
        <p class="text-primary fw-bold">{{ product.price.toLocaleString() }} VND</p>
        <p>{{ product.description }}</p>
        <button class="btn btn-success w-100">🛍 Thêm vào giỏ hàng</button>
      </div>
    </div>
    <div v-else class="text-center text-muted">Sản phẩm không tồn tại.</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const product = ref(null);

const products = [
  { id: 1, name: 'Áo thun cao cấp', price: 250000, image: 'https://via.placeholder.com/300', description: 'Áo thun nam phong cách, chất liệu cotton 100%' },
  { id: 2, name: 'Quần jean nam', price: 500000, image: 'https://via.placeholder.com/300', description: 'Quần jean cao cấp, form dáng chuẩn' }
];

onMounted(() => {
  product.value = products.find(p => p.id === parseInt(route.params.id));
});
</script>

<style scoped>
</style>
