<template>
  <v-app>
    <v-app-bar color="#3A8FB7" app absolute elevate-on-scroll>
      <v-toolbar-title class="white--text text-center mx-2"><strong>食在工大</strong></v-toolbar-title>
      <v-spacer />
      <v-btn to="/" icon class="mx-2">
        <v-icon>home</v-icon>
      </v-btn>
    </v-app-bar>

    <v-main class="bg-fixed">
      <v-container>
        <v-row>
          <!-- 左侧导航栏 -->
          <v-col cols="3">
            <v-card>
              <v-card-title>导航栏</v-card-title>
              <v-list>
                <v-list-item @click="changePage('today')">
                  <v-list-item-icon>
                    <v-icon>mdi-calendar-today</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>当日待办</v-list-item-content>
                </v-list-item>

                <v-list-item @click="changePage('recent')">
                  <v-list-item-icon>
                    <v-icon>mdi-history</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>最近待办</v-list-item-content>
                </v-list-item>

                <v-list-item @click="changePage('overview')">
                  <v-list-item-icon>
                    <v-icon>mdi-chart-bar</v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>待办概览</v-list-item-content>
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>

          <!-- 右侧页面 -->
          <v-col cols="9">
            <component :is="selectedPageComponent" />
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <v-footer dark absolute bottom="0" left="0" right="0" padless>
      <v-card color="#3A8FB7" flat class="w-full white--text text-center">
        <v-card-text>
          <v-btn v-for="platform in socialIcons" :key="platform.icon" class="mx-4 white--text" icon :href="platform.url" rel="noopener">
            <v-icon size="24px">{{ platform.icon }}</v-icon>
          </v-btn>
        </v-card-text>

        <v-card-text class="white--text">
          西北工业大学 软件工程导论课程（2023-2024秋）大作业
          <br>
          Dobug小组作品
        </v-card-text>
      </v-card>
    </v-footer>
  </v-app>
</template>

<script>
import TodayTasks from '~/components/TodayTasks.vue'; // 路径根据实际情况修改
import RecentTasks from '~/components/RecentTasks.vue'; // 路径根据实际情况修改
import OverviewTasks from '~/components/OverviewTasks.vue'; // 路径根据实际情况修改

export default {
  data() {
    return {
      socialIcons: [
        { icon: 'mdi-qqchat', url: '#' },
        { icon: 'mdi-wechat', url: '#' },
        { icon: 'mdi-sina-weibo', url: '#' },
        { icon: 'mdi-github', url: 'https://www.github.com/Lozumi/NPUFood-Nuxt' },
      ],
      selectedPage: 'today', // 默认显示当日待办页面
    };
  },
  computed: {
    selectedPageComponent() {
      // 根据选择的页面返回对应的组件
      switch (this.selectedPage) {
        case 'today':
          return TodayTasks;
        case 'recent':
          return RecentTasks;
        case 'overview':
          return OverviewTasks;
        default:
          return null;
      }
    },
  },
  methods: {
    changePage(value) {
      // 更新选择的页面
      this.selectedPage = value;
    },
  },
};
</script>

<style scoped>
/* Center content in the footer */
.v-footer {
  position: absolute;
  width: 100%;
  bottom: 0;
}

/* Make the footer child elements take up the full width */
.v-footer .w-full {
  width: 100%;
}
</style>
