<<<<<<< HEAD
<template>
  <div class="todo-app">
    <header>
      <h1>翱翔清单</h1>
      <p>基于Nuxt.js的待办清单软件</p>
    </header>
    <section class="team-info">
      <h2>终南一梦队作品</h2>
      <p>组长: 刘黎可</p>
      <p>前端: 刘黎可、徐薏茜、韩紫琪、于李源、傅钰</p>
      <p>后端: 贾聪毅、李振国</p>
      <p>服务端: 张书畅</p>
    </section>
    <section class="project-links">
      <h2>项目链接</h2>
      <p>项目仓库: <a href="https://github.com/Lozumi/AoxiangTodo" target="_blank">https://github.com/Lozumi/AoxiangTodo</a></p>
      <p>部署方法: 请见项目仓库内根目录下的 <a href="https://github.com/Lozumi/AoxiangTodo/blob/master/README.md" target="_blank">README.md</a></p>
      <p>使用方法: 请见项目仓库docs目录下的 <a href="https://github.com/Lozumi/AoxiangTodo/blob/master/docs/用户手册.md" target="_blank">用户手册</a></p>
    </section>
    <!-- 在这里编写你的待办清单组件 -->
  </div>
</template>

<script setup lang="ts">
// 在这里导入组件和逻辑
</script>

<style scoped>
/* 在这里添加样式 */
.todo-app {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
}

header {
  text-align: center;
  margin-bottom: 20px;
}

.team-info {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.team-info h2 {
  margin-bottom: 10px;
}

.project-links {
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 8px;
  margin-bottom: 20px;
}
.project-links h2 {
  margin-bottom: 10px;
}

.project-links p {
  margin: 5px 0;
}

.project-links a {
  color: #0066cc;
}
</style>
