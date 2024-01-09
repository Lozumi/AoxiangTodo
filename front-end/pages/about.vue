<script setup lang="ts">
async function toLogin() {
  // this.$router.push('/Login')
  await navigateTo('/Login')
}

</script>

<template>
  <section>
    <p>此页面将显示在 /about 路由。</p>
    <button @click="toLogin">toLogin</button>


    <NuxtLink to="/login">
      toLogin
    </NuxtLink>
  </section>
</template>


<style scoped>

</style>