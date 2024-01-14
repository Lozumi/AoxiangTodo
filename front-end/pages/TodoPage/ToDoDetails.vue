<template>
  <div>
    <v-app>
      <!-- 应用栏组件，设置无阴影效果和背景色 -->
      <v-app-bar :elevation="0" color="#3A8FB7">
        <v-toolbar-title>待办详情</v-toolbar-title>
        <!-- 返回到待办列表的按钮，带有图标 -->
        <v-btn icon="mdi-chevron-right" @click="backToToDoList"></v-btn>
      </v-app-bar>
      <!-- 主内容区域 -->
      <v-main>
        <!-- 容器组件 -->
        <v-container>
          <!--选择分类文件夹-->
          <v-list-item>
            <!-- 底部弹出式菜单，用于选择文件夹 -->
            <v-bottom-sheet v-model="sheet">
              <!-- 激活器，点击时显示底部弹出菜单 -->
              <template v-slot:activator="{ props }">
                <div>
                  <v-btn v-bind="props" elevation="0" :color="buttonColor">
                    <v-icon size="24">mdi-folder-star-outline</v-icon>
                    {{ buttonText }}
                  </v-btn>
                </div>
              </template>
              <!-- 文件夹列表 -->
              <v-list>
                <!-- 分类标题 -->
                <v-list-subheader>文件夹</v-list-subheader>
                <!-- 循环遍历文件夹列表 -->
                <v-list-item v-for="(tile, index) in tiles" :key="tile.title" @click="changeSheet(tile.title);
                changeContent(tile.title);
                openDialog(tile.title);
                changeColor(tile.color)">
                  <!-- 图标预置区域 -->
                  <template v-slot:prepend>
                    <v-avatar :color="tile.color" size="x-small"></v-avatar>
                  </template>

                  <!-- 文件夹名称 -->
                  <v-list-item-title>
                    <!-- 使用条件语句判断是否要对该item实现特殊处理 -->
                    <span v-if="tile.title === specialTitle" style="color: lightskyblue;">{{ tile.title }}</span>
                    <span v-else>{{ tile.title }}</span>
                  </v-list-item-title>

                  <!-- 删除文件夹操作区域 -->
                  <template v-slot:append>
                    <!-- 删除按钮 -->
                    <v-btn elevation="0">
                      <v-icon>mdi-trash-can-outline</v-icon>
                      <!-- 弹出确认删除对话框 -->
                      <v-dialog v-model="_dialog" activator="parent" width="auto">
                        <v-card>
                          <v-card-text>
                            确定要删除这个文件夹吗？
                          </v-card-text>
                          <!-- 对话框关闭按钮 -->
                          <v-card-actions>
                            <v-btn color="primary" block @click="_dialog = false">关闭
                            </v-btn>
                          </v-card-actions>
                          <!-- 确认删除按钮 -->
                          <v-card-actions>
                            <v-btn color="primary" block @click="_dialog = false; removeFolder(tile.title)">确定
                            </v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-dialog>
                    </v-btn>
                  </template>
                </v-list-item>
              </v-list>

              <!-- 新建文件夹对话框 -->
              <v-dialog v-model="fileDialog" max-width="600">
                <v-card>
                  <!-- 输入新建文件夹名称的文本框 -->
                  <v-text-field v-model="inputText" label="文件夹名称" color="#3A8FB7"></v-text-field>
                  <!-- 对话框操作区域 -->
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <!-- 关闭对话框按钮 -->
                    <v-btn color="blue-darken-1" variant="text" @click="fileDialog = false">
                      关闭
                    </v-btn>
                    <!-- 保存并关闭对话框按钮 -->
                    <v-btn color="blue-darken-1" variant="text" @click="fileDialog = false;
                    changeContent(inputText);
                    addFolder(inputText)">
                      保存
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-bottom-sheet>
          </v-list-item>

          <v-divider></v-divider> <!--下划线-->
          <v-container>

            <!--任务名-->
            <v-text-field label="任务标题" color="#3A8FB7" variant="solo-filled" v-model="inputTodoTitle"></v-text-field>

            <!--              &lt;!&ndash;任务备注&ndash;&gt;-->
            <!--              <v-text-field-->
            <!--                  label="任务副标题"-->
            <!--                  color="#3A8FB7"-->
            <!--                  v-model="inputTodoSubtitle"-->
            <!--              ></v-text-field>-->

            <!--任务描述-->
            <v-textarea placeholder="任务描述" variant="outlined" color="#536976" v-model="inputTodoDescription"></v-textarea>


            <!--任务时间-->
            <!--任务开始日期-->
            <p style="font-weight: bold;color: #9796f0">待办开始时间</p>
            <!-- 开始日期按钮组 -->
            <v-btn-toggle v-model="toggleStart" color="#9796f0" variant="outlined">
              <v-btn>今天</v-btn>
              <v-btn>明天</v-btn>
              <!-- 选择日期按钮 -->
              <v-btn @click="expand = !expand; togglePanel" elevation="0">
                <v-icon>mdi-calendar</v-icon>
                {{ formattedDate }}
              </v-btn>
            </v-btn-toggle>
            <!-- 开始日期展开/折叠过渡动画 -->
            <v-expand-transition>
              <!-- 展开后显示的日历选择器 -->
              <v-card v-show=" expand ">
                <v-date-picker color="#9796f0" v-model=" startDate " @click="expand = !expand"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--待办结束日期-->
            <p style="font-weight: bold;color: #ee9ca7">待办结束时间</p>
            <!-- 结束日期按钮组 -->
            <v-btn-toggle v-model=" toggleEnd " color="#ee9ca7" variant="outlined">
              <v-btn>今天</v-btn>

              <v-btn>明天</v-btn>
              <!-- 选择日期按钮 -->
              <v-btn @click=" expand_over = !expand_over; togglePanel_over " elevation="0">
                <v-icon>mdi-calendar</v-icon>
                {{ formattedDate_over }}
              </v-btn>

            </v-btn-toggle>
            <!-- 结束日期展开/折叠过渡动画 -->
            <v-expand-transition>
              <!-- 展开后显示的日历选择器 -->
              <v-card v-show=" expand_over ">
                <v-date-picker color="#ee9ca7" v-model=" endDate " @click="expand_over = !expand_over"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--任务重要程度设置-->
            <p style="font-weight: bold;
                                         color: #3A8FB7">
              任务重要程度
            </p>

            <v-select :items="depImportantEnumItems"
                      item-title="text"
                      v-model="importancePriority"
            >
            </v-select>


            <!--任务重要程度设置-->
            <p style="font-weight: bold;color: #3A8FB7">任务紧急程度</p>

            <!-- 重要程度下拉选择器 -->
            <v-select :items="depEmergencyEnumItems"
                      item-title="text"
                      v-model="emergencyPriority"
            >
            </v-select>

            <!-- 工具栏，包含删除任务按钮 -->
            <v-toolbar color="white">
              <template v-slot:append>
                <!-- 删除任务按钮，点击时显示确认对话框 -->
                <v-btn color="#3A8FB7">
                  <v-icon>mdi-trash-can-outline</v-icon>
                  <v-dialog v-model=" dialog " activator="parent" width="auto">
                    <v-card>
                      <v-card-text>
                        确定要删除这个任务吗？
                      </v-card-text>
                      <!-- 对话框关闭按钮 -->
                      <v-card-actions>
                        <!-- 确认删除按钮 -->
                        <v-btn color="primary" block @click="dialog = false">关闭
                        </v-btn>
                      </v-card-actions>
                      <v-card-actions>
                        <v-btn color="primary" block @click="dialog = false">确定
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-btn>
              </template>
            </v-toolbar>
          </v-container>

        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script setup lang="ts">
