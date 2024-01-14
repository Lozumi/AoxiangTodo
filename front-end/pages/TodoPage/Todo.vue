<template>
  <!-- 整体页面结构 -->
  <div>
    <!-- 分为左右两栏的行 -->
    <div class="row">
      <!-- 左侧栏 -->
      <div class="column left">
        <!-- 待办列表的卡片 -->
        <v-card
            class="list-card"
            max-width="700"
        >
          <!-- 输入框，用于创建新待办事项 -->
          <v-text-field
              v-model="newItemText"
              label="输入要创建的待办并按回车创建"
              hide-details="auto"
              class="custom-input"
              @keydown.enter="addNewItemFromInput"
          ></v-text-field>
          <v-divider></v-divider>

          <!-- 待办事项列表 -->
          <v-list lines="two"
                  select-strategy="classic"
                  class="list1"
                  density="compact"
          >
            <v-list-subheader
                class="TodoTitle">最近待办</v-list-subheader>

            <v-divider></v-divider>
            <!-- 循环渲染待办事项 -->
            <!-- 新增项 -->
            <v-list-item
                v-for="(item, index) in items"
                :key="item.innerId"
                value="newItem"
                class="hover"
                @click.stop="onItemOrBtnClick(item)"
            >
              <!-- 待办事项左侧的复选框 -->
              <template v-slot:prepend>
                <v-list-item-action>
                  <v-checkbox-btn
                      @click.stop
                      v-model="item.isChecked">
                  </v-checkbox-btn>
                </v-list-item-action>
              </template>

              <!-- 待办事项右侧的操作按钮 -->
              <template v-slot:append>
                <!-- 番茄钟按钮 -->
                <v-btn
                    icon="mdi-clock"
                    size="x-small"
                    style="font-size: 19px; color:olivedrab"
                    position="relative"
                    @click.stop="toTomatoClock(item.innerId.valueOf())">
                </v-btn>
                <!-- 查看待办详情按钮 -->
                <v-btn
                    :ref="'btnInfo' + index"
                    class="details-btn"
                    :disabled="isCardVisible "
                    icon=" mdi-file-document-outline"
                    variant="text"
                    style="font-size: 20px;"
                    @click.stop="toggleCardDetails(item.innerId.valueOf())"
                ></v-btn>
                <!-- 删除待办按钮 -->
                <v-btn
                    icon="mdi-minus"
                    size="x-small"
                    style="font-size: 15px"
                    position="relative"
                    @click.stop="subSelectedItem(item)">
                </v-btn>
              </template>

              <!-- 待办事项标题和描述 -->
              <v-list-item-title :class="{ 'strike-through': item.isChecked }">
                {{item.title}}
              </v-list-item-title>

              <v-list-item-subtitle :class="{ 'strike-through': item.isChecked }">
                {{item.description}}
              </v-list-item-subtitle>


            </v-list-item>
          </v-list>
        </v-card>
      </div>
      <!-- 右侧栏，用于展示待办事项的详细信息 -->
      <div class="column right"
           :class="{ 'hidden': !isCardVisible  }">
        <v-card>
          <v-card-text>
            <!-- 使用iframe展示详细信息 -->
            <iframe
                v-if="isCardVisible "
                :src="iframeSrc"
                class="right-card"
            ></iframe>
          </v-card-text>
          <!-- 新增关闭卡片按钮，仅在卡片显示时出现 -->
          <template v-if="isCardVisible ">
            <v-btn
                class="close-details-btn"></v-btn>
            <!--  @click.stop="toggleCardDetails(item.innerId.valueOf())"-->
          </template>
        </v-card>
      </div>
    </div>
  </div>
</template>



<script setup lang="ts">
// 导入相关组件和工具函数
import { useRouter } from 'vue-router';
import { ref,onMounted } from 'vue';
import ToDoWorkRequest from '@/composables/ToDoWorkRequest'

// 响应式变量
let currentItemId = ref(null);// 当前待办事项的ID
let iframeSrc = ref('getDetailUrl(currentItemId)');// iframe 的 src 属性
const apiUrl = 'http://127.0.0.1:20220';
const isCardVisible = ref(false);// 卡片是否可见

const router = useRouter();
const items = ref([] as{
  title: string;
  description: string | null; // 添加description属性，并设置默认值为null
  isChecked: boolean;
  innerId: number;
}[]);
const newItemText = ref('');


