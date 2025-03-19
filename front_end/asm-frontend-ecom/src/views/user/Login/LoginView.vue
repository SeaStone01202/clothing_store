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

        <!-- 🔥 Nút đăng nhập với Google & Zalo -->
        <div class="social-login mt-3">
          <button @click="loginWithGoogle" class="btn btn-light border d-flex align-items-center w-100">
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/768px-Google_%22G%22_logo.svg.png" 
       alt="Google" class="social-icon me-2" /> 
  Đăng nhập với Google
</button>
          <button @click="loginWithZalo" class="btn btn-light border d-flex align-items-center w-100 mt-2">
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Icon_of_Zalo.svg/512px-Icon_of_Zalo.svg.png" 
     alt="Zalo" class="zalo-icon me-2" /> Đăng nhập với Zalo
          </button>
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



const loginWithGoogle = () => {
  window.location.href = "http://localhost:8080/oauth2/authorization/google";
};

const loginWithZalo = () => {
  const appId = "YOUR_APP_ID";
  const redirectUri = "http://localhost:5173/callback"; // URL frontend nhận mã code
  const codeChallenge = "YOUR_CODE_CHALLENGE"; // Mã hóa từ code_verifier
  const authUrl = `https://oauth.zaloapp.com/v4/permission?app_id=${appId}&redirect_uri=${redirectUri}&code_challenge=${codeChallenge}&code_challenge_method=S256`;
  
  window.location.href = authUrl;
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

.social-login .zalo-icon {
  width: 24px;
  height: 24px;
}

</style>