// 导入 Vue 组件和工具库
import {computed, type Ref, ref, watch} from 'vue';
import ToDoWorkRequest from "~/composables/ToDoWorkRequest";
import { useRoute } from 'vue-router';
import {isArray} from "lodash-es";

// 任务标题相关变量定义
let inputTodoTitle = ref<string>('');
let inputTodoSubtitle = ref<string>('');
let inputTodoDescription = ref<string>('');

// 任务程度相关变量定义
let toggleStart = ref<number>(2);
let toggleEnd = ref<number>(2);
let createTime = ref<number>();
let startTime = ref<number>();
let endTime = ref<number>();
let status = ref<string>();

//任务程度相关
let importancePriorityTitle = ref<string>('未设置');//任务重要程度标题
let importancePriorityColor = ref<string>('gray');// 任务重要程度颜色
const listModelImportancePriority = computed(()=>[importancePriority.value])
let emergencyPriorityTitle = ref<string>('未设置');// 任务紧急程度标题
let emergencyPriorityColor = ref<string>('gray');// 任务紧急程度颜色

// 定义 待办事项 接口类型
interface ToDoWorkItem{
  layer: 1,
  innerId: 0,
  importancePriority: 1,
  emergencyPriority: 1,
  title: 'None',
  subtitle: 'None',
  description: 'None',
  workCreateTime:'number',
  workStartTime:'number',
  workDeadline:'number',
  workStatus:'number',
  workSubToDoWorkItemInnerIdList:'',
  workPomodoroRecordInnerIdList:'',
}

