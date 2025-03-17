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
        <div class="d-grid">
          <button type="submit" class="btn btn-primary">Gửi Yêu Cầu</button>
        </div>
        <div class="mt-3 text-center">
          <router-link to="/login" class="text-primary">Quay lại đăng nhập</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const email = ref('');

const handleForgotPassword = () => {
  console.log('Email:', email.value);
  alert('Chức năng quên mật khẩu chưa triển khai.');
};
</script>

<style scoped>
.forgot-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}
</style>
