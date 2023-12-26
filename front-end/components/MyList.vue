<template>
  <ul>
    <MyItem v-for="(item) in todos" :key="item.id" :item="item">
    </MyItem>
  </ul>
</template>
<script>
import MyItem from "./MyItem.vue";
export default {
  name: "MyList",
  components: {
    MyItem
  },
  props: ['todos']
}
</script>
<style>
ul {
  margin-left: 0px;
  border: 1px solid #ddd;
  border-radius: 2px;
  padding: 0px;
  height: 200px;
  overflow-y: auto;
  overflow-x: hidden;

}
</style>
