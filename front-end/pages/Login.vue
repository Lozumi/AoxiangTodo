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