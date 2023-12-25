<template>
  <div id="root">
    <div class="todo-container">
      <div class="back">
        <button class="btn-danger btn edit" @click="edit">退出</button>
      </div>
      <div class="todo-wrap">
        <h3>你好 {{this.$route.query.username}} ,这是你的TodoList清单</h3>
        <MyHeader @addTodo="addTodo" />
        <MyList :todos="todos" />
        <MyFooter
            :todos="todos"
            :checkAllTodo="checkAllTodo"
            :clearAllTodo="clearAllTodo"
        />
      </div>
    </div>
  </div>
</template>

<script>
import MyHeader from "../components/Todo/MyHeader";
import MyFooter from "../components/Todo/MyFooter";
import MyList from "../components/Todo/MyList";

export default {
  name: "App",
  components: {
    MyHeader,
    MyFooter,
    MyList,
  },
  data() {
    return {
      //读取本地存储的JSON字符串
      todos: JSON.parse(localStorage.getItem(this.$route.query.username)) || [],
    };
  },
  methods: {
    //添加待办
    addTodo(todoObj) {
      this.todos.unshift(todoObj);
    },
    //勾选
    checkTodo(id) {
      this.todos.forEach((todo) => {
        if (todo.id === id) {
          todo.done = !todo.done;
        }
      });
    },
    //修改
    updateTodo(id, title) {
      this.todos.forEach((todo) => {
        if (todo.id === id) {
          todo.title = title;
        }
      });
    },
    //删除todo
    deleteTodo(id) {
      this.todos = this.todos.filter((todo) => {
        return todo.id !== id;
      });
    },
    //清除所有todo
    clearAllTodo() {
      this.todos = this.todos.filter((todo) => {
        return !todo.done;
      });
    },
    //全选或全部取消勾选
    checkAllTodo(done) {
      this.todos.forEach((todo) => {
        todo.done = done;
      });
    },
    edit(){
      if(confirm("您确定要退出登录吗？")){
        this.$router.replace("login")
      }
    }
  },
  //监视
  watch: {
    // 这种写法下勾选之后并没有监视到，需要使用深度监视
    // todos(value){
    //   localStorage.setItem("todos",JSON.stringify(value))
    // }
    todos: {
      deep: true,
      handler(value) {
        localStorage.setItem(this.$route.query.username, JSON.stringify(value));
      },
    },
  },
  mounted() {
    this.$bus.$on("checkTodo", this.checkTodo);
    this.$bus.$on("deleteTodo", this.deleteTodo);
    this.$bus.$on("updateTodo", this.updateTodo);
  },
  beforeDestroy() {
    this.$bus.$off("checkTodo");
    this.$bus.$off("deleteTodo");
    this.$bus.$off("updateTodo");
  },
};
</script>

<style>
body {
  background: #fff;
}
.btn {
  display: inline-block;
  padding: 4px 12px;
  margin-bottom: 0;
  font-size: 14px;
  line-height: 20px;
  text-align: center;
  vertical-align: middle;
  cursor: pointer;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2),
  0 1px 2px rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}
.btn-danger {
  color: #fff;
  background-color: #da4f49;
  border: 1px solid #bd362f;
}
.btn-danger:hover {
  color: #fff;
  background-color: #bd362f;
}

.btn:focus {
  outline: none;
}
.todo-container {
  width: 600px;
  margin: 60px auto;
  background-color: skyblue;
  border-radius: 10px;
}
.todo-container .todo-wrap {
  padding:0 10px 10px 10px;

}
h3 {
  text-align: center;
  padding: 0;
  margin-top: 0;
  margin-bottom: 20px;
}
.edit{
  margin-left: 5px;
  margin-top:  10px;
}
</style>
