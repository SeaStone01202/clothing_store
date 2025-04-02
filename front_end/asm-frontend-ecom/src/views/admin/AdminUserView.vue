<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">👤 Quản lý người dùng</h1>

    <div v-if="adminStore.loading" class="text-center mb-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>
    <div v-if="adminStore.error" class="alert alert-danger text-center" role="alert">
      {{ adminStore.error }}
    </div>

    <div class="card" v-if="!adminStore.loading && !adminStore.error">
      <div class="card-body">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Email</th>
              <th>Tên</th>
              <th>Vai trò</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in adminStore.getUsers" :key="user.email">
              <td>{{ user.email }}</td>
              <td>{{ user.fullname || 'Chưa cập nhật' }}</td>
              <td>
                <select v-model="user.role" @change="updateUserRole(user)" class="form-select">
                  <option value="CUSTOMER">Khách hàng</option>
                  <option value="STAFF">Nhân viên</option>
                  <option value="DIRECTOR">Giám đốc</option>
                </select>
              </td>
              <td>
                <select v-model="user.status" @change="updateUserStatus(user)" class="form-select">
                  <option value="ACTIVE">Đang hoạt động</option>
                  <option value="INACTIVE">Ngừng hoạt động</option>
                </select>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from '@/stores/AuthStore';
import { useAdminStore } from '@/stores/AdminStore';

const authStore = useAuthStore();
const adminStore = useAdminStore();

onMounted(async () => {
  if (!authStore.isAuthenticated() || authStore.user?.role !== 'DIRECTOR') {
    adminStore.error = 'Bạn không có quyền truy cập trang này';
    return;
  }

  await adminStore.fetchUsers();
});

const updateUserRole = async (user) => {
  console.log('Updating user:', { email: user.email, role: user.role });
  const success = await adminStore.updateUserRole(user.email, user.role);
  if (success) {
    console.log(`Cập nhật vai trò người dùng ${user.email} thành ${user.role}`);
  }
};

const updateUserStatus = async (user) => {
  console.log('Deleting user:', user.email);
  const success = await adminStore.updateUserStatus(user.email, user.status);
  if (success) {
    console.log(`Cập nhật trạng thái người dùng ${user.email} thành ${user.status}`);
  }
};
</script>

<style scoped>
.table {
  width: 100%;
}
.btn-sm {
  padding: 0.25rem 0.5rem;
  font-size: 0.875rem;
}
</style>