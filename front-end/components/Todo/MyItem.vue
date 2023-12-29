<template>
  <transition name="todo" appear>
    <li>
      <label>
        <input
            type="checkbox"
            :checked="todo.done"
            @change="handleCheck(todo.id)"
        />
        <!-- 如下代码也能实现功能，但是不太推荐，因为有点违反原则，因为修改了props -->
        <!-- <input type="checkbox" v-model="todo.done"/> -->
        <span
            id="title"
            v-show="!todo.isEdit"
            :class="todo.done? 'line-through' :''"
        >{{ todo.title }}</span>
        <input
            id="tiTle"
            v-show="todo.isEdit"
            type="text"
            :value="todo.title"
            @blur="handleBlur(todo, $event)"
            ref="inputTitle"
        />
      </label>
      <button class="btn btn-danger" @click="handleDelete(todo.id)">
        <i class="el-icon-delete"></i>
      </button>
      <button
          class="btn btn-edit"
          @click="handleEdit(todo)"
          v-show="!todo.isEdit&&!todo.done"
      >
        <i class="el-icon-edit"></i>
      </button>
    </li>
  </transition>
</template>

<script>
export default {
  name: "MyItem",
  //声明接收todo对象
  props: ["todo"],
  methods: {
    //勾选或取消勾选
    handleCheck(id) {
      // this.checkTodo(id)
      this.$bus.$emit("checkTodo", id);
    },
    //删除
    handleDelete(id) {
      if (confirm("您确定要删除该项待办吗？")) {
        // this.deleteTodo(id)
        this.$bus.$emit("deleteTodo", id);
      }
    },
    //编辑
    handleEdit(todo) {
      // this.$set(todo,'isEdit',true)
      todo.isEdit = true;
      //由于Vue先解析代码还没有将输入框渲染上页面，如果不设置延迟将会找不到该输入框，从而没有focus效果
      // setTimeout(()=>{
      //   this.$refs.inputTitle.focus()
      // },100)
      this.$nextTick(function () {
        this.$refs.inputTitle.focus();
      });
    }, //失去焦点修改todo
    handleBlur(todo, e) {
      todo.isEdit = false;
      if (!e.target.value.trim()) {
        return alert("输入不能为空！");
      }
      this.$bus.$emit("updateTodo", todo.id, e.target.value);
    },
  },
};
</script>

<style lang="less" scoped>
#title,#tiTle{
  margin: 0 10px;
}
li {
  list-style: none;
  height: 36px;
  line-height: 36px;
  padding: 0 5px;
  border: 1px solid #ddd;
  border-radius: 10px;
  margin-top: 5px;
  background-color: rgb(255, 255, 255);
}

li label {
  float: left;
  cursor: pointer;
}

li label li input {
  vertical-align: middle;
  margin-right: 6px;
  position: relative;
  top: -1px;
}

li button {
  float: right;
  display: none;
  margin-top: 3px;
}

li:before {
  content: initial;
}
li:hover {
  background-color: #ddd;
}

li:hover button {
  display: block;
}
.todo-enter-active{
  animation: move 0.3s linear;
}
.todo-leave-active{
  animation: move 0.3s linear reverse;
}
@keyframes move {
  from{
    transform: translateX(100%);
  }to{
     transform: translateX(0px);
   }
}
.line-through{
  color: rgba(77, 72, 72, 0.5);
  text-decoration: line-through;
}
</style>
