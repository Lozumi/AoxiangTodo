<template>

  <v-card>
    <v-row no-gutters>
      <v-col cols="6" class="d-flex">
        <v-img src="/static/ckw.jpg"  height="100%" max-width="100%" />
      </v-col>
      <v-col cols="6">


        <v-tabs
            v-model="tab"
            color="primary"
            align-tabs="end"
        >
          <v-tab value="login"> 登录</v-tab>
          <v-tab value="register">注册</v-tab>
        </v-tabs>

        <v-card-text>
          <v-window v-model="tab">
<!--            <登录窗口>-->
            <v-window-item value="login">

              <div>

                <div class="text-h6 mb-1">
                  LOGIN
                </div>
                <v-text-field
                    label="请输入账号"
                    :rules="usernameRules"
                    hide-details="auto"
                    v-model="username"
                ></v-text-field>

                <v-text-field
                    label="请输入密码"
                    :rules="passwordRules"
                    hide-details="auto"
                    v-model="password"
                ></v-text-field>
                <v-card v-if="loginResult" class="mt-5">
                  <v-card-title class="border-bottom mb-4">
                    <p class="text-h5">登录返回包：</p>
                  </v-card-title>
                  <v-card-text>
                    <pre>{{ loginResult }}</pre>
                  </v-card-text>
                </v-card>

                <v-alert v-if="loginError" type="error" class="mt-5">
                  <h2 class="text-h3">登录错误:</h2>
                  <pre>{{ loginError }}</pre>
                </v-alert>
                <v-btn
                    :loading="loading"
                    class="mb-5"
                    height="48"
                    variant="tonal"
                    align-end
                    @click="login"
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
                  :rules="usernameRules"
                  hide-details="auto"
                  v-model="registerUsername"
              ></v-text-field>

              <v-text-field
                  label="设置用户名"
                  :rules="usernameRules"
                  hide-details="auto"
                  v-model="registerDisplayName"
              ></v-text-field>

              <v-text-field
                  label="设置密码"
                  :rules="passwordRules"
                  type = "password"
                  hide-details="auto"
                  v-model="registerPassword"
              ></v-text-field>

              <v-text-field
                  label="确认密码"
                  :rules="confirmPasswordRules"
                  type = "password"
                  hide-details="auto"
                  v-model="confirmRegisterPassword"
              ></v-text-field>
              <v-card v-if="registerResult" class="mt-5">
                <v-card-title class="border-bottom mb-4">
                  <p class="text-h5">注册返回包：</p>
                </v-card-title>
                <v-card-text>
                  <pre>{{ registerResult }}</pre>
                </v-card-text>
              </v-card>

              <v-alert v-if="registerError" type="error" class="mt-5">
                <h2 class="text-h3">注册错误:</h2>
                <pre>{{ registerError }}</pre>
              </v-alert>
              <v-btn
                  :loading="loading"
                  class="mb-5"
                  height="48"
                  variant="tonal"
                  align-end
                  @click="register"
              >
                注册
              </v-btn>

            </v-window-item>

          </v-window>
        </v-card-text>

      </v-col>
    </v-row>
  </v-card>
<!--  <v-card v-if="result" class="mt-5">-->
<!--    &lt;!&ndash; ... &ndash;&gt;-->
<!--  </v-card>-->
  <v-progress-linear v-if="pending" class="mt-5"></v-progress-linear>
</template>



<script>

const loginSend = async () => {
  await refresh();
};

// const { data, pending, error, refresh } = useFetch(apiUrl, {
//   method: 'POST',
//   headers: {
//     'Content-Type': 'application/json',
//   },
//   body: JSON.stringify({
//     content: Content,
//     requestType: requestType,
//   }),
// });
import { required } from "vuelidate/lib/validators";
export default {
  data: () => ({
    users: JSON.parse(localStorage.getItem("users")) || [],
    loading: false,
    tab: 'login', // 添加这行代码以初始化 tab 属性
    username: "",
    password: "",
    registerUsername: "",
    registerDisplayName: "",
    registerPassword: "",
    confirmRegisterPassword: "",
    loginResult: null,
    loginError: null,
    registerResult: null,
    registerError: null,
    usernameRules: [required],
    passwordRules: [required],
    confirmPasswordRules: [
      required,
      (value) => value === this.registerPassword || "密码不一致",
    ],
    rules: [
      // 根据需要定义表单验证规则
      // value => !!value || 'Required.',
      // value => (value && value.length >= 3) || 'Min 3 characters',
      // null
    ],
  }),
  computed: {
    $v() {
      return useVuelidate(this, {
        username: this.usernameRules,
        password: this.passwordRules,
        registerUsername: this.usernameRules,
        registerDisplayName: this.usernameRules,
        registerPassword: this.passwordRules,
        confirmRegisterPassword: this.confirmPasswordRules,
      });
    },
  },
  methods: {

    load() {
      this.loading = true;
      setTimeout(() => (this.loading = false), 3000);
    },

    async login() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        try {
          this.loading = true;
          const response = await this.$fetch('/api/login', {
            method: 'POST',
            body: JSON.stringify({
              username: this.username,
              password: this.password,
            }),
            headers: {
              'Content-Type': 'application/json',
            },
          });
          this.loginResult = JSON.stringify(response.data, null, 2);
          // 清除错误信息
          this.loginError = null;
        }catch (error) {
          this.loginError = JSON.stringify(error.response.data, null, 2);
          alert("账号或密码输入错误");
          this.password = "";
        } finally {
          this.loading = false;
        }
      }
    },

    async register() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        try {
          this.loading = true;
          const response = await this.$fetch('/api/register', {
            method: 'POST',
            body: JSON.stringify({
              username: this.registerUsername,
              displayName: this.registerDisplayName,
              password: this.registerPassword,
            }),
            headers: {
              'Content-Type': 'application/json',
            },
          });
          this.registerResult = JSON.stringify(response.data, null, 2);
          // 清除错误信息
          this.registerError = null;
        }catch (error) {
          if (error.response.status === 409) {
            alert("该用户名已存在，请选择其他用户名");
          } else if (error.response.status === 400) {
            alert("两次输入的密码不一致，请重新输入");
          } else {
            alert("请填写完整且正确的注册信息");
          }
        } finally {
          this.loading = false;
        }
      } else {
        for (const field in this.$v.$invalid) {
          if (this.$v[field].$dirty) {
            console.log(`${field} 输入有误，请检查`);
            alert(`${field} 输入有误，请检查`);
            break;
          }
        }
      }
    },

    toRegister() {
      this.$router.push("register");
    },
  },
  }
// const result = ref(null);
//
// onMounted(() => {
//   refresh();
// });
// watch(data, (newData) => {
//   // 将 JSON 字符串解析为对象
//   result.value = JSON.parse(newData);
// });

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
  background-color: white; /*输入框颜色*/
}

.v-input--is-danger {
  margin-bottom: inherit !important;
}

/* 确保输入框之间的固定间距 */
.v-text-field:not(:last-child) {
  margin-bottom: 18px; /* 或者您想要的固定间距值 */
}

/* 可选：如果需要调整错误提示文本的样式 */
.v-messages__message {
  font-size: 14px;
  margin-bottom: 8px; /* 添加这行代码以避免输入框背景色溢出 */
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