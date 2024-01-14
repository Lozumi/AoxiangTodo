<template>
  <li>
    <label>
      <!-- <input type="checkbox" :checked="item.done" v-model="item.done" /> -->
      <!-- //不建议使用v-model，因为props不能修改 -->
      <input type="checkbox" :checked="item.done" @change="handleCheck(item.id)" />
      <span :class="{ merge: item.done === false, more: item.done === true }" v-show="!item.isEdit">{{
          item.todo
        }}</span>

      <input v-show="item.isEdit" @blur="upData($event, item)" ref="inputTitle" />
    </label>

    <button class="btn btn-danger" @click="handleCheck1(item.id)">删除</button>
    <button @click="handleEidt(item)">编辑</button>
  </li>
</template>
<script>
export default {
  name: 'MyItem',
  props: ['item'],
  methods: {

    handleCheck(id) {
      this.$bus.$emit('checkTodo', id);
      // this.isActive = !this.item.done;
      // console.log(this.isActive)
      console.log(this.$bus)

    },
    handleCheck1(id) {
      this.$bus.$emit('handleMove', id);
    },
    handleEidt(item) {
      if (item.hasOwnProperty('isEdit')) {
        item.isEdit = true;
      }
      else {
        this.$set(item, 'isEdit', true)
      }
      this.$nextTick(function () {
        this.$refs.inputTitle.focus()
      })
    },
    upData(e, item) {
      item.isEdit = false;
      const newvalue = e.target.value;
      this.$bus.$emit('handupData', item.id, newvalue)

    }

  }

}
</script>
<style class="scoped">
.merge {
  color: black;
}

.more {
  text-decoration: line-through;
  color: rgb(80, 80, 80)
}

li {
  list-style: none;
  height: 36px;
  line-height: 36px;
  padding: 0 5px;
  border-bottom: 1px solid #ddd;
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
  margin-top: 9px;
  /* border: none; */
  border-radius: 5px;
  text-align: center;
}

li:before {
  content: initial;
}

li:last-child {
  border-bottom: none;
}

li:hover {
  background-color: rgb(255, 255, 255);
}

li:hover button {
  display: block;
  /* background-color: rgb(245, 115, 115); */
}
</style>



