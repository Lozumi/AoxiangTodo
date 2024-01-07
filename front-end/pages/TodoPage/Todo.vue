<template>
  <div>
    <div class="row">

      <div class="column left">
        <v-card
            class="list-card"
            max-width="700"
        >
          <v-text-field
              v-model="newItemText"
              label="输入要创建的待办并按回车创建"
              hide-details="auto"
              class="custom-input"
              @keydown.enter="addNewItemFromInput"
          ></v-text-field>
          <v-divider></v-divider>

<!--          加不加if真是个问题   <div v-if="items && items.length>0">-->
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
                    icon="mdi-clock"
                    size="x-small"
                    style="font-size: 18px; color:sandybrown"
                    position="relative"
                    @click="toTomatoClock(item)">
                </v-btn>
                <v-btn
                    :ref="'btnInfo' + index"
                    color="grey-lighten-1"
                    icon="mdi-information"
                    variant="text"
                    style="font-size: 20px;"
                    @click.stop="toggleCardDetails(item)"
                ></v-btn>
                <v-btn
                    icon="mdi-minus"
                    size="x-small"
                    style="font-size: 15px"
                    position="relative"
                    @click="subSelectedItem()">
                </v-btn>
                <!--                    @click="onBtnClick(item,$event)"-->
              </template>

              <v-list-item-title :class="{ 'strike-through': item.isChecked }">
                {{item.title}}
              </v-list-item-title>

              <v-list-item-subtitle :class="{ 'strike-through': item.isChecked }">
                {{item.subtitle}}
              </v-list-item-subtitle>

              <!-- 为每个item添加一个详情标识符，用于后续加载对应内容 -->
              <!-- 为什么不是index呢？因为我不知道怎么确保 item 对象中包含一个唯一的 id 属性            -->
              <v-list-item-meta :id="'details_' + item.id"></v-list-item-meta>

            </v-list-item>

          </v-list>
<!--          </div>-->
        </v-card>
      </div>

      <div class="column right" :class="{ 'hidden': !isCardVisible || !currentItemId }">
        <v-card>
          <v-card-text>
            <iframe
                v-if="isCardVisible && currentItemId"
                :src="iframeSrc"
                width="100%"
                height="400px"
            ></iframe>
          </v-card-text>
        </v-card>
      </div>
    </div>
  </div>
</template>



<script setup>
import { useRouter } from 'vue-router';
import { ref,onMounted } from 'vue';
import {useFetch} from "nuxt/app";

let currentItemId = ref(null);
let iframeSrc = ref('');


const router = useRouter();
const newItemText = ref('');
// 增加一个标志位，表示用户是否首次登录
const isFirstLogin = ref(true);

const apiUrl = 'http://localhost:20220/todoworkitem';
// 假设这是获取用户待办事项的API
const userTodosUrl = `http://localhost:20220/user-todos`;


const isCardVisible = ref(false);
const items = ref([]);
const predefinedItems = [
  {
    title: '预设待办事项1',
    subtitle: '这是一个预先定义好的子标题',
    isChecked:false,
  },
  {
    title: '预设待办事项2',
    subtitle: '这是另一个预先定义好的子标题',
    isChecked: false,
  },
];

// 检查用户是否首次登录（实际项目中这部分逻辑应从服务器获取）
// 在这里假设通过某种方式检查并设置 isFirstLogin 的值
// const checkFirstLogin = async () => {
//   // 这里是模拟代码，实际应用中需要替换为从服务器获取数据的逻辑
//   const response = await fetch('/api/check-first-login');
//   const data = await response.json();
//   isFirstLogin.value = data.isFirstLogin;
// };

// onMounted(async () => {
//   await checkFirstLogin();
// });


