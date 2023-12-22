<template>

  <v-card>
    <v-row no-gutters>
      <v-col cols="6" class="d-flex">
        <v-img src="/static/ax.png"  height="100%" max-width="100%" />
      </v-col>
      <v-col cols="6">


    <v-tabs
        v-model="tab"
        bg-color="primary"
        align-tabs="center"
    >
      <v-tab value="login"> 登录</v-tab>
      <v-tab value="register">注册</v-tab>
    </v-tabs>

    <v-card-text>
      <v-window v-model="tab">
        <v-window-item value="login">

          <div>
            账号
            <v-text-field

                label="请输入账号"
                :rules="rules"
                hide-details="auto"
            ></v-text-field>
            密码
            <v-text-field
                label="请输入密码"
                :rules="rules"
                hide-details="auto"
            ></v-text-field>

            <v-btn
                :loading="loading"
                class="flex-grow-1"

                height="48"

                variant="tonal"
                @click="load"

            >
              登入
            </v-btn>
          </div>
        </v-window-item>

        <v-window-item value="register">
          <div>
            账号
            <v-text-field
                label="输入账号"
                :rules="rules"
                hide-details="auto"
                class="custom-height custom-input"
            ></v-text-field>
            用户名
            <v-text-field
                label="输入用户名"
                :rules="rules"
                hide-details="auto"
                class="custom-height custom-input"
            ></v-text-field>
            设置密码
            <v-text-field
                label="输入密码"
                :rules="rules"
                hide-details="auto"
                class="custom-height custom-input"
            ></v-text-field>
            确认密码
            <v-text-field
                label="确认密码"
                :rules="rules"
                hide-details="auto"
                class="custom-height custom-input"
            ></v-text-field>
            <v-btn
                :loading="loading"
                class="flex-grow-1"
                height="48"
                variant="tonal"
                @click="load"
            >
              注册
            </v-btn>
          </div>
        </v-window-item>

      </v-window>
    </v-card-text>

    </v-col>
    </v-row>
  </v-card>


</template>

<script>
export default {
  data: () => ({
    loading: false,
    rules: [
      // value => !!value || 'Required.',
      // value => (value && value.length >= 3) || 'Min 3 characters',
      null

    ],
    tab: null,
  }),
  methods: {
    load() {
      this.loading = true
      setTimeout(() => (this.loading = false), 3000)
    },
  },
}

</script>
<style>
/* 应用整体样式 */
body {
  background-color: #f5f5f5;
  font-family: 'Arial', sans-serif;
}

/* v-card样式 */
.v-card {
  max-width: 800px; /*长度*/
  margin: 80px auto; /*上方距离*/
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  background-color: white; /*card 背景色*/
}

/* v-tabs样式 */
.v-tabs {
  justify-content: center;
}

/* v-tab样式 */
.v-tab {
  color: white !important; /*导航栏文字颜色*/
  font-weight: bold;
}

/* v-tab-active样式 */
.v-tab--active {
  background-color: #1f1e33 !important; /*这谁？*/
}

/* v-card-text样式 */
.v-card-text {
  padding: 0;
}
.custom-height {
  height: 52px; /* 或者你想要的任何较小的高度 */
}

/* v-window样式 */
.v-window {
  overflow-y: auto;
}

/* v-window-item样式 */
.v-window-item {
  padding: 20px;
}

/* v-text-field样式 */
.v-text-field {
  margin-bottom: 18px;
  background-color: whitesmoke; /*输入框颜色*/
}

.v-text-field__slot label {
  font-size: 14px;
  color: #1f1e33; /*这谁*/
}

.v-text-field__slot input {
  font-size: 16px;
  color: #212121;
}

.center-btn {
  margin-left: auto;
  margin-right: auto;
  display: block;
}

@media (max-width: 600px) {
  .center-btn {
    width: fit-content;
    margin-left: auto;
    margin-right: auto;
  }
}

.v-text-field__slot .v-messages {
  color: red; /*输入有误会提示，但是如果产生提示输入框背景色会溢出*/
}
</style>
