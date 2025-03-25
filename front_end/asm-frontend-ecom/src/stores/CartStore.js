import { defineStore } from 'pinia';
import axiosInstance from '@/axios/axiosInstance';
import { useAuthStore } from '@/stores/AuthStore';

export const useCartStore = defineStore('cart', {
  state: () => ({
    cart: null,
    loading: false,
    error: null,
  }),

  actions: {
    async fetchCart() {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !authStore.user) {
        this.error = 'Vui lòng đăng nhập để xem giỏ hàng';
        return;
      }

      this.loading = true;
      this.error = null;
      try {
        const email = authStore.user.email;
        const response = await axiosInstance.get('/cart', { params: { email } });
        if (response.data.status === 200) {
          this.cart = response.data.data || { cartDetails: [] };
          if (!this.cart.cartDetails) {
            this.cart.cartDetails = [];
          }
        } else {
          throw new Error(response.data.message || 'Không thể lấy giỏ hàng');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi lấy giỏ hàng';
        console.error('Error fetching cart:', error);
        this.cart = { cartDetails: [] };
      } finally {
        this.loading = false;
      }
    },

    async addProductToCart(productId, quantity) {
      const authStore = useAuthStore();
      if (!authStore.isAuthenticated() || !authStore.user) {
        this.error = 'Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng';
        return false;
      }

      this.loading = true;
      this.error = null;
      try {
        const email = authStore.user.email;
        const response = await axiosInstance.post('/cart/details', null, {
          params: { email, productId, quantity },
        });
        if (response.data.status === 200) {
          await this.fetchCart();
          return true;
        } else {
          throw new Error(response.data.message || 'Không thể thêm sản phẩm vào giỏ hàng');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi thêm sản phẩm';
        console.error('Error adding product to cart:', error);
        return false;
      } finally {
        this.loading = false;
      }
    },

    async removeFromCart(cartDetailId) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.delete(`/cart/details/${cartDetailId}`);
        if (response.data.status === 200) {
          this.cart.cartDetails = this.cart.cartDetails.filter(detail => detail.id !== cartDetailId);
        } else {
          throw new Error(response.data.message || 'Không thể xóa sản phẩm khỏi giỏ hàng');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi xóa sản phẩm';
        console.error('Error removing from cart:', error);
        await this.fetchCart();
      } finally {
        this.loading = false;
      }
    },

    async decreaseQuantity(cartDetailId) {
      this.loading = true;
      this.error = null;
      try {
        const item = this.cart.cartDetails.find(detail => detail.id === cartDetailId);
        if (!item) {
          throw new Error('Sản phẩm không tồn tại trong giỏ hàng');
        }
        const newQuantity = item.quantity - 1;
        if (newQuantity <= 0) {
          await this.removeFromCart(cartDetailId);
        } else {
          const response = await axiosInstance.put(`/cart/details/${cartDetailId}`, null, {
            params: { quantity: newQuantity },
          });
          if (response.data.status === 200) {
            await this.fetchCart();
          } else {
            throw new Error(response.data.message || 'Không thể giảm số lượng sản phẩm');
          }
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi giảm số lượng';
        console.error('Error decreasing quantity:', error);
        await this.fetchCart();
      } finally {
        this.loading = false;
      }
    },

    async increaseQuantity(cartDetailId) {
      this.loading = true;
      this.error = null;
      try {
        const item = this.cart.cartDetails.find(detail => detail.id === cartDetailId);
        if (!item) {
          throw new Error('Sản phẩm không tồn tại trong giỏ hàng');
        }
        const newQuantity = item.quantity + 1;
        const response = await axiosInstance.put(`/cart/details/${cartDetailId}`, null, {
          params: { quantity: newQuantity },
        });
        if (response.data.status === 200) {
          await this.fetchCart();
        } else {
          throw new Error(response.data.message || 'Không thể tăng số lượng sản phẩm');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi tăng số lượng';
        console.error('Error increasing quantity:', error);
        await this.fetchCart();
      } finally {
        this.loading = false;
      }
    },
  },

  getters: {
    totalPrice(state) {
      if (!state.cart || !state.cart.cartDetails) return 0;
      return state.cart.cartDetails.reduce((sum, item) => sum + (item.productPrice * item.quantity), 0);
    },

    cartItemCount(state) {
      if (!state.cart || !state.cart.cartDetails) return 0;
      return state.cart.cartDetails.length;
    },
  },
});