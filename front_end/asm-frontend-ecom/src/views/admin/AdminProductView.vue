<template>
    <div class="container mt-5">
      <h1 class="text-center text-primary fw-bold mb-4">🛍 Quản lý sản phẩm</h1>
  
      <div v-if="loading" class="text-center mb-4">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Đang tải...</span>
        </div>
      </div>
      <div v-if="error" class="alert alert-danger text-center" role="alert">
        {{ error }}
      </div>
  
      <div class="card">
        <div class="card-body">
          <button v-if="user.role === 'DIRECTOR'" class="btn btn-success mb-3" @click="addProduct">
            Thêm sản phẩm
          </button>
          <table class="table table-striped">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="product in products" :key="product.id">
                <td>{{ product.id }}</td>
                <td>{{ product.name }}</td>
                <td>{{ product.price.toLocaleString() }} VND</td>
                <td>
                  <input v-model="product.quantity" type="number" class="form-control w-50 d-inline" @change="updateProduct(product)">
                </td>
                <td>
                  <button v-if="user.role === 'DIRECTOR'" class="btn btn-danger btn-sm" @click="deleteProduct(product.id)">
                    Xóa
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { useAuthStore } from '@/stores/AuthStore';
  
  const authStore = useAuthStore();
  const loading = ref(false);
  const error = ref(null);
  const user = ref(authStore.user || {});
  const products = ref([]);
  
  onMounted(async () => {
    if (!authStore.isAuthenticated() || !['STAFF', 'DIRECTOR'].includes(authStore.user?.role)) {
      error.value = 'Bạn không có quyền truy cập trang này';
      return;
    }
  
    loading.value = true;
    try {
      // Giả lập lấy danh sách sản phẩm
      // const response = await axiosInstance.get('/admin/products');
      // products.value = response.data.data;
      products.value = [
        { id: 1, name: 'Áo thun', price: 150000, quantity: 50 },
        { id: 2, name: 'Quần jeans', price: 200000, quantity: 30 }
      ];
    } catch (err) {
      error.value = 'Lỗi khi tải danh sách sản phẩm';
    } finally {
      loading.value = false;
    }
  });
  
  const updateProduct = async (product) => {
    try {
      // Gọi API cập nhật sản phẩm
      // await axiosInstance.put(`/admin/products/${product.id}`, product);
      console.log(`Cập nhật sản phẩm ${product.id}: ${product.quantity}`);
    } catch (err) {
      error.value = 'Lỗi khi cập nhật sản phẩm';
    }
  };
  
  const deleteProduct = async (id) => {
    try {
      // Gọi API xóa sản phẩm
      // await axiosInstance.delete(`/admin/products/${id}`);
      products.value = products.value.filter(p => p.id !== id);
      console.log(`Xóa sản phẩm ${id}`);
    } catch (err) {
      error.value = 'Lỗi khi xóa sản phẩm';
    }
  };
  
  const addProduct = () => {
    // Chuyển hướng hoặc mở modal thêm sản phẩm
    console.log('Mở form thêm sản phẩm');
  };
  </script>
  
  <style scoped>
  .table {
    width: 100%;
  }
  </style>