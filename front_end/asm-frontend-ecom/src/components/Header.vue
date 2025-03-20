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

        <!-- Search Bar -->
        <form class="d-flex me-3">
          <input class="form-control me-2" type="search" placeholder="🔍 Tìm kiếm sản phẩm..." />
          <button class="btn btn-outline-primary" type="submit">Tìm</button>
        </form>

        <!-- Icons -->
        <div class="d-flex align-items-center">
          <!-- Giỏ hàng -->
          <router-link class="nav-link text-dark position-relative me-3" to="/cart">
            🛒 Giỏ hàng
            <span class="badge bg-danger rounded-circle position-absolute top-0 start-100 translate-middle">
              2
            </span>
          </router-link>

          <!-- Dropdown Tài khoản -->
          <div class="dropdown">
            <button class="btn btn-light dropdown-toggle" type="button" id="accountDropdown" data-bs-toggle="dropdown">
              👤 {{ isAuthenticated ? userInfo.fullname || userInfo.email : "Tài khoản" }}
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
                <li v-if="userInfo.role === 'ADMIN'">
                  <router-link class="dropdown-item" to="/admin">⚙️ Quản trị viên</router-link>
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
import { computed, watchEffect } from "vue";
import { useAuthStore } from "@/stores/AuthStore";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

// ✅ Kiểm tra đăng nhập
const isAuthenticated = computed(() => authStore.isAuthenticated());
const userInfo = computed(() => authStore.user || { email: "Không có email", role: "CUSTOMER" });

// ✅ Theo dõi thay đổi của accessToken để cập nhật UI ngay khi login/logout
watchEffect(() => {
  if (authStore.accessToken) {
    authStore.fetchUserInfo(); // 🔥 Load user info ngay sau khi login
  }
});

// ✅ Xử lý đăng xuất
const handleLogout = async () => {
  await authStore.logout();
  router.push("/login"); // ✅ Quay về trang login
};
</script>


<style scoped>
.navbar {
  border-bottom: 2px solid #007bff;
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
