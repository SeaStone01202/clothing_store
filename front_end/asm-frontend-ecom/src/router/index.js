import CartView from '@/views/user/Cart/CartView.vue';
import ForgotPasswordView from '@/views/user/ForgotPassword/ForgotPasswordView.vue';
import HomeView from '@/views/user/HomePage/HomeView.vue';
import LoginView from '@/views/user/Login/LoginView.vue';
import OrderHistoryView from '@/views/user/OrderHistory/OrderHistoryView.vue';
import ProductDetail from '@/views/user/ProductDetail/ProductDetailView.vue';
import EditProfileView from '@/views/user/ProfileUser/EditProfileView.vue';
import RegisterView from '@/views/user/Register/RegisterView.vue';
import CallbackView from '@/views/user/Login/CallbackView.vue';

import AdminDashboard from '@/views/admin/AdminDashboard.vue';
import AdminOrderView from '@/views/admin/AdminOrderView.vue';
import AdminProductView from '@/views/admin/AdminProductView.vue';
import AdminUserView from '@/views/admin/AdminUserView.vue';
import AdminCategoryView from '@/views/admin/AdminCategoryView.vue';

import { createRouter, createWebHistory } from 'vue-router';


const routes = [
  { path: '/', component: HomeView },
  { path: '/product/:id', component: ProductDetail },
  { path: '/cart', component: CartView },
  { path: '/login', component: LoginView },
  { path: '/register', component: RegisterView },
  { path: '/forgot-password', component: ForgotPasswordView },
  { path: '/edit-profile', component: EditProfileView },
  { path: '/order-history', component: OrderHistoryView },
  { path: '/callback', name: 'Callback', component: CallbackView },

  { path: '/AdminDashboard', component: AdminDashboard },
  { path: '/AdminOrderView', component: AdminOrderView },
  { path: '/AdminProductView', component: AdminProductView},
  { path: '/AdminUserView', component: AdminUserView},
  { path: '/AdminCategoryView', component: AdminCategoryView},
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