// 日期格式化函数
function formatISOString(date: Date) {
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


// 创建新待办事项
async function addNewItemFromInput() {
  if (newItemText.value) {
    const newTodo = JSON.stringify({
      '@class': 'shared.ToDoWorkItem',
      layer: 1,
      innerId: 0,
      importancePriority: 1,
      emergencyPriority: 1,
      title: newItemText.value,
      subtitle: 'None',
      description: 'None',
      createTime: formatISOString(new Date()),
      startTime: formatISOString(new Date()),
      deadLine: formatISOString(new Date()),
      status: 'Activated',
      subToDoWorkItemInnerIdList: [],
      pomodoroRecordInnerIdList: [],
    });

    const {data, error} = await ToDoWorkRequest.create(newTodo);
    //原本为JSON.parse(data.value);
    const result=JSON.parse(data.value as string);
    const createdTodoId = result.content;
    console.log(data);
    console.log(createdTodoId);
    try {
      if (newItemText.value !== '') {
        items.value.push({
          innerId: createdTodoId,
          isChecked: false,
          description: createdTodoId.toString(),
          // 这样？ description 给后端传的就是 ;none
          //好的嘞，能修改就行，还有那个列表，如果select好用用select吧。 ok
          title: newItemText.value
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

// 删除选中的待办事项
async function subSelectedItem(item:any) {

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

// 从列表中删除指定ID的待办事项
async function deleteItemFromList(innerId:number) {
  // 查找具有指定innerId的项目的索引
  const indexToDelete = items.value.findIndex(item => item.innerId === innerId);
  if (indexToDelete !== -1) {
    // 从items列表中删除该项目
    items.value.splice(indexToDelete, 1);
  }
}

// 获取待办事项列表
async function getItemList()
{
  console.log('Fetching to-do items on page refresh...');
  const {data} = await ToDoWorkRequest.enumerate();
  if (typeof data.value === 'string') {
    console.log('data.value:',data.value);
    try {
      const response = JSON.parse(data.value);

      if (typeof response.content === 'string') {
        const jsonItems = JSON.parse(response.content) as Array<{ [key: string]: any }>;

        if (Array.isArray(jsonItems)) {
          items.value = jsonItems.map((itemData) => ({
            innerId: itemData.innerId,
            isChecked: itemData.isChecked, // 默认值设为 false
            title: itemData.title,
            description: itemData.description || null,
          }));
        }
      }
      console.log(items.value);
    } catch (error) {
      console.error('Failed to parse data.value as JSON:', error);
      return;
    }
  }
}

// 组件挂载时获取待办事项列表
onMounted(async () => {
  // 如果有初始数据加载需求，请在此处使用useFetch或其他方式获取待办事项列表
  console.log('Component mounted, fetching to-do items...');
  await nextTick();
  await getItemList();
});



/**
 * 测试item中独立点击组件的功能
 * @param item 被点击的待办事项
 */
async function onItemOrBtnClick(item: any) {
  console.log('Item clicked:', item);
  //点击事件绑定包含.stop修饰符，意味点击单个组件是独立触发，不会影响其他组件
}

/**
 * 跳转到特定item的番茄中进行计时??还是没想法
 *  @param innerId 待办事项的ID
 */
async function toTomatoClock(innerId:number): Promise<void> {
  await router.push({ path: '/TodoPage/TomatoClock/', query: { itemId:innerId } });
  //进入番茄钟页面并传递该item的唯一id给它......
  //计时完成后根据该itemid将数据绑定到item上

}

/**
 * 切换卡片的显示状态
 * @param innerId 待办事项的ID
 */
function getDetailUrl(itemId: number | null) {
  if (itemId === null) return '/TodoDetails'; // 当没有选中项时返回默认地址

  return `/TodoDetails?itemId=${itemId}`;
}

/**
 //  * 跳转到特定item的番茄中进行计时??还是没想法
 //  * @returns {Promise<void>}
 //  */

/**
 * 怎么样才能在卡片展开式按照itemid拉取相应item的内容?
 * @returns {Promise<void>}
 */
async function toggleCardDetails(innerId:number): Promise<void> {
  await router.push({ path: '/TodoPage/ToDoDetails/', query: { itemId:innerId} });
  // if (isCardVisible.value) {
  //   // 隐藏卡片，并清空内容（这里假设iframe会重新加载页面）
  //   isCardVisible.value = false;
  //   currentItemId.value = null;
  //   console.error("隐藏卡片");
  // } else {
  //   // 显示卡片，并设置当前选中项
  //   isCardVisible.value = true;
  //   currentItemId.value = item.innerId;
  //   console.error("显示卡片:");
  // }
  // console.error("根本没有卡片");
}

</script>

<style>
/* 设置所有元素的盒模型为边框盒模型，使计算方式包含内边距和边框 */
* {
  box-sizing: border-box;
}
/* 清除body的默认外边距并设置滚动条 */
body {
  margin: 0;
  overflow:auto;
}
/* 定义一个通用的column类，左浮动，并添加内边距 */
.column {
  float:left;
  padding: 40px;
}
/* 左侧列，宽度55%，高度100%，负外边距使列向左对齐 */
.column.left {
  width:55%;
  height: 100%;
  margin-left:-40px;
}
/* 右侧列，使用flex布局实现居中，同时添加相对定位以便子元素绝对定位 */
.column.right{
  display: flex;
  justify-content: center; /* 水平居中 */
  position: relative; /* 添加相对定位以便子元素绝对定位 */
}
/* 定义row类，使用flex布局，清除浮动 */
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
/* list1类，设置边框样式 */
.list1{
  border: 0 solid #303030;
}

/* 响应式设计：当屏幕宽度小于或等于600px时，左侧列宽度变为100% */
@media screen and (max-width: 600px) {
  .column.left {
    width: 100%;
  }
}
</style>


//新的一组样式（scoped)
<style scoped>
/* 文本居中并对齐方式设为大号字体 */
.TodoTitle{
  text-align: center;
  font-size: large;
}
/* 定义list-card类，添加左边距 */
.list-card {
  margin-left: 50px;
}
/* hover伪类，鼠标悬停时背景色变为浅黄色，光标变为pointer形状 */
.hover:hover {
  background-color: rgba(255, 250, 205, 1);
  cursor: pointer;
}
/* strike-through类，给文本添加删除线效果 */
.strike-through {
  text-decoration: line-through;
}
/* 隐藏类，设置display为none隐藏元素 */
.hidden{
  display:none;
}
/* right-card类，定义宽度、高度及垂直居中显示（需与position:absolute配合使用） */
.right-card {
  .right-card {
    width: 550px;
    height: 500px;
    margin: auto; /* 垂直居中，配合position: absolute;使用 */

    right: 0; /* 紧贴屏幕右侧 */
  }
  /* details-btn类，设置按钮颜色为浅石板灰 */
  .details-btn {
    color: lightslategray;
  }
  /* close-details-btn类，设置按钮位置在右下角，浮动到右边，文本居中 */
  .close-details-btn {
    bottom: 10px;
    right: 10px;
    float: right;
    text-align: center;
    //position: absolute;
  }
}
</style>