// 其他辅助变量
const item =ref<undefined>;

const text = ref<string>('center');

const icon = ref<string>('justify');

const expand = ref<boolean>(false);

const expand_over = ref<boolean>(false);

const _dialog = ref<boolean>(false);

const dialog = ref<boolean>(false);

const fileDialog = ref<boolean>(false);

const buttonText = ref<string>('未分类');

const buttonColor = ref<string>('primary');

const panelOpen = ref<boolean>(false);

const sheet = ref<boolean>(false);

const inputText = ref<string>('');

const startDate = ref<Date | null>(null);

const endDate = ref<Date | null>(null);

// 用于编辑或新建任务时的临时数据存储
let itemId = ref<number|null>(null);

let importancePriority = ref<number>(0);

let emergencyPriority = ref<number>(0);

let title = ref<string>('');

let subtitle = ref<string>('');

let description = ref<string>('');


let deadLine = ref<string>('');

// 接收从服务器或其他组件传递过来的任务列表
const receivedItems = ref<{
  layer:number;
  innerId: number;
  importancePriority:number;
  emergencyPriority:number;
  title: string;
  subtitle:string;
  description?: string;
  workCreateTime:string;
  workStartTime:string;
  workDeadline:string;
  workStatus:string;
  workSubToDoWorkItemInnerIdList:any;
  workPomodoroRecordInnerIdList:any;
}[]>([]);

// 各种日期相关的字符串变量
const start_today = ref<string>('');

const start_tomorrow = ref<string>('');

const start_date = ref<string>('');

const end_today = ref<string>('');

const end_tomorrow = ref<string>('');

const end_date = ref<string>('');

// 用于选择任务优先级的选择器值
const inputImportanceId = ref<number>(0);

const inputEmergencyId = ref<number>(0);

// 使用路由实例
const route = useRoute();

// 定义标签列表，用于文件夹分类等场景
const tiles = ref<{ color: string; title: string; }[]>([
  { color: '#3A8FB7', title: '自定义' },
  { color: '#dab8ee', title: '不选择文件夹' },
  { color: '#96DEDA', title: '学习' },
  { color: '#ffc500', title: '工作' },
  { color: '#FFB88C', title: '日常' },
  { color: '#49a09d', title: '旅行' },
]);

// 特殊标题，可能用于某些定制化场景
const specialTitle = ref<string>('自定义');

// 选中的标签或分类
const selected = ref<string[]>([]);

// 加载状态
const loading = ref<boolean>(false);

// 搜索关键词
const search = ref<string>('');

// 定义两个重要的枚举列表，它们具有相同的结构（文本、颜色、索引和值）
const depImportantEnumItems = [
  {
    text: '不重要',
    color: '#C3E2C2',
    index: '1',
    value: 1,
  },
  {
    text: '较不重要',
    color: '#EAECCC',
    index: '2',
    value:2,
  },
  {
    text: '比较重要',
    color: '#DBCC95',
    index: '3',
    value:3,
  },
  {
    text: '很重要',
    color: '#CD8D7A',
    index: '4',
    value:4,
  },
]


const importantEnumItems = Object.freeze(depImportantEnumItems);

const depEmergencyEnumItems = [
  {
    text: '不紧急',
    color: '#C3E2C2',
    index: '1',
    value:1,
  },
  {
    text: '较不紧急',
    color: '#EAECCC',
    index: '2',
    value:2,
  },
  {
    text: '比较紧急',
    color: '#DBCC95',
    index: '3',
    value:3,
  },
  {
    text: '很紧急',
    color: '#CD8D7A',
    index: '4',
    value:4,
  },
]

const emergencyEnumItems = Object.freeze(depEmergencyEnumItems);

