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
            <v-btn  icon="mdi-minus"
                size="small"
                @click="subSelectedItem">
            </v-btn>
          </v-col>

        </v-card>

      </div>

      <div class="column middle">

        <v-card
            class="list-card"
            max-width="700"
        >
          <v-divider></v-divider>

          <v-list lines="two"
                  select-strategy="classic"
                  class="list1"
                  density="compact"
          >
            <v-list-subheader>General</v-list-subheader>

            <v-divider></v-divider>
<!--            先给一个事项的示例-->

            <v-list-item value="notifications"
                         class="hover"
                         @click="onExampleClick">
              <template v-slot:prepend>
                <v-list-item-action >
                  <v-checkbox-btn
                      @click.stop
                      v-model="isExampleChecked"
                  ></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    ref="btnInfo"
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                    @click="onBtnClick"
                ></v-btn>
              </template>

              <v-list-item-title
                  :class="{ 'strike-through': isExampleChecked }">
                Notifications
              </v-list-item-title>
              <v-list-item-subtitle
                  :class="{ 'strike-through': isExampleChecked }">
                Notify me about updates to apps or games that I downloaded
              </v-list-item-subtitle>

              <v-list v-model:opened="open"
                      density="compact"
                      class="custom-list"
              >
                <!--v-list-group 是Vuetify中的一个子组件，用于创建可展开或折叠的列表组。-->
                <v-list-group value="Admin"
                              density="compact"
                              class="custom-list-group"
                >
                  <template v-slot:activator="{ props }">
                    <v-list-item
                        v-bind="props"
                        title="Admin"
                    ></v-list-item>
                  </template>

                  <v-list-item
                      v-for="([title, icon], i) in admins"
                      :key="i"
                      :title="title"
                      :prepend-icon="icon"
                      :value="title"

                  ></v-list-item>
                </v-list-group>

              </v-list>
            </v-list-item>


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
                  <v-checkbox-btn @click.stop v-model="item.isChecked"></v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    :ref="'btnInfo' + index"
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                    @click="onBtnClick(item,$event)"
                ></v-btn>
              </template>

              <v-list-item-title :class="{ 'strike-through': item.isChecked }">
                New Item
              </v-list-item-title>

              <v-list-item-subtitle :class="{ 'strike-through': item.isChecked }">
                New Item Subtitle
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
    admins: [
      ['Management', 'mdi-account-multiple-outline'],
      ['Settings', 'mdi-cog-outline'],
    ],
    items: [],
    isExampleChecked: false,
    buttonClicked: false,
  }),

  methods: {
    onExampleClick() {
      console.log('Sound item clicked');
      this.someData = 'Sound item was clicked';
      this.$emit('item-clicked', this.someData);
    },

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

    addNewItem() {
      this.items.push({
        isChecked: false,
      });
    },

    subSelectedItem() {
      const selectedItem = this.items.find(item => item.isChecked);

      // 如果找到被选中的项，则从数组中删除
      if (selectedItem) {
        const index = this.items.indexOf(selectedItem);
        this.items.splice(index, 1);
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

.list-card {
  margin-left: 50px;
}

.custom-list-group {
  /* 自定义样式规则，根据需要调整大小 */
  width: 500px;
}

.custom-list {
  /* 自定义列表项样式规则，根据需要调整大小 */
  width: 500px;
}

.hover:hover {
  background-color: rgba(255, 250, 205, 1);
  cursor: pointer;
}
.strike-through {
  text-decoration: line-through;
}
</style>
