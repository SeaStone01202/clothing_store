<template>
    <div>
      <Header/>
      <router-view />
      <Footer/>
    </div>
  </template>
  
<script setup>
  import Footer from './components/Footer.vue';
  import Header from './components/Header.vue';
</script>  