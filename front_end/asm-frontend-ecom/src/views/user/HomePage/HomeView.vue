<template>
  <div class="container mt-5">
    <!-- Tiêu đề -->
    <h1 class="text-center text-primary fw-bold mb-4">🛍 Sản phẩm mới nhất</h1>

    <!-- Bộ lọc thể loại -->
    <div class="mb-4 text-center">
      <button
        v-for="category in categories"
        :key="category"
        @click="selectedCategory = category"
        class="btn me-2"
        :class="selectedCategory === category ? 'btn-primary' : 'btn-outline-primary'"
      >
        {{ category }}
      </button>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="row">
      <div class="col-md-4 mb-4" v-for="product in paginatedProducts" :key="product.id">
        <div class="card product-card">
          <img :src="product.image" class="card-img-top" alt="Sản phẩm">
          <div class="card-body text-center">
            <h5 class="card-title text-dark fw-bold">{{ product.name }}</h5>
            <p class="card-text text-primary fw-bold">{{ product.price.toLocaleString() }} VND</p>
            <router-link :to="`/product/${product.id}`" class="btn btn-primary w-100">🔍 Xem chi tiết</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Phân trang -->
    <nav>
      <ul class="pagination justify-content-center mt-4">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <button class="page-link" @click="prevPage">Trước</button>
        </li>
        <li class="page-item" v-for="page in totalPages" :key="page" :class="{ active: currentPage === page }">
          <button class="page-link" @click="currentPage = page">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <button class="page-link" @click="nextPage">Sau</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

// Danh sách sản phẩm (thay ảnh mẫu bằng ảnh thực tế)
const products = ref([
  { id: 1, name: 'Áo thun nam', price: 250000, image: 'https://i.imgur.com/ZC9d6sM.jpg', category: 'Áo' },
  { id: 2, name: 'Quần jean nam', price: 500000, image: 'https://i.imgur.com/f5m5B29.jpg', category: 'Quần' },
  { id: 3, name: 'Giày sneaker', price: 750000, image: 'https://i.imgur.com/aP4f5oX.jpg', category: 'Giày' },
  { id: 4, name: 'Áo hoodie nữ', price: 300000, image: 'https://i.imgur.com/5s1bTj6.jpg', category: 'Áo' },
  { id: 5, name: 'Túi xách nữ', price: 450000, image: 'https://i.imgur.com/6kG3NwO.jpg', category: 'Phụ kiện' },
  { id: 6, name: 'Giày thể thao nữ', price: 700000, image: 'https://i.imgur.com/jtxEoPa.jpg', category: 'Giày' },
  { id: 7, name: 'Quần short nam', price: 400000, image: 'https://i.imgur.com/3pK5F4p.jpg', category: 'Quần' },
  { id: 8, name: 'Balo thời trang', price: 550000, image: 'https://i.imgur.com/vL6Cz0c.jpg', category: 'Phụ kiện' }
]);

// Thể loại sản phẩm
const categories = ref(['Tất cả', 'Áo', 'Quần', 'Giày', 'Phụ kiện']);
const selectedCategory = ref('Tất cả');

// Phân trang
const currentPage = ref(1);
const itemsPerPage = 6;

const filteredProducts = computed(() => {
  if (selectedCategory.value === 'Tất cả') {
    return products.value;
  }
  return products.value.filter(product => product.category === selectedCategory.value);
});

const totalPages = computed(() => Math.ceil(filteredProducts.value.length / itemsPerPage));

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredProducts.value.slice(start, start + itemsPerPage);
});

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};
</script>

<style scoped>
.product-card {
  border: none;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out;
}
.product-card:hover {
  transform: scale(1.05);
}
.pagination .page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}
</style>
