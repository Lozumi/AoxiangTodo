<template>
  <v-card>
    <v-row no-gutters>
      <v-col cols="6" class="d-flex">
        <v-img src="/static/ckw.jpg" height="100%" max-width="100%"/>
      </v-col>
      <v-col cols="6">

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
                    hide-details="auto"
                    v-model="state.loginData.password"
                ></v-text-field>
                <v-card v-if="loginResult" class="mt-5">
                  <v-card-title class="border-bottom mb-4">
                    <p class="text-h5">登录返回包：</p>
                  </v-card-title>
                  <v-card-text>
                    <pre>{{ loginResult }}</pre>
                  </v-card-text>
                </v-card>

                <v-alert
                    v-if="loginError"
                    type="warning"
                    class="mt-5"
                    text="登录错误，请检查账号密码是否输入正确">

                </v-alert>

                <!--                -->
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
              <v-card v-if="registerResult" class="mt-5">
                <v-card-title class="border-bottom mb-4">
                  <p class="text-h5">注册返回包：</p>
                </v-card-title>
                <v-card-text>
                  <pre>{{ registerResult }}</pre>
                </v-card-text>
              </v-card>
              <v-alert
                  v-if="registerError"
                  type="error"
                  class="mt-5">
                <h2 class="text-h3">注册错误:</h2>
                <pre>{{ registerError }}</pre>
              </v-alert>

            </v-window-item>
          </v-window>
        </v-card-text>
      </v-col>
    </v-row>
  </v-card>
  <v-progress-linear
      v-if="isLoginPending || isRegisterPending"
      class="mt-5">
  </v-progress-linear>

</template>
<!---->
<!---->
<!---->
<!---->
<!---->
<script setup>
const loginSend = async () => {
  await refresh();
};

const confirmPasswordRules = {
  sameAs: (value) => value === state.registerData.password,
};

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
        // confirmPassword: '',
      },
      loginResult: null,
      loginError: null,
      registerResult: null,
      registerError: null,
    }
);
//**********************验证函数***************************
const base = 'http://10.60.50.102:20220/'
const apiUrlLogin = 'UserLogin';

const apiUrlRegister = 'UserRegister';

const loginResult = ref(null);

const registerResult = ref(null);

const {
  pending: isLoginPending,
  error: loginError,
  refresh: refreshLogin,
  data: loginResponse
} = useFetch(apiUrlLogin, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: state.loginData,
      baseURL: base,
      immediate: false,
      watch: false,
    }
);

const {
  pending: isRegisterPending,
  error: registerError,
  refresh: refreshRegister,
  data: registerResponse
} = useFetch(apiUrlRegister, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: state.registerData,
      baseURL: base,
      immediate: false,
      watch: false,
    }
);

async function handleLogin() {
  if (state.loginData.account && state.loginData.password) {
    await refreshLogin();
    if (isLoginPending.value === false && loginError.value === null) {
      // 更新登录结果到组件内部状态
      state.loginResult = loginResponse.value;
      loginError.value = null;

    }
  }
}

async function handleRegister() {
  if (
      state.registerData.account &&
      state.registerData.userName &&
      state.registerData.password &&
      state.registerData.password === state.registerData.confirmPassword
  ) {
    await refreshRegister();
    if (isRegisterPending.value === false && registerError.value === null) {
      // 更新注册结果到组件内部状态
      state.registerResult = registerResponse.value;
      registerError.value = null;

      // 跳转至登录页面或其他逻辑
    }
  }
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

.v-text-field__slot .v-messages {
  color: red; /*输入有误会提示，但是如果产生提示输入框背景色会溢出*/
}
</style>