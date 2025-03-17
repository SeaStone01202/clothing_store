<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">✍️ Chỉnh sửa hồ sơ</h1>
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card p-4">
          <form @submit.prevent="saveProfile">
            <div class="mb-3">
              <label class="form-label">Họ và tên</label>
              <input type="text" class="form-control" v-model="profile.name" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" class="form-control" v-model="profile.email" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input type="text" class="form-control" v-model="profile.phone" required>
            </div>
            <button class="btn btn-primary w-100">💾 Lưu thay đổi</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const profile = ref({
  name: 'Nguyễn Văn A',
  email: 'nguyenvana@example.com',
  phone: '0123456789'
});

const saveProfile = () => {
  alert('Hồ sơ đã được cập nhật!');
};
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
