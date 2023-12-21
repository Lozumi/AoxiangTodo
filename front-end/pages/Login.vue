<template>
  <v-card>
    <v-tabs
        v-model="tab"
        bg-color="primary"
    >
      <v-tab value="login">登录</v-tab>
      <v-tab value="register">注册</v-tab>
    </v-tabs>

    <v-card-text>
      <v-window v-model="tab">
        <v-window-item value="login">

          <div>
            用户名
            <v-text-field
                label="请输入用户名"
                :rules="rules"
                hide-details="auto"
            ></v-text-field>
            密码
            <v-text-field label="请输入密码"></v-text-field>
          </div>
        </v-window-item>

        <v-window-item value="register">
         <div>
           用户名
           <v-text-field
             label="输入用户名"
             :rules="rules"
             hide-details="auto"
             ></v-text-field>
           设置密码
           <v-text-field
             label="输入密码"
             :rules="rules"
             hide-details="auto"
             ></v-text-field>
           确认密码
           <v-text-field
             label="确认密码"
             :rules="rules"
             hide-details="auto"
             ></v-text-field>
         </div>
        </v-window-item>

      </v-window>
    </v-card-text>
  </v-card>


</template>

<script>
export default {
  data: () => ({
    rules: [
      value => !!value || 'Required.',
      value => (value && value.length >= 3) || 'Min 3 characters',
    ],
    tab: null,
  }),
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
  max-width: 400px;/*长度*/
  margin: 50px auto;/*上方距离*/
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  background-color: white;/*card 背景色*/
}

/* v-tabs样式 */
.v-tabs {
  justify-content: center;
}

/* v-tab样式 */
.v-tab {
  color: white !important;
  font-weight: bold;
}

/* v-tab-active样式 */
.v-tab--active {
  background-color: #009688 !important;
}

/* v-card-text样式 */
.v-card-text {
  padding: 0;
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
  margin-bottom: 15px;
  background-color :wheat;
}

.v-text-field__slot label {
  font-size: 14px;
  color: #616161;
}

.v-text-field__slot input {
  font-size: 16px;
  color: #212121;
}

.v-text-field__slot .v-messages {
  color: red;
}
</style>