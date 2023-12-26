<template>

  <v-card>
    <v-row no-gutters>
      <v-col cols="6" class="d-flex">
        <v-img src="/static/ax.png"  height="100%" max-width="100%" />
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

                <v-btn
                    :loading="loading"
                    class="flex-grow-1"
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
                <v-btn
                    :loading="loading"
                    class="flex-grow-1"
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

</template>



<script>

import { required } from "vuelidate/lib/validators";

export default {
  data: () => ({
    users: JSON.parse(localStorage.getItem("users")) || [],
    loading: false,
    tab: null, // 添加这行代码以初始化 tab 属性
    username: "",
    password: "",
    registerUsername: "",
    registerDisplayName: "",
    registerPassword: "",
    confirmRegisterPassword: "",
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
      null
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
    login() {
      let foundUser = this.users.find(
          (user) => user.username === this.username && user.password === this.password
      );

      if (foundUser) {
        this.$router.push({
          name: "todo",
          query: { username: this.username },
        });
      } else {
        alert("账号或密码输入错误");
        this.password = "";
      }
    },
    register() {
      this.$v.$touch(); // 添加这行代码以触发表单验证

      if (!$v.$invalid) {
        // 检查注册信息是否完整和正确
        if (
            !$v.registerUsername.$invalid &&
            !$v.registerDisplayName.$invalid &&
            !$v.registerPassword.$invalid &&
            !$v.confirmRegisterPassword.$invalid &&
            this.registerPassword === this.confirmRegisterPassword
        ) {
          this.tab = "login";
        } else {
          alert("请填写完整且正确的注册信息");
        }
      } else {
        alert("请填写完整且正确的注册信息");
      }
    },
    toRegister() {
      this.$router.push("register");
    },
  },
};
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