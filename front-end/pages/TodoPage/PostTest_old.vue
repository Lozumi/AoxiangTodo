<template>
  <!-- 使用 Vuetify 的容器组件设置全高度 -->
  <v-container full-height>
    <!-- 使用 Vuetify 的卡片组件设置样式和间距 -->
    <v-card class="mt-5 pa-5">
      <!-- 页面标题 -->
      <h1 class="text-h2 mb-5">网络通信测试</h1>

      <!-- 发送 POST 请求的按钮 -->
      <v-btn @click="sendPostRequest" class="mb-5">Send POST Request</v-btn>

      <!-- 显示返回包的卡片 -->
      <v-card v-if="result" class="mt-5">
        <v-card-title class="border-bottom mb-4">
          <p class="text-h5">返回包：</p>
        </v-card-title>
        <v-card-text>
          <!-- 显示原始返回包数据 -->
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

      <!-- 显示错误信息的警告框 -->
      <v-alert v-if="error" type="error" class="mt-5">
        <h2 class="text-h3">Error:</h2>
        <pre>{{ error }}</pre>
      </v-alert>

      <!-- 显示加载状态的进度条 -->
      <v-progress-linear v-if="pending" class="mt-5"></v-progress-linear>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
const sendPostRequest = async () => {
  await refresh();
};

// 使用 ref 创建响应式变量
const apiUrl = 'http://localhost:20220/todoworkitem';
const requestType = 'CreateToDoWork';
const Content = JSON.stringify({
  '@class': 'shared.ToDoWorkItem',
  layer : 1,
  innerId : 0,
  importancePriority : 1,
  emergencyPriority : 1,
  title : '测试事项',
  subtitle : '测试子事项',
  description : '测试描述',
  createTime : '2023-12-29T20:20:20',
  startTime : '2023-12-29T21:20:20',
  deadLine : '2023-12-29T22:20:20',
  status : 'Activated',
  subToDoWorkItemInnerIdList: [],
  pomodoroRecordInnerIdList : []
});

// 使用 useFetch 进行网络请求
const { data, pending, error, refresh } = useFetch(apiUrl, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    content: Content,
    requestType: requestType,
  }),
});

// 使用 ref 来保存表格的头部信息
const tableHeaders = ref([
  { title: 'Status', value: 'status' },
  { title: 'Message', value: 'message' },
  { title: 'Content', value: 'content' },
]);

// 使用 ref 来保存返回数据
const result = ref(null);

// 在组件挂载后执行刷新操作
onMounted(() => {
  refresh();
});

// 使用 watch 监听 data 的变化
watch(data, (newData) => {
  // 将 JSON 字符串解析为对象
  result.value = JSON.parse(newData);
});

</script>
