<template>
  <v-container full-height>
    <v-card class="mt-5 pa-5">
      <h1 class="text-h2 mb-5">网络通信测试</h1>

      <v-btn @click="sendPostRequest" class="mb-5">Send POST Request</v-btn>

      <v-card v-if="result" class="mt-5">
        <v-card-title class="border-bottom mb-4">
          <p class="text-h5">返回包：</p>
        </v-card-title>
        <v-card-text>
          <p class="text-h5 mb-5">原文：{{ result }}</p>

          <!-- 使用 v-data-table 来展示表格 -->
          <v-data-table
              :headers="tableHeaders"
              :items="[result]"
              :items-per-page="5"
              class="elevation-1"
          ></v-data-table>
        </v-card-text>
      </v-card>

      <v-alert v-if="error" type="error" class="mt-5">
        <h2 class="text-h3">Error:</h2>
        <pre>{{ error }}</pre>
      </v-alert>

      <v-progress-linear v-if="pending" class="mt-5"></v-progress-linear>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import ToDoWorkRequest from '~/composables/ToDoWorkRequest';
import { ref, watch } from 'vue';
import AccountRequest from "~/composables/AccountRequest";
const result = ref(null);

const sendPostRequest = async () => {
  await refresh();
};

  // const todoItem = JSON.stringify({
  //   '@class': 'shared.ToDoWorkItem',
  //   layer: 1,
  //   innerId: 0,
  //   importancePriority: 1,
  //   emergencyPriority: 1,
  //   title: '测试事项',
  //   subtitle: '测试子事项',
  //   description: '测试描述',
  //   createTime: '2023-12-29T20:20:20',
  //   startTime: '2023-12-29T21:20:20',
  //   deadLine: '2023-12-29T22:20:20',
  //   status: 'Activated',
  //   subToDoWorkItemInnerIdList: [],
  //   pomodoroRecordInnerIdList: [],
  // });

  const {data, error, pending, refresh} = await AccountRequest.register('testUserName7','testUserAccount13','!Password1223');

  if (error) {
    console.error('Error:', error);
  } else {
    console.log('Data:', data);
  }

// 使用 ref 来保存表格的头部信息
const tableHeaders = ref([
  { title: 'Status', value: 'status' },
  { title: 'Message', value: 'message' },
  { title: 'Content', value: 'content' },
]);

onMounted(() => {
  refresh();
});

watch(data, (newData) => {
  // 将 JSON 字符串解析为对象
  result.value = JSON.parse(newData);
});

</script>
