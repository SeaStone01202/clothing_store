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
          <!-- Form xác nhận thanh toán -->
          <h5 class="fw-bold mb-3">Xác nhận thanh toán</h5>

          <!-- Tên người dùng -->
          <div class="mb-3">
            <label for="fullName" class="form-label">Tên người nhận</label>
            <input
              type="text"
              class="form-control"
              id="fullName"
              v-model="fullName"
              placeholder="Nhập tên người nhận"
              required
              :disabled="isOrderLoading"
            />
          </div>

          <!-- Số điện thoại -->
          <div class="mb-3">
            <label for="phoneNumber" class="form-label">Số điện thoại</label>
            <input
              type="text"
              class="form-control"
              id="phoneNumber"
              v-model="phoneNumber"
              placeholder="Nhập số điện thoại"
              required
              :disabled="isOrderLoading"
            />
          </div>

          <!-- Chọn địa chỉ giao hàng -->
          <div class="mb-3">
            <label for="address" class="form-label">Địa chỉ giao hàng</label>
            <div v-if="addresses.length === 0" class="alert alert-warning">
              Bạn chưa có địa chỉ nào. Vui lòng thêm địa chỉ trong trang quản lý hồ sơ.
              <router-link to="/edit-profile" class="btn btn-primary btn-sm ms-2">
                Thêm địa chỉ
              </router-link>
            </div>
            <select
              v-else
              class="form-select"
              id="address"
              v-model="selectedAddress"
              required
              :disabled="isOrderLoading"
            >
              <option value="" disabled>Chọn địa chỉ giao hàng</option>
              <option v-for="addr in addresses" :key="addr.id" :value="addr">
                {{ addr.addressLine }}, {{ addr.ward }}, {{ addr.district }}, {{ addr.city }}
              </option>
            </select>
          </div>

          <!-- Phương thức thanh toán -->
          <div class="mb-3">
            <label class="form-label">Phương thức thanh toán</label>
            <div class="d-flex gap-2">
              <button
                class="btn w-100"
                :class="paymentMethod === 'COD' ? 'btn-primary' : 'btn-outline-primary'"
                @click="paymentMethod = 'COD'"
                :disabled="isOrderLoading"
              >
                Thanh toán COD
              </button>
              <button
                class="btn w-100"
                :class="paymentMethod === 'PREPAID' ? 'btn-success' : 'btn-outline-success'"
                @click="paymentMethod = 'PREPAID'"
                :disabled="isOrderLoading"
              >
                Thanh toán trước
              </button>
            </div>
          </div>

          <!-- Tổng tiền -->
          <h5 class="fw-bold mb-3">Tổng tiền: {{ cartStore.totalPrice ? cartStore.totalPrice.toLocaleString() : '0' }} VND</h5>

          <!-- Thông báo lỗi -->
          <div v-if="errorMessage" class="alert alert-danger py-2 text-center">
            {{ errorMessage }}
          </div>

          <!-- Nút Thanh toán -->
          <button
            class="btn btn-success w-100"
            :disabled="!selectedAddress || !paymentMethod || !fullName || !phoneNumber || isOrderLoading"
            @click="placeOrder"
          >
            <span v-if="isOrderLoading" class="spinner-border spinner-border-sm me-2"></span>
            🛍 Thanh toán
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useCartStore } from '@/stores/CartStore';
import { useAuthStore } from '@/stores/AuthStore';
import { useUserStore } from '@/stores/UserStore';
import { useOrderStore } from '@/stores/OrderStore';

const cartStore = useCartStore();
const authStore = useAuthStore();
const userStore = useUserStore();
const orderStore = useOrderStore();

const addresses = ref([]);
const selectedAddress = ref(null);
const paymentMethod = ref(null);
const fullName = ref('');
const phoneNumber = ref('');
const errorMessage = ref('');
const isOrderLoading = ref(false);

onMounted(async () => {
  if (authStore.isAuthenticated() && authStore.user) {
    await cartStore.fetchCart();

    // Lấy danh sách địa chỉ
    await userStore.fetchAddresses(authStore.user.email);
    addresses.value = userStore.getAddresses;
    if (addresses.value.length > 0) {
      selectedAddress.value = addresses.value.find(addr => addr.isDefault) || addresses.value[0];
    }

    // Lấy thông tin người dùng
    fullName.value = authStore.user.fullName || '';
    phoneNumber.value = authStore.user.phoneNumber || '';
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

const placeOrder = async () => {
  if (!fullName.value) {
    errorMessage.value = 'Vui lòng nhập tên người nhận!';
    return;
  }

  if (!phoneNumber.value) {
    errorMessage.value = 'Vui lòng nhập số điện thoại!';
    return;
  }

  if (!selectedAddress.value) {
    errorMessage.value = 'Vui lòng chọn địa chỉ giao hàng!';
    return;
  }

  if (!paymentMethod.value) {
    errorMessage.value = 'Vui lòng chọn phương thức thanh toán!';
    return;
  }

  isOrderLoading.value = true;
  errorMessage.value = '';
  try {
    const orderDetails = cartStore.cart.cartDetails.map(item => ({
      productId: item.productId,
      quantity: item.quantity,
      price: item.productPrice,
    }));

    const orderData = {
      name: fullName.value,
      phone: phoneNumber.value,
      addressId: selectedAddress.value.id,
      paymentMethod: paymentMethod.value,
      orderDetails,
    };

    const result = await orderStore.createOrder(orderData);

    if (result.success) {
      alert(result.message);
      await cartStore.fetchCart(); // Làm mới giỏ hàng
    } else {
      errorMessage.value = result.message;
    }
  } catch (error) {
    errorMessage.value = 'Có lỗi xảy ra khi đặt hàng!';
  } finally {
    isOrderLoading.value = false;
  }
};
</script>

<style scoped>
/* Giữ nguyên style */
</style>