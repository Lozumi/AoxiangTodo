
<script setup>
import { useCreateTodoWork } from '~/composables/CreateTodoWork';

const { createTodoWork } = useCreateTodoWork();

const handleCreateTodoWork = async () => {
  let layer = 1;
  let innerId = 0;
  let importancePriority = 1;
  let emergencyPriority = 1;
  let title = '测试事项';
  let subtitle = '测试子事项';
  let description = '测试描述';
  let createTime = '2023-12-29T20:20:20';
  let startTime = '2023-12-29T21:20:20';
  let deadLine = '2023-12-29T22:20:20';
  let status = 'Activated';
  let subToDoWorkItemInnerIdList = [];
  let pomodoroRecordInnerIdList = [];

  const { data, pending, error, refresh } = createTodoWork(
      layer,
      innerId,
      importancePriority,
      emergencyPriority,
      title,
      subtitle,
      description,
      createTime,
      startTime,
      deadLine,
      status,
      subToDoWorkItemInnerIdList,
      pomodoroRecordInnerIdList
  );

  // 处理返回的数据或错误
  if (data) {
    console.log('TodoWork data:', data);
    // 将 data 数据保存到组件的 data 变量中
    this.data = data;
  }
  if (pending) {
    console.log('Creating TodoWork...');
  }
  if (error) {
    console.error('Error creating TodoWork:', error);
  }
};
</script>

<template>
  <div>
    <button @click="handleCreateTodoWork">创建待办事项</button>

    <!-- 直接呈现 data 内容 -->
    <div v-if="data">
      <h2>TodoWork Data:</h2>
      <pre>{{ data }}</pre>
    </div>

    <!-- 显示数据或错误信息等 -->
    <div v-if="pending">Creating TodoWork...</div>
    <div v-if="error">Error creating TodoWork: {{ error }}</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      data: null,
    };
  },
};
</script>
