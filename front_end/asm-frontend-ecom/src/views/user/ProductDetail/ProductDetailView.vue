<template>
  <div class="container mt-5">
    <!-- Thông báo -->
    <div v-if="notification.visible" :class="['alert', notification.type === 'success' ? 'alert-success' : 'alert-danger', 'text-center']" role="alert">
      {{ notification.message }}
    </div>

    <!-- Hiển thị trạng thái loading -->
    <div v-if="productStore.loading || cartStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <!-- Hiển thị lỗi nếu có -->
    <div v-if="productStore.error" class="alert alert-danger text-center" role="alert">
      {{ productStore.error }}
    </div>
    <div v-if="cartStore.error" class="alert alert-danger text-center" role="alert">
      {{ cartStore.error }}
    </div>

    <!-- Chi tiết sản phẩm -->
    <div v-if="productStore.productDetail" class="row mb-5">
      <div class="col-md-6">
        <img
          :src="productStore.productDetail.image || 'https://placehold.co/400x500?text=No+Image'"
          class="img-fluid rounded shadow-sm product-image"
          alt="Sản phẩm"
          @error="handleImageError"
        />
      </div>
      <div class="col-md-6 d-flex flex-column justify-content-center">
        <h2 class="fw-bold text-dark mb-3">{{ productStore.productDetail.name }}</h2>
        <p class="text-primary fw-bold fs-4 mb-3">{{ productStore.productDetail.price.toLocaleString() }} VND</p>
        <div class="mb-3">
          <span class="fw-bold text-dark">Tình trạng: </span>
          <span :class="productStore.productDetail.stock > 0 ? 'text-success' : 'text-danger'">
            {{ productStore.productDetail.stock > 0 ? `Còn hàng (${productStore.productDetail.stock} sản phẩm)` : 'Hết hàng' }}
          </span>
        </div>
        <div class="mb-3">
          <span class="fw-bold text-dark">Danh mục: </span>
          <span class="text-muted">{{ productStore.productDetail.category }}</span>
        </div>
        <p class="text-muted mb-4">{{ productStore.productDetail.description }}</p>
        <div class="mb-3 d-flex align-items-center">
          <span class="fw-bold text-dark me-3">Số lượng: </span>
          <input
            type="number"
            class="form-control w-25"
            v-model="quantity"
            min="1"
            :max="productStore.productDetail.stock"
            :disabled="productStore.productDetail.stock <= 0"
          />
        </div>
        <button
          class="btn btn-success w-100 py-2"
          :disabled="productStore.productDetail.stock <= 0"
          @click="addToCart"
        >
          <i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng
        </button>
      </div>
    </div>
    <div v-else-if="!productStore.loading" class="text-center text-muted">Sản phẩm không tồn tại.</div>

    <!-- Sản phẩm liên quan -->
    <div v-if="productStore.relatedProducts.length" class="mt-5">
      <h3 class="text-center text-primary fw-bold mb-4">Sản phẩm cùng loại</h3>
      <div class="row">
        <div class="col-md-4 mb-4" v-for="product in productStore.relatedProducts" :key="product.id">
          <div class="card product-card shadow-sm">
            <img
              :src="product.image || 'https://placehold.co/300x300?text=No+Image'"
              class="card-img-top"
              alt="Sản phẩm"
              @error="handleImageError"
            />
            <div class="card-body text-center">
              <h5 class="card-title text-dark fw-bold mb-2">{{ product.name }}</h5>
              <p class="card-text text-primary fw-bold mb-3">{{ product.price.toLocaleString() }} VND</p>
              <router-link :to="`/product/${product.id}`" class="btn btn-primary w-100">
                <i class="bi bi-eye me-2"></i>Xem chi tiết
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Phân trang -->
      <nav v-if="productStore.relatedProducts.length">
        <ul class="pagination justify-content-center mt-4">
          <li class="page-item" :class="{ disabled: productStore.relatedProductsPage === 1 }">
            <button class="page-link" @click="productStore.setRelatedProductsPage(productStore.relatedProductsPage - 1)">Trước</button>
          </li>
          <li class="page-item" v-for="page in productStore.relatedProductsTotalPages" :key="page" :class="{ active: productStore.relatedProductsPage === page }">
            <button class="page-link" @click="productStore.setRelatedProductsPage(page)">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ disabled: productStore.relatedProductsPage === productStore.relatedProductsTotalPages }">
            <button class="page-link" @click="productStore.setRelatedProductsPage(productStore.relatedProductsPage + 1)">Sau</button>
          </li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { onMounted, watch, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useProductStore } from '@/stores/ProductStore';
import { useCartStore } from '@/stores/CartStore';

const route = useRoute();
const router = useRouter();
const productStore = useProductStore();
const cartStore = useCartStore();

const quantity = ref(1);

// Biến để hiển thị thông báo
const notification = ref({
  message: '',
  type: '', // success hoặc error
  visible: false,
});

const loadProductDetail = async (productId) => {
  await productStore.fetchProductDetail(productId);
  if (productStore.productDetail) {
    await productStore.fetchRelatedProducts(productId, productStore.productDetail.category);
  }
};

onMounted(async () => {
  const productId = parseInt(route.params.id);
  await loadProductDetail(productId);
});

watch(() => route.params.id, async (newId) => {
  const productId = parseInt(newId);
  await loadProductDetail(productId);
});

const addToCart = async () => {
  if (!productStore.productDetail) return;

  const success = await cartStore.addProductToCart(productStore.productDetail.id, quantity.value);
  if (success) {
    showNotification('Sản phẩm đã được thêm vào giỏ hàng!', 'success');
  } else {
    showNotification('Không thể thêm sản phẩm vào giỏ hàng.', 'error');
  }
};

const showNotification = (message, type) => {
  notification.value = {
    message,
    type,
    visible: true,
  };
  setTimeout(() => {
    notification.value.visible = false;
  }, 3000); // Ẩn sau 3 giây
};

const handleImageError = (event) => {
  event.target.src = 'https://placehold.co/400x500?text=No+Image';
};
</script>

<style scoped>
.product-image {
  max-height: 500px;
  object-fit: cover;
  border: 1px solid #e0e0e0;
}

.product-card {
  border: none;
  border-radius: 10px;
  transition: transform 0.3s ease-in-out;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1) !important;
}

.card-img-top {
  height: 250px;
  object-fit: cover;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.card-body {
  padding: 1.5rem;
}

.card-title {
  font-size: 1.1rem;
  line-height: 1.4;
}

.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  transition: background-color 0.3s ease;
}

.btn-primary:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.btn-success {
  background-color: #28a745;
  border-color: #28a745;
  transition: background-color 0.3s ease;
}

.btn-success:hover {
  background-color: #218838;
  border-color: #218838;
}

.btn-success:disabled {
  background-color: #6c757d;
  border-color: #6c757d;
  cursor: not-allowed;
}

.pagination .page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
}
</style>
