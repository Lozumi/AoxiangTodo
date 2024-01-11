<template>
  <input type="text" placeholder="请输入您的任务名称" @keyup.enter="add" />
</template>
<script>

import { nanoid } from 'nanoid'
export default {
  name: 'MyHeader',
  data() {

  },
  methods: {
    add(e) {
      if (e.target.value === '') {
        return
      };
      const anitem = { id: nanoid(), todo: e.target.value, done: false };
      this.$bus.$emit('recieve', anitem);
      console.log(this.$bus.recieve);
      e.target.value = '';

    }
  },
  // props: ['recieve']
}
</script>
<style scoped>
/*header*/
input {
  width: 560px;
  height: 28px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 4px 7px;
}

input:focus {
  outline: none;
  border-color: rgba(82, 168, 236, 0.8);
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(82, 168, 236, 0.6);
}
</style>
