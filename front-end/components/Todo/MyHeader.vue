
<template>
  <div >
    <v-text-field
        class="create-task"
        placeholder="请输入要创建的任务名称，按回车键确认"
        :rules="rules"
        hide-details="auto"
        v-model="title"
        @keyup.enter="add"
    ></v-text-field>
<!--     第7行 rules是什么？-->
<!--    v-model="title" 实现了双向绑定，将输入框中的值与 data 中的 title 属性进行关联。-->
<!--    @keyup.enter="add" 表示当用户在输入框中按下回车键时，触发 add 方法。-->

  </div>

</template>

<script>
import { nanoid } from "nanoid";
export default {
  name: "MyHeader",
  data() {
    return {
      title: "",
    };
  },
  methods: {
    add() {
      if (this.title.trim() === "") {
        alert("待办事项不能为空，请输入要创建的待办事项！");
        this.title=''
      } else {
        //包装为todo对象
        const todoObj = {
          id: nanoid(),
          title: this.title,
          done: false,
          isEdit:false
        };
        this.$emit('addTodo', todoObj);
        //清空输入框中的值
        this.title = "";
      }
    },
  },
};
</script>
<!--name: "MyHeader" 给组件取了一个名字叫 "MyHeader"。-->
<!--data 函数返回一个包含 title 属性的对象，用于存储输入框中的值。-->
<!--methods 对象包含一个 add 方法，用于添加待办事项。-->
<!--在 add 方法中，首先检查输入框中的值是否为空，如果为空则弹出提示框，否则创建一个包含待办事项信息的对象 todoObj。-->
<!--使用 this.$emit('addTodo', todoObj) 发送自定义事件 'addTodo'，并传递 todoObj 对象给父组件。-->
<!--最后清空输入框中的值。-->

<style scoped>

.create-task{
  margin-left: 50px;
  margin-top: 3px;
  width:700px;
  max-height: 50px;
  background-color:#ffffff;
  padding: 4px 7px;
}

.create-task:focus{
  outline: none;
  border-color: rgba(82, 168, 236, 0.8);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
  0 0 8px rgba(82, 168, 236, 0.6);
}

</style>
<script setup lang="ts">
</script>
