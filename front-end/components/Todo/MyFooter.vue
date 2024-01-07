<template>
  <div class="todo-footer" v-show="total">
    <label>
      <!-- <input type="checkbox" :checked="isAll" @change="checkAll"/> -->
      <input type="checkbox" v-model="isAll" />
    </label>
    <span>
      <span>已完成{{ doneTotal }}</span> / 全部{{ total }}
    </span>
    <button class="btn btn-danger" @click="clearAll">清除已完成任务</button>
  </div>
</template>

<script>
export default {
  name: "MyFooter",
  props: ["todos", "checkAllTodo", "clearAllTodo"],
  computed: {
    total() {
      return this.todos.length;
    },
    doneTotal() {
      let i = 0;
      this.todos.forEach((todo) => {
        if (todo.done) {
          i++;
        }
      });
      return i;
    },
    isAll: {
      get() {
        return this.doneTotal == this.total && this.total > 0;
      },
      set(value) {
        this.checkAllTodo(value);
      },
    },
  },
  methods: {
    clearAll() {
      if (confirm("您确定要删除所有的已完成待办吗？")) {
        this.clearAllTodo();
      }
    },
  },
};
</script>

<style lang="less" scoped>
.todo-footer {
  height: 40px;
  line-height: 40px;
  padding-left: 6px;
  margin-top: 5px;
}

.todo-footer label {
  display: inline-block;
  margin-right: 20px;
  cursor: pointer;
}

.todo-footer label input {
  position: relative;
  top: -1px;
  vertical-align: middle;
  margin-right: 5px;
}

.todo-footer button {
  float: right;
  margin-top: 5px;
}
</style>
