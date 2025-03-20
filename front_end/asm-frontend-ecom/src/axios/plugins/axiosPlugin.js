import axiosInstance from "../axios.js";

export default {
  install: (app) => {
    app.config.globalProperties.$axios = axiosInstance; // Gắn vào global properties
  },
};
