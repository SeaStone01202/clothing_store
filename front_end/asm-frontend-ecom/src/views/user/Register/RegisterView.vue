<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow-sm register-card">
      <h2 class="text-center text-primary fw-bold">📝 Đăng Ký</h2>
      <form @submit.prevent="handleRegister">
        <div class="mb-3">
          <label class="form-label">Họ và Tên</label>
          <input type="text" class="form-control" v-model="fullName" required placeholder="Nhập họ và tên">
        </div>
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="email" class="form-control" v-model="email" required placeholder="Nhập email">
        </div>
        <div class="mb-3">
          <label class="form-label">Mật khẩu</label>
          <input type="password" class="form-control" v-model="password" required placeholder="Nhập mật khẩu">
        </div>
        <div class="mb-3">
          <label class="form-label">Xác nhận Mật khẩu</label>
          <input type="password" class="form-control" v-model="confirmPassword" required placeholder="Nhập lại mật khẩu">
        </div>
        <div class="d-grid">
          <button type="submit" class="btn btn-primary">Đăng Ký</button>
        </div>
        <div class="mt-3 text-center">
          <router-link to="/login" class="text-primary">Đã có tài khoản? Đăng nhập</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const fullName = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');

const handleRegister = () => {
  if (password.value !== confirmPassword.value) {
    alert('Mật khẩu không khớp.');
    return;
  }
  console.log('Họ và tên:', fullName.value);
  console.log('Email:', email.value);
  console.log('Mật khẩu:', password.value);
  alert('Chức năng đăng ký chưa triển khai.');
};
</script>

<style scoped>
.register-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}
</style>
