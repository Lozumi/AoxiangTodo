<template>
  <v-app style="height: 100%;">
    <!-- 顶部导航栏 -->
    <v-app-bar :elevation="2" app :color="selectedTheme.primaryColor" :dark="selectedTheme.darkTopBar">
      <v-icon class="mx-4 blue--text">mdi-airplane-takeoff</v-icon>
      <v-toolbar-title>翱翔清单</v-toolbar-title>
      <v-btn @click="toggleTheme" text>
        <v-icon>mdi-palette</v-icon>
        主题
      </v-btn>
      <v-btn v-if="isLogin" @click="userLogout" text>
        <v-icon>mdi-logout</v-icon>
        注销
      </v-btn>
    </v-app-bar>

    <!-- 侧面导航栏 -->
    <v-navigation-drawer
        :width="233" app
        :color="selectedTheme.primaryColor"
        :dark="selectedTheme.darkSideNav"
        theme="light"
    >
      <v-container class="pa-4">

        <v-row align="center">
          <v-col cols="auto">
            <v-avatar size="20">
              <v-icon>mdi-account</v-icon>
            </v-avatar>
          </v-col>
          <v-col>
            <v-row>
              <v-col>

                <p style="font-weight: bold">{{ isLogin ? `亲爱的 ${currentUser}` : '请登录' }}</p>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <p>欢迎使用翱翔清单</p>
              </v-col>
            </v-row>
          </v-col>
        </v-row>

      </v-container>
      <v-list density="compact" nav>
<!--        <v-list-item v-if="isLogin" :title="`亲爱的 ${currentUser}`" subtitle="欢迎使用翱翔清单！"></v-list-item>-->
<!--        <v-list-item v-if="!isLogin" :title="`请登录`" subtitle="欢迎使用翱翔清单！"></v-list-item>-->
        <!--        <v-list-item class="nav-item" @click="$router.push('/TodoPage/TodayTasks')">-->
        <!--          <v-list-item-title class="white&#45;&#45;text">今日待办</v-list-item-title>-->
        <!--        </v-list-item>-->
<!--        <v-divider class="white&#45;&#45;text"></v-divider>-->
        <v-divider></v-divider>
        <v-list-item prepend-icon="mdi-clipboard-list" class="nav-item mt-2 mb-2" @click="$router.push('/TodoPage/Todo')">
          <v-list-item-title >最近待办</v-list-item-title>
        </v-list-item>
        <v-divider></v-divider>
<!--        <v-divider class="white&#45;&#45;text"></v-divider>-->
        <v-list-item prepend-icon="mdi-briefcase-clock" class="nav-item mt-2 mb-2" @click="$router.push('/TodoPage/TomatoClock')">
          <v-list-item-title >番茄时钟</v-list-item-title>
        </v-list-item>
        <v-divider></v-divider>
<!--        <v-divider class="white&#45;&#45;text"></v-divider>-->
        <v-list-item prepend-icon="mdi-information" class="nav-item mt-2 mb-2" @click="$router.push('/TodoPage/About')">
          <v-list-item-title >关于团队</v-list-item-title>
        </v-list-item>
      </v-list>
      <template v-slot:append>
        <div class="pa-2">
          <v-btn block>
            这是彩蛋
          </v-btn>
        </div>
      </template>
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
import AccountRequest from "~/composables/AccountRequest";
import {ref} from 'vue';

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
    currentUser: '',
    isLogin: false,
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
    userLogout() {
      AccountRequest.userLogout()
          .then(response => {
            const { status, message, content} = JSON.parse(response.data.value);

            if (status === "Success") {
              console.error("注销成功");
              this.isLogin = false;
              this.$router.push('/TodoPage/Login'); // 跳转到登录页面
            } else {
              console.error("注销失败");
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
  created() {
    AccountRequest.getCurrentUser()
        .then(response => {
          console.log(response);
          const { status, message, content} = JSON.parse(response.data.value);
          console.log(status);
          console.log(content);
          if (status === "Success") {
            this.isLogin = true;
            this.currentUser = content;
            setTimeout(() => {
              this.$router.push('/TodoPage/Todo');
            }, 1000); // 等待1秒
          } else {
            setTimeout(() => {
              this.$router.push('/TodoPage/Login');
            }, 1000); // 等待1秒
          }
        })
        .catch(error => {
          console.error(error);
        });
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
