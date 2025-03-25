<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow-sm login-card">
      <h2 class="text-center text-primary fw-bold">🔐 Đăng Nhập</h2>

      <form @submit.prevent="handleLogin">
        <!-- 🔹 Email -->
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="email" class="form-control" v-model="email" required placeholder="Nhập email của bạn">
        </div>

        <!-- 🔹 Mật khẩu -->
        <div class="mb-3">
          <label class="form-label">Mật khẩu</label>
          <input type="password" class="form-control" v-model="password" required placeholder="Nhập mật khẩu">
        </div>

        <!-- 🔹 Thông báo lỗi -->
        <div v-if="errorMessage" class="alert alert-danger py-2 text-center">{{ errorMessage }}</div>

        <!-- 🔹 Nút Đăng nhập -->
        <div class="d-grid">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm"></span>
            Đăng Nhập
          </button>
        </div>

        <!-- 🔥 Nút đăng nhập với Google & Zalo -->
        <div class="social-login mt-3">
          <button @click="loginWithGoogle" type="button" class="btn btn-light border d-flex align-items-center w-100">
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/768px-Google_%22G%22_logo.svg.png" 
                 alt="Google" class="social-icon me-2" /> 
            Đăng nhập với Google
          </button>
          <ZaloLoginButton />
        </div>

        <!-- 🔹 Liên kết -->
        <div class="mt-3 text-center">
          <router-link to="/forgot-password" class="text-primary">Quên mật khẩu?</router-link> |
          <router-link to="/register" class="text-primary">Đăng ký</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/AuthStore";
import { useRouter } from "vue-router";
import ZaloLoginButton from "./ZaloLoginButton.vue";

// ✅ Trạng thái đăng nhập
const email = ref("");
const password = ref("");
const errorMessage = ref("");
const loading = ref(false);
const authStore = useAuthStore();
const router = useRouter();

/**
 * ✅ Xử lý đăng nhập hệ thống
 */
const handleLogin = async () => {
  errorMessage.value = "";
  loading.value = true;

  const result = await authStore.login(email.value, password.value);

  if (result.success) {
    router.push("/"); // 🚀 Chuyển hướng về trang chủ
  } else {
    errorMessage.value = result.message; // ❌ Hiển thị lỗi
  }
  
  loading.value = false;
};

/**
 * ✅ Chuyển hướng đến Google OAuth2
 */
const loginWithGoogle = () => {
  // window.location.href = "http://localhost:8080/oauth2/authorization/google";
  var loginWindow = window.open('http://localhost:8080/oauth2/authorization/google', 'Google Login', 'width=500,height=600');
};
</script>

<style scoped>
.login-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}

.social-login button {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
}

.social-login .social-icon {
  width: 24px;
  height: 24px;
}
</style>