const addNewItemToServer = async (item) => {
  const requestType = 'CreateToDoWork';
  const Content  = JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    layer: 1,
    innerId: 0,
    importancePriority: 1,
    emergencyPriority: 1,
    title: item.title,
    subtitle:  'None', // 添加默认值或从其他地方获取
    description: '', // 如果需要的话，添加描述
    createTime: new Date().toISOString(),
    startTime: '',
    deadLine: '',
    status: 'Activated',
    subToDoWorkItemInnerIdList: [],
    pomodoroRecordInnerIdList: [],
  });

  const addItemFetcher = useFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      content: Content,
      requestType: requestType,
    }),
  });

  try {
    await addItemFetcher.refresh();
    if (!addItemFetcher.pending.value && !addItemFetcher.error.value) {
      // 请求成功后执行的操作，例如将新添加的待办项添加到本地数组状态或 UI 更新

      const response = addItemFetcher.data.value; // 获取后端返回的数据
      //嘶嘶这个新建项是不是这么添加到本地数组啊。。。
      const newItemWithId = { title:item.title,subtitle:item.subtitle, id: response.id ,isChecked:false}; // 合并新的id
      items.value.push(newItemWithId); // 将带有id的新项目添加到本地数组状态
    }

  } catch (error) {
    console.error('Error adding new item:', error);
  }
};

onMounted(async () => {
  if (isFirstLogin.value) {
    for (const item of predefinedItems) {
      await addNewItemToServer(item);
    }
    // 登录后将首次登录标志位设为 false
    isFirstLogin.value = false;
  } else {
    // 若非首次登录，从服务器获取用户的待办事项列表
    const getUserTodosFetcher = useFetch(userTodosUrl, { method: 'GET' });

    try {
      await getUserTodosFetcher.refresh();
      if (!getUserTodosFetcher.pending.value && !getUserTodosFetcher.error.value) {
        // 获取后端返回的数据
        const response = getUserTodosFetcher.data.value;
        // 将数据添加到本地items数组，并初始化isChecked为false
        items.value = response.map(todo => ({ ...todo, isChecked: false }));
      }
    } catch (error) {
      console.error('Error fetching user todos:', error);
    }
  }
});

const addNewItemFromInput = async () => {
  if (newItemText.value.trim() !== '') {
    const newItem = {
      title: newItemText.value,
      subtitle: 'None', // 如果需要用户输入子标题，请在这里添加逻辑
    };
    await addNewItemToServer(newItem);

    // 清空输入框
    newItemText.value = '';
  }
};

//到底要怎么样才能在卡片展开式按照itemid拉取相应item的内容啊
async function toggleCardDetails(item) {
  if (!this.isCardVisible) {
    this.isCardVisible = true;
    currentItemId.value = item.id; // 设置当前点击项的id???
    this.iframeSrc = `http://localhost:3000/TodoPage/TodoDetails?itemId=${item.id}`;
  }
}

function  onItemOrBtnClick(item, event) {
  event.stopPropagation();
  //接受两个参数：item 表示被点击的项（即 v-list-item 中的数据项），event 表示触发点击事件的事件对象。
  //使用 event.stopPropagation() 阻止事件冒泡，确保只有当前的 v-list-item 收到点击事件，而不会传播到其他元素。
  console.log('Item clicked:', item);
  // 处理 item 的点击逻辑，例如更新数据等操作
}

function subSelectedItem() {
  event.stopPropagation();
  const selectedItem = this.items.find(item => item.isChecked);
  if (selectedItem) {
    const index = this.items.indexOf(selectedItem);
    this.items.splice(index, 1);
  }
}

async function toTomatoClock(item)
{
  await router.push('/TodoPage/Tomato');
  //进入番茄钟页面并传递该item的唯一id给它......
  //计时完成后根据该itemid将数据绑定到item上
}



</script>

<style>
* {
  box-sizing: border-box;
}
body {
  margin: 0;
  overflow:auto;
}

.column {
  float: left;
  padding: 10px;
}

.column.left {
  width:70%;
}

.row {
  display: flex;
  //justify-content: space-between; /* 使子元素在主轴方向两端对齐 */
}

.row:after {
  content: "";
  display: table;
  clear: both;
}

.list1{
  border: 0 solid #303030;
}

@media screen and (max-width: 600px) {
  .column.left {
    width: 100%;
  }
}
</style>

<style scoped>

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

.hidden{
  display:none;
}
</style>
