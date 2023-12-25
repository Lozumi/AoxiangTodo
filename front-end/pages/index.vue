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
    <v-navigation-drawer app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkSideNav">
      <v-list dense>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="white--text">用户名</v-list-item-title>
            <v-list-item-subtitle class="white--text">用户信息</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-divider class="white--text"></v-divider>
        <v-list-item link title="今日待办"></v-list-item>
        <v-list-item link title="最近待办"></v-list-item>
        <v-list-item link title="番茄时钟"></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- 内容区域 -->
    <v-main>
      <!-- 此处添加你的页面内容 -->
    </v-main>

    <!-- 页脚 -->
    <v-footer app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkFooter">
      <v-row justify="center" no-gutters>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-github</v-icon>
        </v-btn>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-github</v-icon>
        </v-btn>
        <v-btn icon href="https://github.com/Lozumi" target="_blank" class="mx-4" variant="text">
          <v-icon>mdi-github</v-icon>
        </v-btn>
        <v-col class="text-center mt-4" cols="12">
          {{ new Date().getFullYear() }} — <strong>Vuetify</strong>
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
/* 可以添加一些自定义的样式 */
</style>