// 计算属性：检查所有选项是否都被选中
const allSelected = computed(() => isEqual());

// 检查所选项数量是否与importantEnumItems长度相等的函数
function isEqual() {
  return selected.value.length == importantEnumItems.length;
}

// 计算属性：根据搜索内容过滤并返回重要性枚举列表
const categories = computed(() => getCategories());

// 根据search值过滤重要性枚举项的函数
function getCategories() {
  const search1 = search.value.toLocaleLowerCase();

  if (!search1) {
    return importantEnumItems;
  }

  return importantEnumItems.filter(item => {
    const text = item.text.toLocaleLowerCase();

    return text.indexOf(search1) > -1;
  })
}

// 计算属性：获取已选择的项目集合
const selections = computed(() => getSelections())


// 获取当前已选择项目的函数
function getSelections() {
  const selections1 = [];

  for (const selection1 of selected.value) {
    selections1.push(selection1);
  }

  return selections1;
}

// 计算属性：格式化开始日期
const formattedDate = computed(() => formatStartDate());

// 计算属性：格式化结束日期
const formattedDate_over = computed(() => formatEndDate());

// 格式化开始日期的函数
function formatStartDate() {
  if (startDate.value) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return startDate.value.toLocaleString('en-US', options);
  } else {
    let undef = '自定义';
    return undef;
  }
}

// 格式化结束日期的函数
function formatEndDate() {
  if (endDate.value) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return endDate.value.toLocaleString('en-US', options);
  } else {
    let undef = '自定义';
    return undef;
  }
}

// 监听selected值的变化，重置搜索框内容
watch(selected, () => {
  search.value = '';
});

// 监听多个输入变量，并在变化时执行exportToDoWork函数
watch([
  inputTodoTitle,
  inputTodoDescription,
  toggleStart,
  startDate,
  toggleEnd,
  endDate,
  // sheet,
  // inputText,

  importancePriority,
  importancePriorityTitle,
  emergencyPriority,
  emergencyPriorityTitle
], (...params) => {
  console.log('request', params)

  // current work
  exportToDoWork({})
});

// 点击按钮时执行的回调函数
function buttonClicked(buttonName: string) {
  console.log(`${buttonName} clicked`);
}

// 点击“下一步”时的处理函数，模拟加载状态并清空相关数据
function next() {
  loading.value = true;

  setTimeout(() => {
    search.value = '';
    selected.value = [];
    loading.value = false;
  }, 2000)
}

// 切换面板显示/隐藏状态
function togglePanel() {
  panelOpen.value = !panelOpen.value;
}

// 另一个切换面板的方法
function togglePanel_over() {
  panelOpen.value = !panelOpen.value;
}

// 更改按钮标题内容的函数
function changeContent(title: string) {
  if (title == '不选择文件夹') {
    buttonText.value = '未分类';
  } else {
    buttonText.value = title;
  }
}

// 更改按钮颜色的函数
function changeColor(color: string) {
  if (color == '不选择文件夹') {
    buttonColor.value = 'primary';
  } else {
    buttonColor.value = color;
  }
}

// 打开或关闭文件对话框的函数
function openDialog(title: string) {
  if (title == '自定义') {
    fileDialog.value = true;
  } else {
    fileDialog.value = false;
  }
}

// 切换sheet状态的函数
function changeSheet(title: string) {
  if (title === '自定义') {
    sheet.value = true;
  } else {
    sheet.value = false;
  }
}

// 添加文件夹到tiles数组的函数
function addFolder(title: string) {
  tiles.value.push({
    color: getRandomColor(),
    title: title
  })
}

//用于生成随机颜色（十六进制格式）
function getRandomColor() {
  // 使用Math.random()生成0到16777215之间的随机整数，并转换为十六进制字符串表示颜色值
  return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
}

// 从tiles数组中移除指定索引位置的元素
function removeFolder(index: number) {
  tiles.value.splice(index, 1);
}

// 修改优先级标题的重要级别文本内容
function changeTitle(text: string) {
  importancePriorityTitle.value = text;
}
//
// function changeTitle_1(text: string) {
//   emergencyPriorityTitle.value = text;
// }
//
// function changeExColor(color: string) {
//   importancePriorityColor.value = color;
// }
//
// function changeExColor_1(color: string) {
//   emergencyPriorityColor.value = color;
// }



