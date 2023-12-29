<template>
  <div>
    <div class="row">
      <div class="column side">
        <h2>左侧栏</h2>
        <div class="spacer"></div>
        <v-card class="leftcard" hoverable>
          <div class="Catalog1">

          </div>
          <v-col cols="auto">
            <div class="spacer"></div>
            <v-btn icon="mdi-plus"
                   size="small"
                   @click="addNewItem"></v-btn>
          </v-col>

          <v-col cols="auto">
            <div class="spacer"></div>
          </v-col>

        </v-card>

      </div>

      <div class="column middle">

        <v-card
            class="list-card"
            max-width="700"
        >
          <v-text-field
              v-model="newItemText"
              label="输入要创建的待办并按回车创建"
              :rules="rules"
              hide-details="auto"
              class="custom-input"
              @keydown.enter="addNewItemFromInput"

          ></v-text-field>
          <v-divider></v-divider>

          <v-list lines="two"
                  select-strategy="classic"
                  class="list1"
                  density="compact"
          >
            <v-list-subheader
            class="TodoTitle">最近待办</v-list-subheader>

            <v-divider></v-divider>

            <!-- 新增项 -->
            <v-list-item
                v-for="(item, index) in items"
                :key="index"
                value="newItem"
                class="hover"
                @click="onItemOrBtnClick(item)"
            >
              <template v-slot:prepend>

                <v-list-item-action>
                  <v-checkbox-btn
                      @click.stop
                      v-model="item.isChecked">
                  </v-checkbox-btn>
                </v-list-item-action>
<!--      item.isChecked       找到复选框被选中的item并赋值，方便后续指针删除该item-->
              </template>

              <template v-slot:append>
                <v-btn
                    icon="mdi-minus"
                    size="x-small"
                    style="font-size: 15px"
                    position="relative"
                    @click="subSelectedItem">
                </v-btn>
                <v-btn
                    :ref="'btnInfo' + index"
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                    style="font-size: 20px;"
                    @click="onBtnClick(item,$event)"
                ></v-btn>
              </template>

              <v-list-item-title :class="{ 'strike-through': item.isChecked }">
                {{item.title}}
              </v-list-item-title>

              <v-list-item-subtitle :class="{ 'strike-through': item.isChecked }">
                {{item.subtitle}}
              </v-list-item-subtitle>


            </v-list-item>

          </v-list>
        </v-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    items: [],
    newItemText:'',
  }),

  created(){
    this.addNewItem({title:'待办示例',subtitle:'此处显示该待办的备注~'});
    this.addNewItem({title:'尝试创建一个待办',subtitle:'开始你的待办旅途吧！'});
  },

  methods: {

    onItemOrBtnClick(item, event) {
      event.stopPropagation();
      //接受两个参数：item 表示被点击的项（即 v-list-item 中的数据项），event 表示触发点击事件的事件对象。
      //使用 event.stopPropagation() 阻止事件冒泡，确保只有当前的 v-list-item 收到点击事件，而不会传播到其他元素。
      console.log('Item clicked:', item);
      // 处理 item 的点击逻辑，例如更新数据等操作
    },

    onBtnClick(item, event) {
      event.stopPropagation();
      console.log('Button clicked:', item);
      // 处理按钮点击的逻辑
    },
    // 此处的传入的title应该是输入栏的文字
    //然后同时创建的新的待办要加入数据库当中
    addNewItem({ title=null , subtitle = 'None' } = {}) {
      this.items.push({
        title,
        subtitle,
        isChecked: false,
      });
    },

    subSelectedItem(event) {
      event.stopPropagation();
      const selectedItem = this.items.find(item => item.isChecked);
      if (selectedItem) {
        const index = this.items.indexOf(selectedItem);
        this.items.splice(index, 1);
      }
        },

    addNewItemFromInput() {
      if (this.newItemText.trim() !== '') {
        this.addNewItem({ title: this.newItemText });
        this.newItemText = ''; // 清空输入框
      }
    },
    },
}
</script>


<style>
* {
  box-sizing: border-box;
}

body {
  margin: 200px;
}

.column {
  float: left;
  padding: 10px;
}

.column.side {
  text-align: center;
  width: 20%;

}

.column.middle {
  width: 80%;
}

.row:after {
  content: "";
  display: table;
  clear: both;
}

.Catalog1 {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: -7px;
  height: 100%; /* 确保高度充满 */
}

.list1{
  border:1px solid #303030;
}

@media screen and (max-width: 600px) {
  .column.side, .column.middle {
    width: 100%;
  }
}
</style>

<style scoped>

.spacer{
  height:16px;
}

.TodoTitle{
  text-align: center;
  font-size: large;
}
.list-card {
  margin-left: 50px;
}

.hover:hover {
  background-color: rgba(255, 250, 205, 1);
  cursor: pointer;
}
.strike-through {
  text-decoration: line-through;
}
</style>
