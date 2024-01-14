<script setup lang="ts">

// 定义异步登录函数
async function toLogin() {
  // 使用导航函数跳转至'/Login'
  await navigateTo('/Login')
}

</script>

<template>
  <!-- 页面主体部分 -->
  <section>
    <!-- 显示当前年份和作品信息 -->
    {{ new Date().getFullYear() }} — <strong>终南一梦组作品</strong>
    <!-- 点击按钮触发登录函数 -->
    <button @click="toLogin">toLogin</button>

    <!-- 使用NuxtLink进行路由跳转至'/login' -->
    <NuxtLink to="/login">
      toLogin
    </NuxtLink>
  </section>
</template>


<style scoped>

</style>