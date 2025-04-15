<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
    <div class="container">
      <!-- Logo -->
      <router-link class="navbar-brand fw-bold text-primary" to="/">🛍 CLOTHING SHOP</router-link>

      <!-- Toggle Button (Mobile) -->
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- Navbar Links -->
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav mx-auto">
          <li class="nav-item">
            <router-link class="nav-link text-dark" to="/">🏠 Trang chủ</router-link>
          </li>
        </ul>

        <!-- Icons -->
        <div class="d-flex align-items-center">
          <!-- Giỏ hàng -->
          <router-link class="nav-link text-dark position-relative me-3" to="/cart">
            🛒 Giỏ hàng
            <span
              v-if="cartStore.cartItemCount > 0"
              class="badge bg-danger rounded-circle position-absolute top-0 start-100 translate-middle"
            >
              {{ cartStore.cartItemCount }}
            </span>
          </router-link>

          <!-- Dropdown Tài khoản -->
          <div class="dropdown">
            <button class="btn btn-light dropdown-toggle" type="button" id="accountDropdown" data-bs-toggle="dropdown">
              <img
                :src="userInfo.image || 'https://ui-avatars.com/api/?name=John+Doe'"
                alt="Avatar"
                class="rounded-circle me-2 img-fluid"
                style="width: 32px; height: 32px; object-fit: cover;"
              />
              {{ isAuthenticated ? userInfo.fullname || userInfo.email : "Tài khoản" }}
            </button>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="accountDropdown">
              <template v-if="!isAuthenticated">
                <li><router-link class="dropdown-item" to="/login">🔑 Đăng nhập</router-link></li>
                <li><router-link class="dropdown-item" to="/register">📝 Đăng ký</router-link></li>
                <li><router-link class="dropdown-item" to="/forgot-password">🔄 Quên mật khẩu</router-link></li>
              </template>

              <template v-else>
                <li class="dropdown-item">👋 Xin chào: {{ userInfo.email || "Người dùng" }}</li>
                <li><router-link class="dropdown-item" to="/edit-profile">✏️ Chỉnh sửa hồ sơ</router-link></li>
                <li><router-link class="dropdown-item" to="/order-history">📜 Lịch sử mua hàng</router-link></li>
                <li v-if="['DIRECTOR', 'STAFF'].includes(userInfo.role)">
                  <router-link class="dropdown-item" to="/AdminDashboard">⚙️ Admin Dashboard</router-link>
                </li>
                <li><hr class="dropdown-divider" /></li>
                <li>
                  <button class="dropdown-item text-danger" @click="handleLogout">🚪 Đăng xuất</button>
                </li>
              </template>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { computed, watchEffect } from 'vue';
import { useAuthStore } from '@/stores/AuthStore';
import { useCartStore } from '@/stores/CartStore';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const cartStore = useCartStore();
const router = useRouter();

const isAuthenticated = computed(() => authStore.isAuthenticated());
const userInfo = computed(() => authStore.user || { email: 'Không có email', role: 'CUSTOMER' });

watchEffect(async () => {
  if (authStore.accessToken) {
    await authStore.fetchUserInfo();
    await cartStore.fetchCart();
  } else {
    cartStore.cart = null;
  }
});

const handleLogout = async () => {
  await authStore.logout();
  router.push('/login');
};
</script>

<style scoped>
.navbar {
  border-bottom: 2px solid #007bff;
  z-index: 1060; /* Đảm bảo header nằm trên modal */
}
.nav-link {
  font-weight: 500;
  transition: color 0.3s ease-in-out;
}
.nav-link:hover {
  color: #007bff;
}
.badge {
  font-size: 12px;
  padding: 5px;
}
.dropdown-menu {
  min-width: 200px;
}
</style>