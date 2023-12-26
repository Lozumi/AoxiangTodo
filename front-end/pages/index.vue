<template>
  <v-app style="height: 100%;">
  <TodoPage />
  </v-app>
</template>

<script>

import {defineComponent} from "vue";
import TodoPage from "~/pages/TodoPage.vue";

export default defineComponent({
  components: {TodoPage}
})
</script>

<style scoped>
/* 可以添加一些自定义的样式 */
</style>
