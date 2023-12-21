<template>
  <v-app>
    <v-app-bar color="#3A8FB7" app absolute elevate-on-scroll>
<!--      <img src="/npu-logo.png" alt="NPU"  class="ml-2" style="width: 36px; height: 36px">-->
      <v-toolbar-title class="white--text text-center mx-2"><strong>食在工大</strong></v-toolbar-title>
      <v-spacer/>
      <v-btn to="/" icon class="mx-2">
        <v-icon>home</v-icon>
      </v-btn>

    </v-app-bar>

    <v-main class="bg-fixed" >
      <v-container>
        <v-row class="justify-center">
          <v-col cols="12">
            <v-card>
              <v-card-text class="text-center">
                <p class="nputitle">
                  <strong>食在工大——食堂订餐配送系统</strong>
                </p>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-text class="text-center">
                <p>
                  西北工业大学软件工程导论课程（2023-2024秋）大作业项目，由Dobug小组作品出品。<br>
                  项目前端采用Vue 3框架，引入Tailwind CSS美化组件，利用Nuxt、Vuex技术实现快速构建。<br>
                  因时间、技术有限，仅实现了部分前端功能（主页、餐厅菜单、购物车），餐厅、菜品使用字符串存储。
                </p>
              </v-card-text>
              <v-divider></v-divider>
              <v-card-actions class="justify-center">
                <div class="flex items-center">
                  <div class="github-badge" style="margin-right: 10px;">
                    <a href="https://www.github.com/Lozumi" target="_blank" title="©2018-2022 洛衣Lozumi" class="white-text">
                      <span class="badge-subject">Copyright</span><span class="badge-value bg-tianyi">©2018-2023 Lozumi</span>
                    </a>
                  </div>
                  <div class="github-badge" style="margin-right: 10px;">
                    <a href="http://beian.miit.gov.cn/" target="_blank" title="鄂ICP备 19021090号-1" class="white-text">
                      <span class="badge-subject">鄂ICP备</span><span class="badge-value bg-miku">19021090号-1</span>
                    </a>
                  </div>
                  <div class="github-badge">
                    <a href="./" target="_blank" title="版本 V1.0.0" class="white-text">
                      <span class="badge-subject">部署版本</span><span class="badge-value bg-orange">V1.0.0</span>
                    </a>
                  </div>
                </div>

              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>

    </v-main>


    <v-footer dark absolute bottom="0" left="0" right="0" padless>
      <v-card color="#3A8FB7" flat class="w-full white--text text-center">
        <v-card-text>
          <v-btn v-for="platform in socialIcons" :key="platform.icon"
                 class="mx-4 white--text" icon
                 :href="platform.url" rel="noopener"
          >
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

export default {
  data() {
    return {
      socialIcons: [
        { icon: 'mdi-qqchat', url: '#' },
        { icon: 'mdi-wechat', url: '#' },
        { icon: 'mdi-sina-weibo', url: '#' },
        { icon: 'mdi-github', url: 'https://www.github.com/Lozumi/NPUFood-Nuxt' },
      ],
    }
  },
}
</script>

<style scoped>
.nputitle{
  justify-content: center;
  font-size: 24px;
}
.flex {
  display: flex;
  justify-content: center;
}
.items-center {
  align-items: center;
}

/*底部页脚*/
.github-badge {
  display: inline-block;
  border-radius: 4px;
  text-shadow: none;
  font-size: 12px;
  color: #fff;
  line-height: 15px;
  background-color: #abbac3;
  margin-bottom: 5px
}

.github-badge .badge-subject {
  display: inline-block;
  background-color: #4d4d4d;
  padding: 4px 4px 4px 6px;
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px
}

.github-badge .badge-value {
  display: inline-block;
  padding: 4px 6px 4px 4px;
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px
}

.github-badge .bg-orange {
  background-color: #ffa500
}

.github-badge .bg-tianyi {
  background-color: #66CCFF;
}

.github-badge .bg-miku {
  background-color: #39C5BB;
}

.white-text {
  color: white;
}

/* Center content in the footer */
.v-footer {
  position: absolute;
  width: 100%;
  bottom: 0;
}

</style>
