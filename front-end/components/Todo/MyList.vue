<template>
  <ul class="todo-main">
    <MyItem
        v-for="todoObj in todos"
        :key="todoObj.id"
        :todo="todoObj"
    />
  </ul>
</template>

<script>
import MyItem from "./MyItem.vue";
export default {
  name: "MyList",
  components: {
    MyItem,
  },
  props:['todos']
};
</script>

<style lang="less" scoped>
.todo-main {
  margin-left: 0px;
  // border: 1px solid #ddd;
  border-radius: 2px;
  padding: 0px;
  max-height: 500px;
  overflow-y: scroll;
}

</style>
