import AdminView from '@/views/admin/AdminView.vue';
import CartView from '@/views/user/Cart/CartView.vue';
import ForgotPasswordView from '@/views/user/ForgotPassword/ForgotPasswordView.vue';
import HomeView from '@/views/user/HomePage/HomeView.vue';
import LoginView from '@/views/user/Login/LoginView.vue';
import OrderHistoryView from '@/views/user/OrderHistory/OrderHistoryView.vue';
import ProductDetail from '@/views/user/ProductDetail/ProductDetailView.vue';
import EditProfileView from '@/views/user/ProfileUser/EditProfileView.vue';
import RegisterView from '@/views/user/Register/RegisterView.vue';
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
  { path: '/admin', component: AdminView }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
