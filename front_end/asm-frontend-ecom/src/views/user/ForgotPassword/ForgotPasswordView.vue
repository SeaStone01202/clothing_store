<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow-sm forgot-card">
      <h2 class="text-center text-primary fw-bold">🔑 Quên Mật Khẩu</h2>
      <p class="text-center text-muted">Nhập email của bạn để nhận liên kết đặt lại mật khẩu.</p>

      <form @submit.prevent="handleForgotPassword">
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="email" class="form-control" v-model="email" required placeholder="Nhập email của bạn">
        </div>

        <!-- 🔹 Thông báo -->
        <div v-if="message" :class="['alert', success ? 'alert-success' : 'alert-danger', 'py-2', 'text-center']">
          {{ message }}
        </div>

        <!-- 🔹 Nút gửi -->
        <div class="d-grid">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
            Gửi Yêu Cầu
          </button>
        </div>

        <div class="mt-3 text-center">
          <router-link to="/login" class="text-primary">Quay lại đăng nhập</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/UserStore';
import { ref } from 'vue';

// 📌 Khai báo các biến trạng thái
const email = ref('');
const loading = ref(false);
const message = ref('');
const success = ref(false);
const userStore = useUserStore();

/**
 * ✅ Kiểm tra định dạng email hợp lệ
 */
const validateEmail = (value) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(value);
};

/**
 * ✅ Xử lý gửi yêu cầu quên mật khẩu
 */
const handleForgotPassword = async () => {
  message.value = '';
  success.value = false;

  // ❗ Kiểm tra định dạng email
  if (!validateEmail(email.value)) {
    message.value = '❗ Email không hợp lệ. Vui lòng kiểm tra lại.';
    return;
  }

  loading.value = true;

  // 📩 Gọi hàm gửi yêu cầu
  const result = await userStore.forgotPassword(email.value);
  if (result.success) {
    success.value = true;
    message.value = '✅ Yêu cầu đã được gửi! Vui lòng kiểm tra email của bạn.';
  } else {
    success.value = false;
    message.value = '❌ Có lỗi xảy ra. Vui lòng thử lại sau.';
  }

  loading.value = false;

  // ⏳ Tự động ẩn thông báo sau 5 giây
  setTimeout(() => {
    message.value = '';
  }, 5000);
};
</script>

<style scoped>
.forgot-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}

</style>
