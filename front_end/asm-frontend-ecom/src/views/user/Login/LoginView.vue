<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow-sm login-card">
      <h2 class="text-center text-primary fw-bold">🔐 Đăng Nhập</h2>
      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input type="email" class="form-control" v-model="email" required placeholder="Nhập email của bạn">
        </div>
        <div class="mb-3">
          <label class="form-label">Mật khẩu</label>
          <input type="password" class="form-control" v-model="password" required placeholder="Nhập mật khẩu">
        </div>
        <div class="d-grid">
          <button type="submit" class="btn btn-primary">Đăng Nhập</button>
        </div>
        <div class="mt-3 text-center">
          <router-link to="/forgot-password" class="text-primary">Quên mật khẩu?</router-link> |
          <router-link to="/register" class="text-primary">Đăng ký</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const email = ref('');
const password = ref('');

const handleLogin = () => {
  console.log('Email:', email.value);
  console.log('Password:', password.value);
  alert('Chức năng đăng nhập chưa triển khai.');
};
</script>

<style scoped>
.login-card {
  width: 100%;
  max-width: 400px;
  border-top: 4px solid #007bff;
}
</style>
