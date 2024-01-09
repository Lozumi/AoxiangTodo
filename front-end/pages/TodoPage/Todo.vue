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
                v-for="(item, index) in items"
                :key="item.innerId"
                value="newItem"
                class="hover"
                @click="onItemOrBtnClick(item,event)"
            >
              <template v-slot:prepend>

                <v-list-item-action>
                  <v-checkbox-btn
                      @click.stop
                      v-model="item.isChecked">
                  </v-checkbox-btn>
                </v-list-item-action>
              </template>

              <template v-slot:append>
                <v-btn
                    icon="mdi-clock"
                    size="x-small"
                    style="font-size: 19px; color:olivedrab"
                    position="relative"
                    @click="toTomatoClock(item)">
                </v-btn>
                <v-btn
                    :ref="'btnInfo' + index"
                    class="details-btn"
                    :disabled="isCardVisible "
                    icon=" mdi-file-document-outline"
                    variant="text"
                    style="font-size: 20px;"
                    @click.stop="toggleCardDetails(item)"
                ></v-btn>
                <v-btn
                    icon="mdi-minus"
                    size="x-small"
                    style="font-size: 15px"
                    position="relative"
                    @click="subSelectedItem(item)">
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
      <div class="column right"
           :class="{ 'hidden': !isCardVisible  }">

      <!-- ... 卡片内容 -->
        <v-card>
          <v-card-text>
            <iframe
                v-if="isCardVisible "
                :src="iframeSrc"
                class="right-card"
            ></iframe>
          </v-card-text>
          <!-- 新增关闭卡片按钮，仅在卡片显示时出现 -->
          <template v-if="isCardVisible ">
            <v-btn
                class="close-details-btn"
                @click.stop="toggleCardDetails(items.find(i => i.innerId === currentItemId.value))"
            >关闭详情</v-btn>
          </template>
        </v-card>
      </div>
    </div>
  </div>
</template>



<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ref,onMounted } from 'vue';
import ToDoWorkRequest from '@/composables/ToDoWorkRequest'

let currentItemId = ref(null);
let iframeSrc = ref('/TodoDetails');
const apiUrl = 'http://127.0.0.1:20220';
const isCardVisible = ref(false);

const router = useRouter();
const items = ref([] as{
  title: string;
  subtitle: string | null; // 添加subtitle属性，并设置默认值为null
  isChecked: boolean;
  innerId: number | null;
}[]);
const newItemText = ref('');


function formatISOString(date) {
  // 获取年、月、日、小时、分钟、秒
  const year = date.getFullYear();
  const month = ("0" + (date.getMonth() + 1)).slice(-2);
  const day = ("0" + date.getDate()).slice(-2);
  const hours = ("0" + date.getHours()).slice(-2);
  const minutes = ("0" + date.getMinutes()).slice(-2);
  const seconds = ("0" + date.getSeconds()).slice(-2);

  // 拼接成 "年-月-日T时:分:秒" 格式
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

onMounted(async () => {
  // 如果有初始数据加载需求，请在此处使用useFetch或其他方式获取待办事项列表
     await getItemList();
});



async function addNewItemFromInput() {
  if (newItemText.value) {
    const newTodo = JSON.stringify({
      '@class': 'shared.ToDoWorkItem',
      layer: 1,
      innerId: 0,
      importancePriority: 1,
      emergencyPriority: 1,
      title: newItemText.value,
      subtitle: '',
      description: '',
      createTime: formatISOString(new Date()),
      startTime: formatISOString(new Date()),
      deadLine: formatISOString(new Date()),
      status: 'Activated',
      subToDoWorkItemInnerIdList: [],
      pomodoroRecordInnerIdList: [],
    });

    const {data, error} = await ToDoWorkRequest.create(newTodo);
    const createdTodoId = data.value;
    try {
      if (newItemText.value !== '') {
        items.value.push({
          title: newItemText.value,
          subtitle: 'None',
          isChecked: false,
          innerId: createdTodoId
        });
        newItemText.value = '';
      } else {
        console.error('Error adding new item:', error);
      }
    }catch (error)
    {
      console.error('Error adding new item:',error);
    }
  }
}

async function subSelectedItem(item) {
  event.stopPropagation();

  if (item.isChecked) {
    try {
      //注意，从后端是没有数据传过来的
      const {error} = await ToDoWorkRequest.delete(item.innerId);
      console.log('后端删除成功');
      // 等待删除操作完成后再更新items列表
      await deleteItemFromList(item.innerId);
      console.log('后端删除成功，本地列表已同步更新');
    }catch (error)
    {
      console.error('Error is:',error);
    }
  }
}

 async function deleteItemFromList(innerId) {
  // 查找具有指定innerId的项目的索引
  const indexToDelete = items.value.findIndex(item => item.innerId === innerId);
  if (indexToDelete !== -1) {
    // 从items列表中删除该项目
    items.value.splice(indexToDelete, 1);
  }
}

async function getItemList()
{
    const {data} = await ToDoWorkRequest.enumerate();
    const jsonItems = JSON.parse(data.value);

  if (Array.isArray(jsonItems)) {
    items.value = jsonItems.map((itemData) => ({

      layer: itemData.layer,
      innerId: itemData.innerId || null,
      importancePriority: itemData.importancePriority,
      emergencyPriority: itemData.emergencyPriority,
      title: itemData.title,
      subtitle: itemData.subtitle || null,
      description: itemData.description,
      createTime: itemData.createTime,
      startTime: itemData.startTime,
      deadLine: itemData.deadLine,
      status: itemData.status,
      subToDoWorkItemInnerIdList: itemData.subToDoWorkItemInnerIdList,
      pomodoroRecordInnerIdList: itemData.pomodoroRecordInnerIdList,

      isChecked: itemData.isChecked || false,
    }));
  }
}

function getDetailUrl(itemId) {
  return `/TodoDetails?itemId=${itemId}`;
}

  /**
   * 到底要怎么样才能在卡片展开式按照itemid拉取相应item的内容啊
   * @param item
   * @returns {Promise<void>}
   */
  async function toggleCardDetails(item) {
    if (isCardVisible.value) {
      // 隐藏卡片，并清空内容（这里假设iframe会重新加载页面）
      isCardVisible.value = false;
      currentItemId.value = null;
      console.error("隐藏卡片");
    } else {
      // 显示卡片，并设置当前选中项
      isCardVisible.value = true;
      currentItemId.value = item.innerId;
      console.error("显示卡片:");
    }
    console.error("根本没有卡片");
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
    await router.push({name: 'TomatoClock', params: {itemId: item.innerId}});
    //进入番茄钟页面并传递该item的唯一id给它......
    //计时完成后根据该itemid将数据绑定到item上

}

//  需要nico配个路由才行  router.js (或相应的路由配置文件)
// {
//   path: '/TodoPage/TomatoClock/:itemId',
//       component: TomatoPage,
//     name: 'TomatoClock'
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

.column.right{
  display: flex;
  justify-content: center; /* 水平居中 */
  position: relative; /* 添加相对定位以便子元素绝对定位 */
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
  width: 550px;
  height: 500px;
  margin: auto; /* 垂直居中，配合position: absolute;使用 */

  right: 0; /* 紧贴屏幕右侧 */
}

.details-btn{
  color:lightslategray;
}

.close-details-btn{
  bottom:10px;
  right:10px;
  float:right;
  text-align: center;
  //position: absolute;
}
</style>
