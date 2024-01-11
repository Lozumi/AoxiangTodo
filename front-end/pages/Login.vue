<template>
  <v-card>
    <v-row no-gutters>
      <v-col cols="4" class="d-flex">
        <v-img src="/static/yezi.jpeg" height="100%" max-width="100%"/>
      </v-col>
      <v-col cols="8">
        <v-tabs
            v-model="state.tab"
            color="primary"
            align-tabs="end"
        >
          <v-tab value="login">登录</v-tab>
          <v-tab value="register">注册</v-tab>
        </v-tabs>

        <v-card-text>
          <v-window v-model="state.tab">

            <!--            <登录窗口>-->
            <v-window-item value="login">
              <div>

                <div class="text-h6 mb-1">
                  LOGIN
                </div>
                <v-text-field
                    label="请输入账号"
                    hide-details="auto"
                    v-model="state.loginData.account"
                ></v-text-field>

                <v-text-field
                    label="请输入密码"
                    type="password"
                    hide-details="auto"
                    v-model="state.loginData.password"
                ></v-text-field>

<!--                此处为登录返回包的界面-->
                <v-card v-if="state.loginResult" class="mt-5">
                  <v-card-title class="border-bottom mb-4">
                    <p class="text-h5"></p>
                  </v-card-title>
                  <v-card-text>
                    <pre>{{ JSON.parse(state.loginResult).message }}</pre>
                  </v-card-text>
                </v-card>
                <v-alert
                    v-if="state.loginError"
                    type="warning"
                    class="mt-5"
                    text="登录错误，请检查账号密码是否输入正确">
                </v-alert>
                <v-btn
                    :loading="state.loading"
                    class="mb-5"
                    height="48"
                    variant="tonal"
                    align-end
                    @click="handleLogin"
                >
                  登入
                </v-btn>

              </div>
            </v-window-item>

            <v-window-item value="register">
              <div class="text-h6 mb-1">
                REGISTER
              </div>

              <v-text-field
                  label="请设置账号"
                  hide-details="auto"
                  v-model="state.registerData.account"
              ></v-text-field>

              <v-text-field
                  label="设置用户名"
                  hide-details="auto"
                  v-model="state.registerData.userName"
              ></v-text-field>

              <v-text-field
                  label="设置密码"
                  type="password"
                  hide-details="auto"
                  v-model="state.registerData.password"
              ></v-text-field>

              <v-text-field
                  label="确认密码"
                  :rules="confirmPasswordRules"
                  type="password"
                  hide-details="auto"
                  v-model="state.registerData.confirmPassword"
              ></v-text-field>
              <v-alert
                  v-if="state.registerData.password !== '' && state.registerData.confirmPassword !== '' && state.registerData.password !== state.registerData.confirmPassword"
                  type="error"
                  class="mt-5"
                  text="确认密码错误,请检查两次输入的密码是否一致">
              </v-alert>

              <!--              !isRegisterFormValid ||-->
              <v-btn
                  :disabled="state.registerData.password !== state.registerData.confirmPassword"
                  :loading="state.loading"
                  class="mb-5"
                  height="48"
                  variant="tonal"
                  align-end
                  @click="handleRegister"
              >
                注册
              </v-btn>

<!--              此处为注册返回包的界面-->
              <v-card v-if="state.registerResult" class="mt-5">
                <v-card-title class="border-bottom mb-4">
                  <p class="text-h5"></p>
                </v-card-title>
                <v-card-text>
                  <pre>{{ JSON.parse(state.registerResult).message}}</pre>
                </v-card-text>
              </v-card>
              <v-alert
                  v-if="state.registerError"
                  type="error"
                  class="mt-5">
                <h2 class="text-h3">注册错误:</h2>
                <pre>{{ state.registerError }}</pre>
              </v-alert>

            </v-window-item>
          </v-window>
        </v-card-text>
      </v-col>
    </v-row>
  </v-card>
  <v-progress-linear
      v-if="state.isLoginPending || state.isRegisterPending"
      class="mt-5">
  </v-progress-linear>

</template>
<!---->
<!---->
<!---->
<script setup>

import AccountRequest, {baseUrl} from "../composables/AccountRequest.ts"

