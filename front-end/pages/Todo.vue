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
        <div>
          <v-text-field
              class="create-task"
              label="Enter the task you want to do."
              :rules="rules"
              hide-details="auto"
          ></v-text-field>
        </div>
        <v-card
            class="list-card"
            max-width="700"
        >

          <v-list lines="two">
            <v-list-subheader>
              Things To Do
            </v-list-subheader>

          </v-list>

          <v-divider></v-divider>

          <v-list lines="two" select-strategy="classic" class="list1">
            <v-list-subheader>General</v-list-subheader>

            <v-list-item value="notifications">
              <template v-slot:prepend="{ isActive }">

                <v-list-item-action start>
                  <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <v-list-item-title>Notifications</v-list-item-title>

              <v-list-item-subtitle>
                Notify me about updates to apps or games that I downloaded
              </v-list-item-subtitle>
            </v-list-item>

            <v-list-item value="sound" class="custom-list1">
              <template v-slot:prepend="{ isActive }">
                <v-list-item-action start>
                  <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <v-list-item-title>
                Sound
              </v-list-item-title>

              <v-list-item-subtitle>
                Auto-update apps at any time. Data charges may apply
              </v-list-item-subtitle>
            </v-list-item>

            <v-list-item value="widgets">
              <template v-slot:prepend="{ isActive }">
                <v-list-item-action start>
                  <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
                </v-list-item-action>
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
    rules: [
      // value => !!value || '',
      // value => (value && value.length >= 3) || 'Min 3 characters',
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

.create-task{
  margin-left: 50px;
  margin-top: 3px;
  width:700px;
  max-height: 50px;
  background-color:#ffffff;

}

.list-card {
  margin-left: 50px;
}

</style>


