<template>
    <div class="loading-container">
      <div class="spinner"></div>
      <p class="loading-text">Đang xử lý đăng nhập...</p>
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/AuthStore';
  import { ref, onMounted } from 'vue';
  
  const router = useRouter();
  const authStore = useAuthStore();
  const errorMessage = ref(''); // Biến để hiển thị thông báo lỗi
  
  onMounted(() => {
    console.log("🔄 Bắt đầu xử lý đăng nhập Google...");
  
    // Gọi fetchUserInfo (không cần await, để chạy song song)
    authStore.fetchUserInfo().catch(error => {
      console.error("❌ Lỗi khi lấy thông tin user:", error);
      if (error.response?.status === 400) {
        errorMessage.value = "Yêu cầu không hợp lệ, vui lòng thử lại.";
      } else if (error.response?.status === 401) {
        errorMessage.value = "Phiên đăng nhập không hợp lệ, vui lòng đăng nhập lại.";
      } else {
        errorMessage.value = "Đã có lỗi xảy ra, vui lòng thử lại sau.";
      }
    });
  
    setTimeout(() => {
      console.log("✅ Đã chờ 2 giây, chuyển hướng về trang chủ...");
      router.push('/');
    }, 2000);
  });
  </script>
  
  <style scoped>
  .loading-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f5f5f5;
  }
  
  .spinner {
    width: 40px;
    height: 40px;
    border: 4px solid #f3f3f3;
    border-top: 4px solid #007bff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  .loading-text {
    margin-top: 16px;
    font-size: 18px;
    color: #333;
    font-weight: 500;
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  </style>