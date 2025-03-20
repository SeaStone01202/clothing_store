<template>
  <div>
    <button @click="loginWithZalo" class="btn btn-light border d-flex align-items-center w-100 mt-2">
      <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Icon_of_Zalo.svg/512px-Google_%22G%22_logo.svg.png" 
        alt="Zalo" class="zalo-icon me-2" /> Đăng nhập với Zalo
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const zaloConfig = {
  appId: "3502086008475683777",
  redirectUri: "http://localhost:5173/auth/zalo/callback",
  state: "secure_random_string",
};

const loginWithZalo = () => {
  const authUrl = `https://oauth.zaloapp.com/v4/permission?app_id=${zaloConfig.appId}&redirect_uri=${zaloConfig.redirectUri}&state=${zaloConfig.state}`;
  window.location.href = authUrl; // Chuyển hướng user đến trang đăng nhập Zalo
};
</script>

<style scoped>
.social-login button {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
}

.social-login .zalo-icon {
  width: 24px;
  height: 24px;
}
</style>
