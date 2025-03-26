<!-- src/views/user/Register/Register.vue -->
<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow-sm register-card">
      <h2 class="text-center text-primary fw-bold">📝 Đăng Ký</h2>
      <form @submit.prevent="handleRegister">
        <!-- 🔹 Họ và Tên -->
        <div class="mb-3">
          <label class="form-label">Họ và Tên</label>
          <input
            type="text"
            class="form-control"
            v-model="fullName"
            required
            placeholder="Nhập họ và tên"
          />
        </div>

        <!-- 🔹 Email -->
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input
            type="email"
            class="form-control"
            v-model="email"
            required
            placeholder="Nhập email"
          />
        </div>

        <!-- 🔹 Mật khẩu -->
        <div class="mb-3">
          <label class="form-label">Mật khẩu</label>
          <input
            type="password"
            class="form-control"
            v-model="password"
            required
            placeholder="Nhập mật khẩu"
          />
        </div>

        <!-- 🔹 Xác nhận Mật khẩu -->
        <div class="mb-3">
          <label class="form-label">Xác nhận Mật khẩu</label>
          <input
            type="password"
            class="form-control"
            v-model="confirmPassword"
            required
            placeholder="Nhập lại mật khẩu"
          />
        </div>

        <!-- 🔹 Thông báo lỗi -->
        <div v-if="errorMessage" class="alert alert-danger py-2 text-center">
          {{ errorMessage }}
        </div>

        <!-- 🔹 Nút Đăng ký -->
        <div class="d-grid">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm"></span>
            Đăng Ký
          </button>
        </div>

        <!-- 🔹 Liên kết -->
        <div class="mt-3 text-center">
          <router-link to="/login" class="text-primary"
            >Đã có tài khoản? Đăng nhập</router-link
          >
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useUserStore } from "@/stores/UserStore";
import { useRouter } from "vue-router";

// ✅ Trạng thái đăng ký
const fullName = ref("");
const email = ref("");
const password = ref("");
const confirmPassword = ref("");
const errorMessage = ref("");
const loading = ref(false);

const userStore = useUserStore();
const router = useRouter();

/**
 * ✅ Xử lý đăng ký
 */
const handleRegister = async () => {
  // Xóa thông báo lỗi trước đó
  errorMessage.value = "";
  userStore.clearError();
  loading.value = true;

  // Kiểm tra mật khẩu khớp
  if (password.value !== confirmPassword.value) {
    errorMessage.value = "Mật khẩu không khớp.";
    loading.value = false;
    return;
  }

  // Gọi action registerUser từ UserStore
  const result = await userStore.registerUser({
    email: email.value,
    password: password.value,
    fullname: fullName.value,
  });

  if (result.success) {
    // Đăng ký thành công, chuyển hướng về trang đăng nhập
    router.push("/login");
  } else {
    // Hiển thị thông báo lỗi
    errorMessage.value = result.message;
  }

  loading.value = false;
};
</script>

<style scoped>
.register-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}
</style>