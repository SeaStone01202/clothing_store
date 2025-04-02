import { defineStore } from 'pinia';
import axiosInstance from '@/axios/axiosInstance';

export const useProductStore = defineStore('product', {
  state: () => ({
    products: [],
    categories: [], // Thay vì hardcode, sẽ lấy từ API
    selectedCategory: 'Tất cả',
    selectedPriceRange: 'Tất cả',
    priceRanges: ['Tất cả', 'Dưới 200k', 'Dưới 400k', 'Dưới 600k', 'Dưới 1000k'],
    searchKeyword: '',
    currentPage: 1,
    totalPages: 1,
    itemsPerPage: 6,
    loading: false,
    error: null,
    productDetail: null,
    relatedProducts: [],
    relatedProductsPage: 1,
    relatedProductsTotalPages: 1,
  }),

  actions: {
    // Thêm action để lấy danh sách danh mục
    async fetchCategories() {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get('/category/status');
        if (response.data.status === 200) {
          // Lấy danh sách danh mục từ API và thêm "Tất cả" vào đầu
          this.categories = ['Tất cả', ...response.data.data.map(cat => cat.name)];
          console.log('Fetched categories:', this.categories);
        } else {
          throw new Error(response.data.message);
        }
      } catch (error) {
        this.error = error.response?.data?.message || 'Lỗi khi tải danh sách danh mục';
        console.error('Error fetching categories:', error);
        // Fallback về danh sách hardcode nếu lỗi
        this.categories = ['Tất cả', 'Áo', 'Quần', 'Giày', 'Phụ Kiện'];
      } finally {
        this.loading = false;
      }
    },

    async fetchProducts(page = 1) {
      this.loading = true;
      this.error = null;
      try {
        let url = '/product';
        const params = { page: page };

        if (this.searchKeyword.trim() !== '') {
          url = '/product/search';
          params.keyword = this.searchKeyword;
        } else {
          if (this.selectedPriceRange !== 'Tất cả') {
            let minPrice = 0;
            let maxPrice;
            switch (this.selectedPriceRange) {
              case 'Dưới 200k':
                maxPrice = 200000;
                break;
              case 'Dưới 400k':
                maxPrice = 400000;
                break;
              case 'Dưới 600k':
                maxPrice = 600000;
                break;
              case 'Dưới 1000k':
                maxPrice = 1000000;
                break;
              default:
                maxPrice = Infinity;
            }
            params.min = minPrice;
            params.max = maxPrice;

            if (this.selectedCategory !== 'Tất cả') {
              url = `/product/category/price/${this.selectedCategory}`;
            } else {
              url = '/product/price';
            }
          } else if (this.selectedCategory !== 'Tất cả') {
            url = `/product/category/${this.selectedCategory}`;
          }
        }

        const response = await axiosInstance.get(url, { params });

        if (response.data.status === 200) {
          this.products = response.data.data.content.map(product => ({
            id: product.id,
            name: product.name,
            price: parseFloat(product.price),
            image: product.imageUrl,
            category: product.category,
            description: product.description,
            stock: product.stock,
          }));
          this.totalPages = response.data.data.totalPages;
          this.currentPage = page;
        } else {
          throw new Error('Không thể lấy danh sách sản phẩm');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi lấy sản phẩm';
        console.error('Error fetching products:', error);
      } finally {
        this.loading = false;
      }
    },

    async fetchProductDetail(id) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get(`/product/${id}`);
        if (response.data.status === 200) {
          this.productDetail = {
            id: response.data.data.id,
            name: response.data.data.name,
            price: parseFloat(response.data.data.price),
            image: response.data.data.imageUrl,
            category: response.data.data.category,
            description: response.data.data.description,
            stock: response.data.data.stock,
          };
        } else {
          throw new Error('Không thể lấy chi tiết sản phẩm');
        }
      } catch (error) {
        this.error = error.message || 'Sản phẩm không tồn tại';
        this.productDetail = null;
        console.error('Error fetching product detail:', error);
      } finally {
        this.loading = false;
      }
    },

    async fetchRelatedProducts(id, category, page = 1) {
      this.loading = true;
      this.error = null;
      try {
        const response = await axiosInstance.get(`/product/${id}/related`, {
          params: { category, page }
        });
        if (response.data.status === 200) {
          this.relatedProducts = response.data.data.content.map(product => ({
            id: product.id,
            name: product.name,
            price: parseFloat(product.price),
            image: product.imageUrl,
            category: product.category,
            description: product.description,
            stock: product.stock,
          }));
          this.relatedProductsTotalPages = response.data.data.totalPages;
          this.relatedProductsPage = page;
        } else {
          throw new Error('Không thể lấy sản phẩm liên quan');
        }
      } catch (error) {
        this.error = error.message || 'Đã có lỗi xảy ra khi lấy sản phẩm liên quan';
        console.error('Error fetching related products:', error);
      } finally {
        this.loading = false;
      }
    },

    setSelectedCategory(category) {
      this.selectedCategory = category;
      this.currentPage = 1;
      this.fetchProducts(this.currentPage);
    },

    setSelectedPriceRange(priceRange) {
      this.selectedPriceRange = priceRange;
      this.currentPage = 1;
      this.fetchProducts(this.currentPage);
    },

    setSearchKeyword(keyword) {
      this.searchKeyword = keyword;
      this.currentPage = 1;
      this.fetchProducts(this.currentPage);
    },

    clearSearch() {
      this.searchKeyword = '';
      this.currentPage = 1;
      this.fetchProducts(this.currentPage);
    },

    setPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
        this.fetchProducts(page);
      }
    },

    setRelatedProductsPage(page) {
      if (page >= 1 && page <= this.relatedProductsTotalPages) {
        this.relatedProductsPage = page;
        this.fetchRelatedProducts(this.productDetail.id, this.productDetail.category, page);
      }
    },
  },

  getters: {
    filteredProducts(state) {
      return state.products;
    },

    paginatedProducts(state) {
      return state.filteredProducts;
    },
  },
});