<template>
  <div>
    <div class="row">
      <div class="column side">
        <h2>左侧栏</h2>

        <n-card title="" hoverable>
          <div class="Catalog1">
            今日 Todo
          </div>
        </n-card>

      </div>

      <div class="column middle">

        <v-card
            class="list-card"
            max-width="700"
        >


          <v-divider></v-divider>

          <v-list lines="two"
                  select-strategy="classic"
                  class="list1"
                  density="compact"
          >
            <v-list-subheader>General</v-list-subheader>

            <v-list-item value="notifications"
                         class="hover">
              <template v-slot:prepend="{ isActive }">
                <v-list-item-action start>
                  <v-checkbox-btn
                      :model-value="isActive"
                  ></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                ></v-btn>
              </template>

              <v-list-item-title>
                Notifications
              </v-list-item-title>
              <v-list-item-subtitle>
                Notify me about updates to apps or games that I downloaded
              </v-list-item-subtitle>

              <v-list v-model:opened="open"
                      density="compact"
                      class="custom-list"
              >
<!--v-list-group 是Vuetify中的一个子组件，用于创建可展开或折叠的列表组。-->
              <v-list-group value="Admin"
                            density="compact"
                            class="custom-list-group"
              >
                <template v-slot:activator="{ props }">
                  <v-list-item
                      v-bind="props"
                      title="Admin"
                  ></v-list-item>
                </template>

                <v-list-item
                    v-for="([title, icon], i) in admins"
                    :key="i"
                    :title="title"
                    :prepend-icon="icon"
                    :value="title"

                ></v-list-item>
              </v-list-group>

              </v-list>
            </v-list-item>

            <v-list-item value="sound"
                         class="hover">
              <template v-slot:prepend="{ isActive }">
                <v-list-item-action start >
                  <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                ></v-btn>
              </template>

              <v-list-item-title>
                Sound
              </v-list-item-title>

              <v-list-item-subtitle>
                Auto-update apps at any time. Data charges may apply
              </v-list-item-subtitle>
            </v-list-item>

            <v-list-item value="widgets"
                         class="hover">
              <template v-slot:prepend="{ isActive }">
                <v-list-item-action start>
                  <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                ></v-btn>
              </template>

              <v-list-item-title>Auto-add widgets</v-list-item-title>

              <v-list-item-subtitle>
                Automatically add home screen widgets when downloads complete
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>
        </v-card>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  data: () => ({
    admins: [
      ['Management', 'mdi-account-multiple-outline'],
      ['Settings', 'mdi-cog-outline'],
    ],
  }),
}
</script>


<script setup>
import { NCard } from 'naive-ui'
</script>


<style>
* {
  box-sizing: border-box;
}

body {
  margin: 200px;
}

.column {
  float: left;
  padding: 10px;
}

.column.side {
  text-align: center;
  width: 20%;

}

.column.middle {
  width: 80%;
}

.row:after {
  content: "";
  display: table;
  clear: both;
}

.Catalog1 {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -7px;
  height: 100%; /* 确保高度充满 */
}

.list1{
  border:1px solid #303030;

}
@media screen and (max-width: 600px) {
  .column.side, .column.middle {
    width: 100%;
  }
}
</style>

<style scoped>
.n-card {
  max-width: 500px;
  max-height: 50px;
  font-size: 8px;
  margin-bottom: 20px;
}



.list-card {
  margin-left: 50px;
}

.custom-list-group {
  /* 自定义样式规则，根据需要调整大小 */
  width: 500px;
}

.custom-list {
  /* 自定义列表项样式规则，根据需要调整大小 */
  width: 500px;
}

.hover:hover {
  background-color: rgba(255, 250, 205, 1);
  cursor: pointer;
}
</style>

<!--              <v-list v-model:opened="open">：-->

<!--                这是一个 Vuetify 的列表组件，通过 v-model 绑定到 open 变量。v-model:opened 用于控制列表项的展开与折叠。-->
<!--                <v-list-group value="Users">：-->

<!--                  v-list-group 是一个组合组件，可以包含多个列表项，并在一个组内进行展开与折叠。value 属性表示该组的唯一标识，这里是 "Users"。-->
<!--                  <template v-slot:activator="{ props }">：-->

<!--                    通过 v-slot:activator 插槽定义组的激活器。在这里，激活器是一个用户列表项，点击该项可以展开或折叠用户组。-->
<!--                    <v-list-item>：-->

<!--                      表示用户列表项。使用了 v-bind="props" 将激活器的属性绑定到列表项，包括点击事件等。-->
<!--                      <v-list-group value="Admin">：-->

<!--                        嵌套在用户组内的管理员组，同样是一个 v-list-group。-->
<!--                        <v-list-item v-for="([title, icon], i) in admins">：-->

<!--                          使用 v-for 遍历 admins 数组，生成管理员列表项。每个管理员列表项包含标题和图标，图标使用了 mdi-account-multiple-outline 和 mdi-cog-outline。-->
<!--                          data 部分：-->

<!--                          数据部分定义了一个 open 数组，用于控制哪个列表项是展开的。初始时，"Users" 列表项是展开的。-->
<!--                          admins 数组包含了管理员的信息，每个管理员信息由标题和图标组成。-->

