<template>
    <div class="container mt-5">
      <h1 class="text-center text-primary fw-bold mb-4">📋 Quản lý danh mục</h1>
  
      <!-- Thông báo lỗi hoặc loading -->
      <div v-if="adminStore.loading" class="text-center mb-4">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Đang tải...</span>
        </div>
      </div>
      <div v-if="adminStore.error" class="alert alert-danger text-center" role="alert">
        {{ adminStore.error }}
      </div>
  
      <!-- Danh sách danh mục -->
      <div class="card mb-4" v-if="!adminStore.loading && !adminStore.error">
        <div class="card-body">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="category in adminStore.getCategories" :key="category.id">
                <td>{{ category.id }}</td>
                <td>{{ category.name }}</td>
                <td>
                  <select
                    v-if="isDirector"
                    v-model="category.status"
                    @change="updateCategoryStatus(category)"
                    class="form-select"
                  >
                    <option value="ACTIVE">Đang hoạt động</option>
                    <option value="INACTIVE">Ngừng hoạt động</option>
                  </select>
                  <span v-else>{{ category.status === 'ACTIVE' ? 'Đang hoạt động' : 'Ngừng hoạt động' }}</span>
                </td>
                <td>
                  <button
                    class="btn btn-sm btn-warning"
                    @click="editCategory(category)"
                  >
                    Sửa
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
  
      <!-- Form thêm/sửa danh mục -->
      <div class="card">
        <div class="card-body">
          <h5>{{ editMode ? 'Sửa danh mục' : 'Thêm danh mục mới' }}</h5>
          <form @submit.prevent="submitCategory">
            <div class="mb-3">
              <label for="categoryName" class="form-label">Tên danh mục</label>
              <input
                v-model="categoryForm.name"
                type="text"
                class="form-control"
                id="categoryName"
                required
              />
            </div>
            <button type="submit" class="btn btn-primary">
              {{ editMode ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button
              v-if="editMode"
              type="button"
              class="btn btn-secondary ms-2"
              @click="cancelEdit"
            >
              Hủy
            </button>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, computed } from 'vue';
  import { useAdminStore } from '@/stores/AdminStore';
  import { useAuthStore } from '@/stores/AuthStore';
  
  const adminStore = useAdminStore();
  const authStore = useAuthStore();
  
  const editMode = ref(false);
  const categoryForm = ref({ id: null, name: '' });
  
  const isDirector = computed(() => authStore.user?.role === 'DIRECTOR');
  
  onMounted(async () => {
    if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
      adminStore.error = 'Bạn không có quyền truy cập trang này';
      return;
    }
    await adminStore.fetchCategories();
  });
  
  const editCategory = (category) => {
    editMode.value = true;
    categoryForm.value = { id: category.id, name: category.name };
  };
  
  const cancelEdit = () => {
    editMode.value = false;
    categoryForm.value = { id: null, name: '' };
  };
  
  const submitCategory = async () => {
    const { id, name } = categoryForm.value;
    let success;
    if (editMode.value) {
      success = await adminStore.updateCategory(id, name);
    } else {
      success = await adminStore.createCategory(name);
    }
    if (success) {
      cancelEdit();
      await adminStore.fetchCategories();
    }
  };
  
  const updateCategoryStatus = async (category) => {
    if (!isDirector.value) return;
    const success = await adminStore.changeCategoryStatus(category.id, category.name , category.status);
    if (success) {
      console.log(`Cập nhật trạng thái danh mục ${category.name} thành ${category.status}`);
    } else {
      // Nếu thất bại, có thể gọi lại fetchCategories để đồng bộ
      await adminStore.fetchCategories();
    }
  };
  </script>
  
  <style scoped>
  .card {
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  }
  .table th, .table td {
    vertical-align: middle;
  }
  .form-select {
    width: auto;
    display: inline-block;
  }
  </style>