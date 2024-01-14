<template>
  <v-app style="height: 100%;">
    <!-- 顶部导航栏 -->
    <v-app-bar :elevation="2" app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkTopBar">
      <v-icon class="mx-4 blue--text">mdi-clipboard-list</v-icon>
      <v-toolbar-title>翱翔清单</v-toolbar-title>
      <v-btn @click="toggleTheme" text>
        <v-icon>mdi-palette</v-icon>
        主题
      </v-btn>
    </v-app-bar>

    <!-- 侧面导航栏 -->
    <v-navigation-drawer :width="200" app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkSideNav">
      <v-list dense>
        <v-list-item title="亲爱的{{}}}" subtitle="欢迎使用翱翔清单！"></v-list-item>
<!--        <v-list-item class="nav-item" @click="$router.push('/TodoPage/TodayTasks')">-->
<!--          <v-list-item-title class="white&#45;&#45;text">今日待办</v-list-item-title>-->
<!--        </v-list-item>-->
        <v-divider class="white--text"></v-divider>
        <v-list-item class="nav-item" @click="$router.push('/TodoPage/Todo')">
          <v-list-item-title class="white--text">最近待办</v-list-item-title>
        </v-list-item>
        <v-divider class="white--text"></v-divider>
        <v-list-item class="nav-item" @click="$router.push('/TodoPage/TomatoClock')">
          <v-list-item-title class="white--text">番茄时钟</v-list-item-title>
        </v-list-item>
        <v-divider class="white--text"></v-divider>
        <v-list-item class="nav-item" @click="$router.push('/TodoPage/About')">
          <v-list-item-title class="white--text">关于团队</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- 内容区域 -->
    <v-main>
      <NuxtPage/>
    </v-main>

    <!-- 页脚 -->
    <v-footer app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkFooter">
      <v-row justify="center" no-gutters>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-github</v-icon>
        </v-btn>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-qqchat</v-icon>
        </v-btn>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-wechat</v-icon>
        </v-btn>
        <v-col class="text-center mt-4" cols="12">
          {{ new Date().getFullYear() }} — <strong>终南一梦组作品</strong>
        </v-col>
      </v-row>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  data: () => ({
    themes: [
      {
        primaryColor: '#6096B4',
        darkTopBar: false,
        darkSideNav: true,
        darkFooter: false,
      },
      {
        primaryColor: '#93BFCF',
        darkTopBar: false,
        darkSideNav: true,
        darkFooter: false,
      },
      {
        primaryColor: '#BDCDD6',
        darkTopBar: false,
        darkSideNav: true,
        darkFooter: false,
      },
      {
        primaryColor: '#EEE9DA',
        darkTopBar: true,
        darkSideNav: false,
        darkFooter: true,
      },
    ],
    selectedThemeIndex: 0,
  }),
  computed: {
    selectedTheme() {
      return this.themes[this.selectedThemeIndex];
    },
  },
  methods: {
    toggleTheme() {
      // 切换到下一个主题
      this.selectedThemeIndex = (this.selectedThemeIndex + 1) % this.themes.length;
    },
  },
};
</script>

<style scoped>
.nav-item {
  border-bottom: 1px solid #ffffff; /* 边框样式 */
  padding: 12px; /* 间隔 */
  cursor: pointer; /* 添加鼠标指针样式 */
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1); /* 鼠标悬停时的背景色 */
}
</style>
