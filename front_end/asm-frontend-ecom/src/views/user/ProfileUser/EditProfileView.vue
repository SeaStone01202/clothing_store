<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">✍️ Chỉnh sửa hồ sơ</h1>
    <div class="row">
      <div class="col-md-6 mx-auto">
        <div class="card p-4">
          <form @submit.prevent="saveProfile">
            <div class="mb-3">
              <label class="form-label">Họ và tên</label>
              <input type="text" class="form-control" v-model="profile.fullname" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" class="form-control" v-model="profile.email" required readonly>
            </div>
            <div class="mb-3">
              <label class="form-label">Số điện thoại</label>
              <input type="text" class="form-control" v-model="profile.phone" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Ảnh đại diện</label>
              <input type="file" class="form-control" @change="onFileChange" accept="image/*">
              <img v-if="profile.imageUrl" :src="profile.imageUrl" alt="Preview" class="mt-2" style="max-width: 200px;">
            </div>
            <button class="btn btn-primary w-100" :disabled="isLoading">
              {{ isLoading ? "Đang lưu..." : "💾 Lưu thay đổi" }}
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/AuthStore"; // Import AuthStore
import { useUserStore } from "@/stores/UserStore"; // Import UserStore

// Khởi tạo stores
const authStore = useAuthStore();
const userStore = useUserStore();

// Dữ liệu profile
const profile = ref({
  fullname: "",
  email: "",
  phone: "",
  image: null,      // Lưu file ảnh
  imageUrl: "",     // Lưu URL để hiển thị preview
});

// Trạng thái loading
const isLoading = ref(false);

// Load thông tin người dùng từ AuthStore khi component được mount
onMounted(async () => {
  if (!authStore.user) {
    await authStore.fetchUserInfo(); // Lấy thông tin user nếu chưa có
  }
  const user = authStore.user;
  if (user) {
    profile.value = {
      fullname: user.fullname || "Nguyễn Văn A",
      email: user.email || "nguyenvana@example.com",
      phone: user.phone || "0123456789",
      image: null,
      imageUrl: user.image || "",
    };
    userStore.setUser(user); // Đồng bộ với UserStore
  } else {
    console.error("Không tìm thấy thông tin người dùng!");
  }
});

// Xử lý khi chọn file ảnh
const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    profile.value.image = file;
    profile.value.imageUrl = URL.createObjectURL(file); // Hiển thị preview
  }
};

// Gửi request cập nhật hồ sơ qua UserStore
const saveProfile = async () => {
  isLoading.value = true;
  try {
    const result = await userStore.updateUser({
      email: profile.value.email,
      fullname: profile.value.fullname,
      phone: profile.value.phone,
      image: profile.value.image,
    });
    if (result.success) {
      alert(result.message);
      profile.value.imageUrl = userStore.getUser.image; // Cập nhật URL ảnh từ backend
      authStore.user = userStore.getUser; // Đồng bộ với AuthStore
    } else {
      alert(result.message);
    }
  } catch (error) {
    alert("Có lỗi xảy ra khi cập nhật hồ sơ!");
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.card {
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
</style>