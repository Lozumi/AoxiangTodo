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
            <!--              要把index改成innerId-->
            <!-- 新增项 -->
            <v-list-item
                v-for="item in items"
                :key="item.innerId"
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
                    @click="toTomatoClock()">
                </v-btn>
                <v-btn
                    :ref="'btnInfo' + item.innerId"
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

      <div class="column right" :class="{ 'hidden': !isCardVisible || !currentItemId }">
        <v-card>
          <v-card-text>
            <iframe
                v-if="isCardVisible && currentItemId"
                :src="iframeSrc"
                class="right-card"
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


const apiUrl = 'http://localhost:20220';


const isCardVisible = ref(false);
const items = ref([]);

/**
 * 将新建的待办事项发送到服务器
 */
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

  /**
   * 内含的钩子函数——添加待办事项的Fetcher
   * @type {AsyncData<PickFrom<FetchResult<"http://localhost:20220", string>, KeysOf<FetchResult<"http://localhost:20220", string>>> | null, FetchError | null>}
   */
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
      const newItemWithId = { title:response.title,subtitle:response.subtitle, innerId: response.innerId ,isChecked:false}; // 合并新的id
      items.value.push(newItemWithId); // 将带有id的新项目添加到本地数组状态
    }

  } catch (error) {
    console.error('Error adding new item:', error);
  }
};

//这里将HTTP方法从POST更改为DELETE，以便符合RESTful API的设计原则
const deleteItemInServer = async (item) => {
  const requestType = 'DeleteToDoWork';
  const deleteData = JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    innerId: item.innerId,
    requestType: requestType,
  });

  const deleteItemFetcher = useFetch(apiUrl, {
    //或者直接在body中传入innerId，取决于你的API设计
    method: 'DELETE', // 通常对于删除操作使用HTTP DELETE方法
    headers: {
      'Content-Type': 'application/json',
    },
    body: deleteData,
  });

  try {
    await deleteItemFetcher.refresh();
    if (!deleteItemFetcher.pending.value && !deleteItemFetcher.error.value) {
      // 请求成功后从本地数组中移除该待办事项
      const index = items.value.indexOf(item);
      if (index !== -1) {
        items.value.splice(index, 1);
      }
    }
  } catch (error) {
    console.error('Error deleting item:', error);
  }
};



// 假设这是获取用户待办事项的API
const userTodosUrl = `http://localhost:20220`;
/**
 * 钩子函数——获取已创建的待办事项列表
 * @returns {Promise<void>}
 */
async function getItemFetcher ()  {
  const requestType = 'EnumerateToDoWorkList';
  const response = await fetch(userTodosUrl, {
    method: 'GET',
    headers: {
      //??Type
      'Content-Type': 'application/json',
    },

  });

  if (!response.ok) {
    throw new Error(`Failed to fetch todos with status ${response.status}`);
  }

  const data = await response.json();

  items.value = data.map((item) => ({
    layer: item.layer,
    innerId: item.innerId,
    importancePriority: item.importancePriority,
    emergencyPriority: item.emergencyPriority,
    title: item.title,
    subtitle: item.subtitle,
    description: item.description,
    createTime: new Date(item.createTime),
    startTime: item.startTime ? new Date(item.startTime) : '',
    deadLine: item.deadLine ? new Date(item.deadLine) : '',
    status: item.status,
    subToDoWorkItemInnerIdList: item.subToDoWorkItemInnerIdList || [],
    pomodoroRecordInnerIdList: item.pomodoroRecordInnerIdList || [],
    isChecked: false, // 初始化isChecked为false
  }));
}

/**
 * 从后端拉取已经创建的待办事项列表
 */
onMounted(async () => {
  try {
    await getItemFetcher();

  } catch (error) {
    console.error('Error fetching user todos:', error);
  }
});

/**
 * 获取输入框中文字创建待办事项
 * @returns {Promise<void>}
 */
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

/**
 * 删除处于勾选项目的item
 */
function subSelectedItem() {
  event.stopPropagation();
  const selectedItem = this.items.find(item => item.isChecked);
  if (selectedItem) {
    deleteItemInServer(selectedItem); // 调用删除服务器端数据的方法
  }
}

/**
 * 到底要怎么样才能在卡片展开式按照itemid拉取相应item的内容啊
 * @param item
 * @returns {Promise<void>}
 */
async function toggleCardDetails(item) {
  if (!this.isCardVisible) {
    this.isCardVisible = true;
    this.iframeSrc = '/TodoDetails';
    currentItemId.value = item.innerId; // 设置当前点击项的id???

  }
}

/**
 * 点击整个item的鸡肋函数
 * @param item
 * @param event
 */
function onItemOrBtnClick(item, event) {
  event.stopPropagation();
  //接受两个参数：item 表示被点击的项（即 v-list-item 中的数据项），event 表示触发点击事件的事件对象。
  //使用 event.stopPropagation() 阻止事件冒泡，确保只有当前的 v-list-item 收到点击事件，而不会传播到其他元素。
  console.log('Item clicked:', item);
}

/**
 * 跳转到特定item的番茄中进行计时??还是没想法
 * @returns {Promise<void>}
 */
async function toTomatoClock(item) {
  await router.push({ name: 'Tomato', params: { itemid: item.innerId } });
  //进入番茄钟页面并传递该item的唯一id给它......
  //计时完成后根据该itemid将数据绑定到item上
}
//  router.js (或相应的路由配置文件)
// {
//   path: '/TodoPage/Tomato/:itemid',
//       component: TomatoPage,
//     name: 'Tomato'
// }
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
  float:left;
  padding: 40px;
}

.column.left {
  width:55%;
  height: 100%;
  margin-left:-40px;
}

.row {
  display: flex;
  border-inline-style: none;
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

.right-card{
  width:500px;
  height:500px;
}
</style>