// 定义异步函数，用于导航回待办事项列表页面
async function backToToDoList() {
  await navigateTo("/TodoPage/Todo");
}

// Vue生命周期钩子：组件挂载后执行的操作
onMounted(() => {
  const route = useRoute();// 获取当前路由对象
  const itemId = computed(()=>parseInt(route?.query?.itemId ?? '-1',10));// 解析URL查询参数中的itemId并将其转换为数字

  if (itemId.value != null) {// 如果itemId有效
    console.log('receivedInnerId:', itemId.value);// 输出接收到的itemId
    QueryToDoWork(itemId.value);// 根据itemId查询待办事项详情
  } else {
    console.error('error:真蚌埠住了');// 如果itemId无效则输出错误信息
  }
});

//
// function changeImportanceItem(...e) {
//   console.log(e);
// }

// 异步函数，根据innerId查询待办事项详情
async function QueryToDoWork(innerId: number) {
  let currentToDoWork = null;

  // 调用API查询指定innerId的待办事项
  const { data, pending, error, refresh }  =await (ToDoWorkRequest.query(innerId));
  console.log({ data, pending, error, refresh });// 输出查询结果状态
  await refresh();// 刷新数据以获取最新状态

  // 将查询结果赋值给currentToDoWork
  currentToDoWork = data?.value;
  // 如果查询结果是JSON格式的字符串，则尝试解析为对象
  if (typeof currentToDoWork === 'string') {
    console.log('data.value...:',currentToDoWork);

    try {
      const response = JSON.parse(currentToDoWork);

      // 检查content属性是否为字符串化的数组，并将其解析为具体的待办事项对象数组
      if (typeof response.content === 'string') {
        const jsonContentArrayString = response.content;
        const responseContentArray = JSON.parse(jsonContentArrayString) as ToDoWorkItem[];
        if (!isArray(responseContentArray)) {
          return importToDoWork(responseContentArray);// 若解析出错或非数组，则直接处理结果
        }

        console.log('responseContentArray:',responseContentArray);

        // 在数组中查找与 innerId 匹配的待办事项对象
        const targetItem = responseContentArray.find(item => item.innerId === innerId);

        if (targetItem) {
          importToDoWork(targetItem);// 找到目标待办事项时导入其详细信息
        } else {
          console.error(`找不到ID为 ${innerId} 的待办事项`);
        }
      } else {
        console.error('Failed to parse content property as JSON array.');
      }
    } catch (error) {
      console.error('Failed to parse data.value as JSON:', error);
      return;
    }
  }
}

// 导入待办事项详细信息到表单中
function importToDoWork(currentToDoWork: { [key: string]: any }) {
  console.log(`importToDoWork currentToDoWork`, currentToDoWork)
// 将各项属性值设置到对应的UI组件变量上
  inputTodoTitle.value = currentToDoWork.title;
  inputTodoSubtitle.value = currentToDoWork.subtitle;
  inputTodoDescription.value = currentToDoWork.description;
  startTime.value = currentToDoWork.startTime;
  deadLine.value = currentToDoWork.deadLine;
  importancePriority.value = Number(currentToDoWork.importancePriority);
  emergencyPriority.value = Number(currentToDoWork.emergencyPriority);
  createTime.value = currentToDoWork.createTime;
  endTime.value = currentToDoWork.endTime;
  status.value = currentToDoWork.status;
}

// 异步函数，将当前编辑的待办事项导出为JSON格式的数据以进行更新操作
async function exportToDoWork(currentToDoWork: any) {
  // 创建包含当前编辑信息的JSON对象
  const editToDoWork = JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    layer: 1,
    innerId: 0,
    importancePriority: importancePriority.value,
    emergencyPriority: emergencyPriority.value,
    title: inputTodoTitle.value,
    subtitle: inputTodoSubtitle.value,
    description: inputTodoDescription.value,
    createTime: createTime.value,
    startTime: startTime.value,
    deadLine: deadLine.value,
    status: status.value,
    subToDoWorkItemInnerIdList: [],
    pomodoroRecordInnerIdList: [],
  });
  // 调用API编辑待办事项
  const { data } = await ToDoWorkRequest.edit(editToDoWork);
}
//*
</script>

// CSS样式定义，禁用hover效果和一些样式
<style scoped>
.no-hover {
  transition: none !important;
  box-shadow: none !important;
  cursor: auto !important;
}
</style>


