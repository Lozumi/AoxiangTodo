<script setup lang="ts">
async function toLogin() {
  // this.$router.push('/Login')
  await navigateTo('/Login')
}

</script>

<template>
  <section>
    {{ new Date().getFullYear() }} — <strong>终南一梦组作品</strong>
    <button @click="toLogin">toLogin</button>


    <NuxtLink to="/login">
      toLogin
    </NuxtLink>
  </section>
</template>


<style scoped>

</style>