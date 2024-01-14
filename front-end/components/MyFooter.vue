<template>
  <div class="todo-footer" v-show="total">
    <label>

      <input type="checkbox" v-model="isAll" />
    </label>
    <span>
            <span>已完成{{ doneNum }}</span>/ 全部{{ total }}
        </span>
    <button @click="changeisshowitem">{{ isshowitem ? "显示所有" : "隐藏已完成" }}</button>
    <button class="btn btn-danger" @click="clearHaveDone">清除已完成任务</button>

  </div>
</template>
<script>


export default {
  name: 'MyFooter',
  // props: ['todos', 'chooseAll', 'deleteHaveDone'],
  props: ['todos', 'isshowitem'],
  data() {
    return {

    }
  },
  computed: {
    total() {
      return this.todos.length;
    },
    doneNum() {
      // const x = this.todos.reduce((num, todo) => {
      //     return num + (todo.done ? 1 : 0)
      // }, 0)
      // return x;
      return this.todos.reduce((num, todo) => num + (todo.done ? 1 : 0), 0)
    },
    isAll: {
      get() {
        if (this.todos.length === 0) {
          return false;
        }
        return this.todos.length === this.doneNum
      },
      set(value) {
        this.$bus.$emit('chooseAll', value);
      }
    },

  },
  methods: {
    //     checkAll(e) {
    //         // console.log(e.target.checked);
    //         this.chooseAll(e.target.checked);
    //     }
    clearHaveDone() {

      this.$bus.$emit('deleteHaveDone');
    },
    changeisshowitem() {
      this.$bus.$emit('changeisshowitem');
    }
  },

}
</script>
<style scoped>
.todo-footer {
  height: 50px;
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