import {useRouter} from "vue-router";

import accountRequest from "../composables/AccountRequest.ts";

// const loginSend = async () => {
//   await refresh();
// };

const confirmPasswordRules = {
  sameAs: (value) => value === state.registerData.password,
};

const router = useRouter()

const resp = ref()

const state = reactive({
      loading: false,
      tab: 'login',
      loginData: {
        account: '',
        password: '',
      },
      registerData: {
        account: '',
        userName: '',
        password: '',
        confirmPassword: '',
      },
      loginResult: null,
      isLoginPending: null,
      loginError: null,

      isRegisterPending: null,
      registerResult: null,
      registerError: null,
    }
);


//**********************验证函数***************************
// const base = 'http://10.60.50.102:20220/'
// const apiUrlLogin = 'UserLogin';
//
// const apiUrlRegister = 'UserRegister';
//
// const loginResult = ref(null);
//
// const registerResult = ref(null);


async function handleLogin() {

  state.loginError = null;

  const {
    pending: isLoginPending,
    error: loginError,
    refresh: refreshLogin,
    data: loginResponse
  } = AccountRequest.useLogin(state.loginData.account, state.loginData.password)

  if (state.loginData.account && state.loginData.password) {

    await refreshLogin();

    if (!state.isLoginPending && state.loginError === null) {

      // 登录，并结构出data，pending，error三种状态进行赋值
      await refreshLogin()

      // 更新登录结果到组件内部状态
      state.loginResult = loginResponse
      state.loginError = loginError
      state.isLoginPending = isLoginPending
      console.log("error:" +state.loginError)

      // 解析json字符串
      resp.value = parseJson(state.loginResult)
      // todo(message） resp.message

      // 通过字段中的status进行判断
      if (resp.value.status !== "Failure") {
        navigateTo("/")
      }
    }
  }
}

async function handleRegister() {

  state.registerError = null;

  const {
    pending: isRegisterPending,
    error: registerError,
    refresh: refreshRegister,
    data: registerResponse
  } = accountRequest.useRegister(state.registerData.userName,
      state.registerData.account,
      state.registerData.password,)

// AccountRequest.useRegister(state.registerData.userName, state.registerData.account, state.registerData.password)

  if (
      state.registerData.account &&
      state.registerData.userName &&
      state.registerData.password &&
      state.registerData.password === state.registerData.confirmPassword
  ) {

    if (!state.isRegisterPending && state.registerError === null) {

      await refreshRegister()
      console.log(registerResponse);

      // 更新注册结果到组件内部状态
      state.registerResult = registerResponse
      state.registerError = registerError
      state.isRegisterPending = isRegisterPending

      // asdvy!12511
      console.log(registerError)
      setTimeout(() => {
        if (!(registerError.value)) {
          // 跳转至登录页面或其他逻辑
          state.tab = "login"
        }
      }, 500)
    }
  }
}


function parseJson(response) {
  return JSON.parse(response)
}
// 可以删除
// onMounted(async () => {
//   state.loginData.account = "123"
//   state.loginData.password = "123"
//   const res = await AccountRequest.login(state.loginData)
//   console.log(res)
// })

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
  margin: 40px auto; /*上方距离*/
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
  color: palevioletred !important; /*导航栏文字颜色*/
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
  height: 52px; /* 高度 */
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
  background-color: white; /*输入框颜色*/
}

.v-input--is-danger {
  margin-bottom: inherit !important;
}

/* 确保输入框之间的固定间距 */
.v-text-field:not(:last-child) {
  margin-bottom: 18px; /* 固定间距值 */
}

/* 可选：如果需要调整错误提示文本的样式 */
.v-messages__message {
  font-size: 14px;
  margin-bottom: 8px; /* 避免输入框背景色溢出 */
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

pre {
  white-space: pre-wrap; /* 允许换行 */
  word-break: break-all; /* 长单词换行 */
  overflow-wrap: break-word; /* 强制在需要的地方断行 */
  max-width: 100%; /* 按照容器宽度显示，避免过长导致的横向溢出 */
  box-sizing: border-box;
}

.v-text-field__slot .v-messages {
  color: red;
}
</style>
