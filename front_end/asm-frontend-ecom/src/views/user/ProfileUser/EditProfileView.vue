<!-- src/views/user/EditProfileView.vue -->
<template>
  <div class="container mt-5">
    <h1 class="text-center text-primary fw-bold mb-4">✍️ Quản lý hồ sơ</h1>

    <ul class="nav nav-tabs mb-4" id="profileTab" role="tablist">
      <li class="nav-item" role="presentation">
        <button
          class="nav-link active"
          id="profile-tab"
          data-bs-toggle="tab"
          data-bs-target="#profile"
          type="button"
          role="tab"
          aria-controls="profile"
          aria-selected="true"
        >
          Thông tin cá nhân
        </button>
      </li>
      <li class="nav-item" role="presentation">
        <button
          class="nav-link"
          id="address-info-tab"
          data-bs-toggle="tab"
          data-bs-target="#address-info"
          type="button"
          role="tab"
          aria-controls="address-info"
          aria-selected="false"
        >
          Thông tin địa chỉ
        </button>
      </li>
    </ul>

    <div class="tab-content" id="profileTabContent">
      <!-- Tab chỉnh sửa hồ sơ -->
      <div
        class="tab-pane fade show active"
        id="profile"
        role="tabpanel"
        aria-labelledby="profile-tab"
      >
        <div class="card p-4">
          <h3 class="mb-3">Chỉnh sửa thông tin cá nhân</h3>
          <form @submit.prevent="saveProfile">
            <div class="mb-3">
              <label for="fullname" class="form-label">Họ và tên</label>
              <input
                type="text"
                class="form-control"
                id="fullname"
                v-model="profile.fullname"
                required
              />
            </div>
            <div class="mb-3">
              <label for="email" class="form-label">Email</label>
              <input
                type="email"
                class="form-control"
                id="email"
                v-model="profile.email"
                required
                readonly
              />
            </div>
            <div class="mb-3">
              <label for="phone" class="form-label">Số điện thoại</label>
              <input
                type="text"
                class="form-control"
                id="phone"
                v-model="profile.phone"
                required
              />
            </div>
            <div class="mb-3">
              <label for="image" class="form-label">Ảnh đại diện</label>
              <input
                type="file"
                class="form-control"
                id="image"
                @change="onFileChange"
                accept="image/*"
              />
              <img
                v-if="profile.imageUrl"
                :src="profile.imageUrl"
                alt="Preview"
                class="mt-2 img-thumbnail"
                style="max-width: 200px;"
              />
            </div>
            <div v-if="errorMessage" class="alert alert-danger py-2 text-center">
              {{ errorMessage }}
            </div>
            <button
              type="submit"
              class="btn btn-primary w-100"
              :disabled="isProfileLoading"
            >
              <span v-if="isProfileLoading" class="spinner-border spinner-border-sm me-2"></span>
              {{ isProfileLoading ? "Đang lưu..." : "💾 Lưu thay đổi" }}
            </button>
          </form>
        </div>
      </div>

      <!-- Tab thông tin địa chỉ -->
      <div
        class="tab-pane fade"
        id="address-info"
        role="tabpanel"
        aria-labelledby="address-info-tab"
      >
        <div class="card p-4">
          <h3 class="mb-3">Thông tin địa chỉ</h3>

          <!-- Phần danh sách địa chỉ -->
          <div class="mb-5">
            <h4 class="mb-3">Danh sách địa chỉ hiện có</h4>
            <div v-if="errorMessage" class="alert alert-danger py-2 text-center">
              {{ errorMessage }}
            </div>
            <div v-if="isAddressListLoading" class="text-center">
              <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Đang tải...</span>
              </div>
            </div>
            <div v-else-if="addresses.length === 0" class="text-center">
              <p>Chưa có địa chỉ nào.</p>
            </div>
            <div v-else class="list-group">
              <div v-for="addr in addresses" :key="addr.id" class="list-group-item mb-2">
                <p><strong>Địa chỉ:</strong> {{ addr.addressLine }}, {{ addr.ward }}, {{ addr.district }}, {{ addr.city }}</p>
                <p><strong>Mặc định:</strong> {{ addr.isDefault ? "Có" : "Không" }}</p>
                <p><strong>Trạng thái:</strong> {{ addr.status }}</p>
              </div>
            </div>
          </div>

          <!-- Phần thêm địa chỉ mới -->
          <div>
            <h4 class="mb-3">Thêm địa chỉ mới</h4>
            <form @submit.prevent="saveAddress">
              <div class="mb-3">
                <label for="city" class="form-label">Thành phố</label>
                <select
                  class="form-select"
                  id="city"
                  v-model="address.city"
                  @change="updateDistricts"
                  required
                >
                  <option value="" disabled>Chọn thành phố</option>
                  <option v-for="province in provinces" :key="province.Code" :value="province.Name">
                    {{ province.Name }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="district" class="form-label">Quận/Huyện</label>
                <select
                  class="form-select"
                  id="district"
                  v-model="address.district"
                  @change="updateWards"
                  required
                  :disabled="!address.city"
                >
                  <option value="" disabled>Chọn quận/huyện</option>
                  <option v-for="district in filteredDistricts" :key="district.Code" :value="district.Name">
                    {{ district.Name }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="ward" class="form-label">Phường/Xã</label>
                <select
                  class="form-select"
                  id="ward"
                  v-model="address.ward"
                  required
                  :disabled="!address.district"
                >
                  <option value="" disabled>Chọn phường/xã</option>
                  <option v-for="ward in filteredWards" :key="ward.Code" :value="ward.Name">
                    {{ ward.Name }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="addressLine" class="form-label">Địa chỉ chi tiết</label>
                <input
                  type="text"
                  class="form-control"
                  id="addressLine"
                  v-model="address.addressLine"
                  required
                />
              </div>
              <div class="mb-3 form-check">
                <input
                  type="checkbox"
                  class="form-check-input"
                  id="isDefault"
                  v-model="address.isDefault"
                />
                <label class="form-check-label" for="isDefault">Đặt làm địa chỉ mặc định</label>
              </div>
              <div v-if="errorMessage" class="alert alert-danger py-2 text-center">
                {{ errorMessage }}
              </div>
              <button
                type="submit"
                class="btn btn-primary w-100"
                :disabled="isAddressLoading"
              >
                <span v-if="isAddressLoading" class="spinner-border spinner-border-sm me-2"></span>
                {{ isAddressLoading ? "Đang lưu..." : "💾 Lưu địa chỉ" }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useAuthStore } from "@/stores/AuthStore";
import { useUserStore } from "@/stores/UserStore";
import vietnamAddress from "@/data/vietnam-address.json";

const authStore = useAuthStore();
const userStore = useUserStore();

const profile = ref({
  fullname: "",
  email: "",
  phone: "",
  image: null,
  imageUrl: "",
});

const address = ref({
  addressLine: "",
  ward: "",
  district: "",
  city: "",
  isDefault: false,
});

const addresses = ref([]);
const errorMessage = ref("");
const isProfileLoading = ref(false);
const isAddressLoading = ref(false);
const isAddressListLoading = ref(false);

const provinces = ref(vietnamAddress);

const filteredDistricts = computed(() => {
  const selectedProvince = provinces.value.find((province) => province.Name === address.value.city);
  return selectedProvince ? selectedProvince.District : [];
});

const filteredWards = computed(() => {
  const selectedProvince = provinces.value.find((province) => province.Name === address.value.city);
  if (!selectedProvince) return [];
  const selectedDistrict = selectedProvince.District.find(
    (district) => district.Name === address.value.district
  );
  return selectedDistrict ? selectedDistrict.Ward : [];
});

const updateDistricts = () => {
  address.value.district = "";
  address.value.ward = "";
};

const updateWards = () => {
  address.value.ward = "";
};

onMounted(async () => {
  if (!authStore.user) {
    await authStore.fetchUserInfo();
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
    userStore.setUser(user);

    // Lấy danh sách địa chỉ
    isAddressListLoading.value = true;
    await userStore.fetchAddresses(user.email);
    addresses.value = userStore.getAddresses;
    errorMessage.value = userStore.error || "";
    isAddressListLoading.value = false;
  } else {
    errorMessage.value = "Không tìm thấy thông tin người dùng!";
  }
});

const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    profile.value.image = file;
    profile.value.imageUrl = URL.createObjectURL(file);
  }
};

const saveProfile = async () => {
  isProfileLoading.value = true;
  errorMessage.value = "";
  try {
    const result = await userStore.updateUser({
      email: profile.value.email,
      fullname: profile.value.fullname,
      phone: profile.value.phone,
      image: profile.value.image,
    });
    if (result.success) {
      alert(result.message);
      profile.value.imageUrl = userStore.getUser?.image || profile.value.imageUrl;
    } else {
      errorMessage.value = result.message;
    }
  } catch (error) {
    errorMessage.value = "Có lỗi xảy ra khi cập nhật hồ sơ!";
  } finally {
    isProfileLoading.value = false;
  }
};

const saveAddress = async () => {
  isAddressLoading.value = true;
  errorMessage.value = "";
  try {
    const result = await userStore.addAddress({
      addressLine: address.value.addressLine,
      ward: address.value.ward,
      district: address.value.district,
      city: address.value.city,
      isDefault: address.value.isDefault,
    });
    if (result.success) {
      alert(result.message);
      address.value = { addressLine: "", ward: "", district: "", city: "", isDefault: false };
      // Làm mới danh sách địa chỉ
      isAddressListLoading.value = true;
      await userStore.fetchAddresses(profile.value.email);
      addresses.value = userStore.getAddresses;
      isAddressListLoading.value = false;
    } else {
      errorMessage.value = result.message;
    }
  } catch (error) {
    errorMessage.value = "Có lỗi xảy ra khi thêm địa chỉ!";
  } finally {
    isAddressLoading.value = false;
  }
};
</script>

<style scoped>
.card {
  max-width: 800px;
  margin: 0 auto;
  border-top: 4px solid #007bff;
}
.list-group-item {
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f8f9fa;
}
h4 {
  color: #007bff;
  font-weight: 600;
}
</style>