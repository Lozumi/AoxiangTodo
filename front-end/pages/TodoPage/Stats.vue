<template>
  <div class="container">
    <div class="title">待办事项数据</div>
    <v-data-table :headers="tableHeaders" :items="tableData" hide-default-footer>
      <template v-slot:item.completedTodos="{ item }">
        {{ item.completedTodos }}
      </template>
      <template v-slot:item.totalTodos="{ item }">
        {{ item.totalTodos }}
      </template>
      <template v-slot:item.completionRate="{ item }">
        {{ item.completionRate }}%
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableHeaders: [
        {title: 'Total Todos', value: 'totalTodos'},
        {title: 'Completed Todos', value: 'completedTodos'},
        {title: 'Completion Rate', value: 'completionRate'}
      ],
      tableData: []
    };
  },
  mounted() {
    this.fetchToDoData();
  },
  methods: {
    async fetchToDoData() {
      try {
        const {data} = await ToDoWorkRequest.enumerate();
        if (typeof data.value === 'string') {
          const response = JSON.parse(data.value);

          if (typeof response.content === 'string') {
            const jsonItems = JSON.parse(response.content);

            if (Array.isArray(jsonItems)) {
              this.tableData = [
                {
                  totalTodos: jsonItems.length,
                  completedTodos: jsonItems.filter(item => item.isChecked).length,
                  completionRate: Math.round(
                      (jsonItems.filter(item => item.isChecked).length / jsonItems.length) * 100
                  )
                }
              ];
            }
          }
        }
      } catch (error) {
        console.error('Failed to fetch ToDo data:', error);
      }
    }
  }
};
</script>

<style>
.container {
  border: 1px solid #ddd;
  padding: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}

.v-data-table-header th {
  font-weight: bold;
}
</style>