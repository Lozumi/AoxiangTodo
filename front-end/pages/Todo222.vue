<template>
  <div id="app">
    <div>
      <MyHeader></MyHeader>
      <MyList :todos="filterTodos" :isshowitem="isshowitem"></MyList>
      <MyFooter :todos="todos" :isshowitem="isshowitem"></MyFooter>
    </div>
  </div>
</template>


<script>
import { defineComponent, ref, provide,  watchEffect } from 'vue';

import MyList from "../components/MyList.vue";
import MyFooter from "../components/MyFooter.vue";
import MyHeader from "../components/MyHeader.vue";

import { onMounted, inject } from 'vue'

const eventBus = inject('eventBus')

const todoId = ref('Todo222')

onMounted(() => {
  if (eventBus) {
    eventBus.emit('todo-mounted', todoId.value)
  } else {
    console.error('Event bus is not injected')
  }
})

export default defineComponent({
  name: "App",
  components: {
    MyHeader,
    MyList,
    MyFooter
  },

  setup() {
    const todos = ref([]);
    const filterTodos = ref([]);
    const isshowitem = ref(false);

    // 获取数据
    useFetch('/api/todos', {
      async handler({ data }) {
        todos.value = data;
        filterTodos.value = isshowitem.value ? data.filter(todo => !todo.done) : data;
      },
      immediate: true
    });

    // 提供全局事件总线
    const eventBus = inject('eventBus');
    provide('$bus', eventBus);

    // 监听数据变化并更新存储
    watchEffect(() => {
      if (process.client) {
        localStorage.setItem('todos', JSON.stringify(todos.value));
      }
    });

    return {
      todos,
      filterTodos,
      isshowitem,

      recieve(x) {
        todos.value.unshift(x);
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      handupData(id, newvalue) {
        todos.value = todos.value.map(todo => {
          if (todo.id === id) todo.todo = newvalue;
          return todo;
        });
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      handleMove(id) {
        todos.value = todos.value.filter(todo => todo.id !== id);
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      checkTodo(id) {
        todos.value = todos.value.map(todo => {
          if (todo.id === id) todo.done = !todo.done;
          return todo;
        });
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      chooseAll(bool) {
        todos.value = todos.value.map(todo => ({ ...todo, done: bool }));
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      deleteHaveDone() {
        todos.value = todos.value.filter(todo => !todo.done);
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      },

      changeisshowitem() {
        isshowitem.value = !isshowitem.value;
        filterTodos.value = isshowitem.value ? todos.value.filter(todo => !todo.done) : todos.value;
      }
    };
  },


  mounted() {
    if (this.$bus) {
      this.$bus.$emit('some-event', data)
    }
    const bus = this.$options.context.$bus;
    bus.$on('recieve', this.recieve);
    bus.$on('checkTodo', this.checkTodo);
    bus.$on('handleMove', this.handleMove);
    bus.$on('chooseAll', this.chooseAll);
    bus.$on('deleteHaveDone', this.deleteHaveDone);
    bus.$on('handupData', this.handupData);
    bus.$on('changeisshowitem', this.changeisshowitem);
  },

  beforeDestroy() {
    const bus = this.$options.context.$bus;
    bus.$off('checkTodo');
    bus.$off('handleMove');
    bus.$off('recieve');
    bus.$off('chooseAll');
    bus.$off('deleteHaveDone');
  },
});
</script>

<style>
#app {
  padding: 10px;
  border: 1px solid gray;
  background-color: rgb(242, 242, 242);
  display: flex;
  align-items: center;
  justify-content: center;
}

button {
  margin: 5px 10px;
}
</style>





