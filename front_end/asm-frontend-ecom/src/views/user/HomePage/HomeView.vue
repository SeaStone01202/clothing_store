<template>
  <div class="container mt-5">
    <!-- Tiêu đề -->
    <h1 class="text-center text-primary fw-bold mb-4">🛍 Sản phẩm mới nhất</h1>

    <!-- Thanh tìm kiếm -->
    <div class="mb-4 text-center">
      <div class="input-group" style="max-width: 500px; margin: 0 auto;">
        <input
          type="text"
          class="form-control"
          placeholder="Tìm kiếm sản phẩm..."
          v-model="searchKeyword"
          @keyup.enter="searchProducts"
        />
        <button class="btn btn-primary" @click="searchProducts">
          <i class="bi bi-search"></i> Tìm kiếm
        </button>
        <button v-if="productStore.searchKeyword" class="btn btn-outline-secondary" @click="clearSearch">
          Xóa
        </button>
      </div>
    </div>

    <!-- Hiển thị trạng thái loading -->
    <div v-if="productStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <!-- Hiển thị lỗi nếu có -->
    <div v-if="productStore.error" class="alert alert-danger text-center" role="alert">
      {{ productStore.error }}
    </div>

    <!-- Bộ lọc -->
    <div v-if="!productStore.loading" class="mb-4 text-center d-flex justify-content-center align-items-center flex-wrap">
      <!-- Bộ lọc thể loại -->
      <button
        v-for="category in productStore.categories"
        :key="category"
        @click="productStore.setSelectedCategory(category)"
        class="btn me-2 mb-2"
        :class="productStore.selectedCategory === category ? 'btn-primary' : 'btn-outline-primary'"
      >
        {{ category }}
      </button>

      <!-- Dropdown lọc theo mệnh giá -->
      <div class="dropdown me-2 mb-2">
        <button
          class="btn btn-outline-primary dropdown-toggle"
          type="button"
          id="priceRangeDropdown"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          Lọc theo giá: {{ productStore.selectedPriceRange }}
        </button>
        <ul class="dropdown-menu" aria-labelledby="priceRangeDropdown">
          <li v-for="priceRange in productStore.priceRanges" :key="priceRange">
            <button
              class="dropdown-item"
              @click="productStore.setSelectedPriceRange(priceRange)"
            >
              {{ priceRange }}
            </button>
          </li>
        </ul>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div v-if="!productStore.loading && productStore.paginatedProducts.length" class="row">
      <div class="col-md-4 mb-4" v-for="product in productStore.paginatedProducts" :key="product.id">
        <div class="card product-card">
          <img :src="product.image" class="card-img-top" alt="Sản phẩm" @error="handleImageError" />
          <div class="card-body text-center">
            <h5 class="card-title text-dark fw-bold">{{ product.name }}</h5>
            <p class="card-text text-primary fw-bold">{{ product.price.toLocaleString() }} VND</p>
            <router-link :to="`/product/${product.id}`" class="btn btn-primary w-100">🔍 Xem chi tiết</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Thông báo nếu không có sản phẩm -->
    <div v-if="!productStore.loading && !productStore.paginatedProducts.length" class="text-center">
      <p class="text-muted">Không có sản phẩm nào trong danh mục này.</p>
    </div>

    <!-- Phân trang -->
    <nav v-if="!productStore.loading && productStore.paginatedProducts.length">
      <ul class="pagination justify-content-center mt-4">
        <li class="page-item" :class="{ disabled: productStore.currentPage === 1 }">
          <button class="page-link" @click="productStore.setPage(productStore.currentPage - 1)">Trước</button>
        </li>
        <li class="page-item" v-for="page in productStore.totalPages" :key="page" :class="{ active: productStore.currentPage === page }">
          <button class="page-link" @click="productStore.setPage(page)">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: productStore.currentPage === productStore.totalPages }">
          <button class="page-link" @click="productStore.setPage(productStore.currentPage + 1)">Sau</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useProductStore } from '@/stores/ProductStore';

const productStore = useProductStore();
const searchKeyword = ref('');

onMounted(async () => {
  await productStore.fetchCategories(); // Lấy danh mục trước
  productStore.fetchProducts(productStore.currentPage); // Sau đó lấy sản phẩm
});

const searchProducts = () => {
  productStore.setSearchKeyword(searchKeyword.value);
};

const clearSearch = () => {
  searchKeyword.value = '';
  productStore.clearSearch();
};

const handleImageError = (event) => {
  event.target.src = 'https://via.placeholder.com/300x200?text=No+Image';
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
.card-img-top {
  height: 200px;
  object-fit: cover;
}
.pagination .page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}
.dropdown-menu {
  min-width: 150px;
}
</style>