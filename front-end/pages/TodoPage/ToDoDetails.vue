<template>
  <div>
    <v-app>
      <!--应用栏-->
      <!-- 设置v-app-bar组件，elevation属性值为0，表示无阴影效果，color属性值为#3A8FB7，设置整体背景色 -->
      <v-app-bar :elevation="0" color="#3A8FB7">
        <!-- 在v-app-bar中添加一个v-toolbar-title组件，显示内容为“待办详情” -->
        <v-toolbar-title>待办详情</v-toolbar-title>
        <!-- 添加一个v-btn组件，icon属性值为mdi-chevron-right，表示右侧向箭头图标，点击该按钮时触发backToToDoList方法 -->
        <v-btn icon="mdi-chevron-right" @click="backToToDoList"></v-btn>
      </v-app-bar>

      <v-main>
        <v-container>

          <!--选择分类文件夹-->
          <v-list-item>
            <!-- 使用v-bottom-sheet组件创建一个底部弹出面板，其显示与否的状态与变量sheet（通过v-model绑定）同步 -->
            <v-bottom-sheet v-model="sheet">
              <!-- 使用模板slot插槽，并指定名称为"activator"，它接收一个对象参数{ props }，该对象包含了激活器组件所需的props -->
              <template v-slot:activator="{ props }">
                <!-- 创建一个div容器包裹按钮 -->
                <div>
                  <!-- 使用v-btn组件创建一个按钮，将从activator slot传入的props属性绑定到此按钮上 -->
                  <v-btn v-bind="props" elevation="0" :color="buttonColor">
                    <v-icon size="24">mdi-folder-star-outline</v-icon>
                    <!-- 显示文本内容，其值由Vue实例中的buttonText变量决定 -->
                    {{ buttonText }}
                  </v-btn>
                </div>
              </template>

              <v-list>
                <v-list-subheader>文件夹</v-list-subheader>
                <!-- 使用v-for指令遍历tiles数组，对于数组中的每个tile元素及其索引index生成一个v-list-item组件 -->
                <!-- 为每个列表项绑定点击事件，当点击时依次执行以下四个方法，并将tile.title作为参数传递 -->
                <v-list-item v-for="(tile, index) in tiles" :key="tile.title" @click="changeSheet(tile.title);
                changeContent(tile.title);
                openDialog(tile.title);
                changeColor(tile.color)">
                  <template v-slot:prepend>
                    <v-avatar :color="tile.color" size="x-small"></v-avatar>
                  </template>

                  <v-list-item-title>
                    <!-- 使用条件语句判断是否要对该item实现特殊处理 -->
                    <!-- 如果当前tile对象的title属性与预设的特殊标题（specialTitle）相匹配 -->
                    <span v-if="tile.title === specialTitle" style="color: lightskyblue;">{{ tile.title }}</span>
                    <!-- 否则按照默认样式显示当前tile的title -->
                    <span v-else>{{ tile.title }}</span>
                  </v-list-item-title>

                  <template v-slot:append>
                    <v-btn elevation="0">
                      <v-icon>mdi-trash-can-outline</v-icon>
                      <v-dialog v-model="_dialog" activator="parent" width="auto">
                        <v-card>
                          <v-card-text>
                            确定要删除这个文件夹吗？
                          </v-card-text>
                          <v-card-actions>
                            <!-- 第一组按钮：关闭对话框，点击后将_v-dialog设为false -->
                            <v-btn color="primary" block @click="_dialog = false">关闭
                            </v-btn>
                          </v-card-actions>
                          <v-card-actions>
                            <!-- 另一组按钮：关闭对话框并执行removeFolder方法，传入当前tile的title作为参数 -->
                            <v-btn color="primary" block @click="_dialog = false; removeFolder(tile.title)">确定
                            </v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-dialog>
                    </v-btn>
                  </template>
                </v-list-item>
              </v-list>

              <!-- 创建一个v-dialog对话框组件，其显示隐藏状态由Vue实例中的fileDialog变量控制 -->
              <v-dialog v-model="fileDialog" max-width="600">
                <v-card>
                  <!-- 添加一个v-text-field输入框，用于用户输入文件夹名称，输入内容与inputText变量双向绑定 -->
                  <v-text-field v-model="inputText" label="文件夹名称" color="#3A8FB7"></v-text-field>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <!-- 第一个按钮：关闭对话框，点击后将fileDialog设为false -->
                    <v-btn color="blue-darken-1" variant="text" @click="fileDialog = false">
                      关闭
                    </v-btn>

                    <!-- 第二个按钮：关闭对话框，并执行changeContent和addFolder两个方法，将inputText的当前值作为参数传递 -->
                    <!-- changeContent方法可用于更新创建的文件夹 -->
                    <!-- addFolder方法用于根据用户输入的名称创建新的文件夹 -->
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
            <!-- 使用Vuetify的v-btn-toggle组件创建一个按钮组，通过v-model指令绑定到toggleStart变量，用于控制当前选中的按钮项-->
            <v-btn-toggle v-model="toggleStart" color="#9796f0" variant="outlined">
              <!-- 提供两个预设选项，分别对应今天和明天的待办开始时间 -->
              <v-btn>今天</v-btn>
              <v-btn>明天</v-btn>
              <v-btn @click="expand = !expand; togglePanel" elevation="0">
                <v-icon>mdi-calendar</v-icon>
                {{ formattedDate }}
              </v-btn>
            </v-btn-toggle>

            <v-expand-transition>
              <!-- 当expand为true时，v-card将会显示 -->
              <v-card v-show=" expand ">
                <!-- 点击日期选择器也会关闭面板，即将expand设置为false -->
                <v-date-picker color="#9796f0" v-model=" startDate " @click="expand = !expand"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--待办结束日期-->
            <!--代码功能同上-->
            <p style="font-weight: bold;color: #ee9ca7">待办结束时间</p>

            <v-btn-toggle v-model=" toggleEnd " color="#ee9ca7" variant="outlined">
              <v-btn>今天</v-btn>

              <v-btn>明天</v-btn>

              <v-btn @click=" expand_over = !expand_over; togglePanel_over " elevation="0">
                <v-icon>mdi-calendar</v-icon>
                {{ formattedDate_over }}
              </v-btn>

            </v-btn-toggle>

            <v-expand-transition>
              <v-card v-show=" expand_over ">
                <!-- v-model指令将该日期选择器的选择值绑定到Vue实例的endDate数据属性 -->
                <!-- 当点击日期选择器时，会触发事件处理器，使得expand_over状态取反，实现展开/收起面板的功能 -->
                <v-date-picker color="#ee9ca7" v-model=" endDate " @click="expand_over = !expand_over"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--任务重要程度设置-->
            <p style="font-weight: bold;
                                         color: #3A8FB7">
              任务重要程度
            </p>

            <!-- 使用Vuetify框架中的v-expansion-panels组件创建一个可展开/折叠面板组 -->
            <v-expansion-panels>
              <!-- 定义一个可展开的面板项，其标题由Vue实例的importancePriorityTitle数据绑定决定，并根据importancePriorityColor属性设置背景色 -->
              <v-expansion-panel :title=" importancePriorityTitle " :color=" importancePriorityColor ">
                <!-- 在展开的面板内容区域中嵌入一个v-expansion-panel-text组件以显示列表信息 -->
                <v-expansion-panel-text>
                  <v-list>
                    <!-- 遍历items数组（假设该数组包含多个对象，每个对象有text和color属性） -->
                    <v-list-item v-for="     item      in      items     " :key=" item.text "
                                 @click="changeTitle(item.text); changeExColor(item.color)"
                                 :style=" { 'background-color': item.color } ">
                      <v-list-item-title>{{ item.text }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-expansion-panel-text>
              </v-expansion-panel>
            </v-expansion-panels>

            <!--任务重紧急程度设置-->
            <p style="font-weight: bold;color: #3A8FB7">任务紧急程度</p>

            <v-expansion-panels>
              <v-expansion-panel :title=" emergencyPriorityTitle " :color=" emergencyPriorityColor ">
                <v-expansion-panel-text>
                  <v-list>
                    <v-list-item v-for="     item      in      items_1     " :key=" item.text "
                                 @click="changeTitle_1(item.text); changeExColor_1(item.color)"
                                 :style=" { 'background-color': item.color } ">
                      <v-list-item-title>{{ item.text }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-expansion-panel-text>
              </v-expansion-panel>
            </v-expansion-panels>

            <v-toolbar color="white">
              <template v-slot:append>
                <v-btn color="#3A8FB7">
                  <v-icon>mdi-trash-can-outline</v-icon>
                  <v-dialog v-model=" dialog " activator="parent" width="auto">
                    <v-card>
                      <v-card-text>
                        确定要删除这个任务吗？
                      </v-card-text>
                      <v-card-actions>
                        <v-btn color="primary" block @click="dialog = false">关闭
                        </v-btn>
                      </v-card-actions>
                      <v-card-actions>
                        <!-- 修改此处为实际执行删除操作的回调函数 -->
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

import { computed, ref, watch } from 'vue';
import ToDoWorkRequest from "~/composables/ToDoWorkRequest";
import { useRoute } from 'vue-router';

//任务标题相关
let inputTodoTitle = ref<string>('');
let inputTodoSubtitle = ref<string>('');
let inputTodoDescription = ref<string>('');

//待办区间相关
let toggleStart = ref<number>(2);
let toggleEnd = ref<number>(2);
let createTime = ref<number>();
let startTime = ref<number>();
let endTime = ref<number>();
let status = ref<string>();

//任务程度相关
let importancePriorityTitle = ref<string>('未设置');//任务重要程度
let importancePriorityColor = ref<string>('gray');
let emergencyPriorityTitle = ref<string>('未设置');//任务紧急程度
let emergencyPriorityColor = ref<string>('gray');

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


let itemId = ref<number>;

let importancePriority = ref<number>(0);

let emergencyPriority = ref<number>(0);

let title = ref<string>('');

let subtitle = ref<string>('');

let description = ref<string>('');


let deadLine = ref<string>('');


const start_today = ref<string>('');

const start_tomorrow = ref<string>('');

const start_date = ref<string>('');

const end_today = ref<string>('');

const end_tomorrow = ref<string>('');

const end_date = ref<string>('');

const inputImportanceId = ref<number>(0);

const inputEmergencyId = ref<number>(0);

const route = useRoute();

//任务分类文件夹列表
const tiles = ref<{ color: string; title: string; }[]>([
  { color: '#3A8FB7', title: '自定义' },
  { color: '#dab8ee', title: '不选择文件夹' },
  { color: '#96DEDA', title: '学习' },
  { color: '#ffc500', title: '工作' },
  { color: '#FFB88C', title: '日常' },
  { color: '#49a09d', title: '旅行' },
]);

const specialTitle = ref<string>('自定义');

const selected = ref<string[]>([]);

const loading = ref<boolean>(false);

const search = ref<string>('');

//任务重要程度和紧急程度列表
const items = Object.freeze([
  {
    text: '不重要',
    color: '#C3E2C2',
    index: '1',
  },
  {
    text: '较不重要',
    color: '#EAECCC',
    index: '2',
  },
  {
    text: '比较重要',
    color: '#DBCC95',
    index: '3',
  },
  {
    text: '很重要',
    color: '#CD8D7A',
    index: '4',
  },
]);

const items_1 = Object.freeze([
  {
    text: '不紧急',
    color: '#C3E2C2',
    index: '1',
  },
  {
    text: '较不紧急',
    color: '#EAECCC',
    index: '2',
  },
  {
    text: '比较紧急',
    color: '#DBCC95',
    index: '3',
  },
  {
    text: '很紧急',
    color: '#CD8D7A',
    index: '4',
  },
]);
const allSelected = computed(() => isEqual());

/**
 * isEqual
 * 用于判断选中项的数量是否与原始项目列表数量相等
 * */
function isEqual() {
  return selected.value.length == items.length;
}

const categories = computed(() => getCategories());

/**
 * getCategories
 * 根据用户输入的搜索关键字过滤并返回符合条件的类别列表
 * */
function getCategories() {
  //获取并转换用户在搜索框中输入的关键词为小写形式
  const search1 = search.value.toLocaleLowerCase();
  // 如果用户没有输入任何搜索内容（即search1为空），则直接返回原始类别列表
  if (!search1) {
    return items;
  }
// 对原始类别列表items应用过滤器方法，筛选出符合搜索条件的类别项
  return items.filter(item => {
    const text = item.text.toLocaleLowerCase();

    // 使用indexOf方法检查text是否包含搜索关键词search1，若包含则返回索引大于-1，否则返回false
    // 返回true的项将被保留在过滤后的结果数组中
    return text.indexOf(search1) > -1;
  })
}

const selections = computed(() => getSelections())
/**
 *getSelections
 *用于获取并返回当前选中的项目列表
 * */
function getSelections() {
  // 初始化一个空数组，用于存储已选择项目的副本
  const selections1 = [];

  // 遍历存储已选择项目的引用（selected.value）中的每一项
  for (const selection1 of selected.value) {
    // 将当前遍历到的选择项添加至selections1数组中
    selections1.push(selection1);
  }

  // 返回包含所有已选择项目的新数组
  return selections1;
}

// 定义一个计算属性（computed property）formattedDate，它会基于formatStartDate函数的返回值进行计算并实时更新
const formattedDate = computed(() => formatStartDate());

const formattedDate_over = computed(() => formatEndDate());

/**
 * formatStartDate
 * 格式化开始日期
 * */
function formatStartDate() {
  // 检查startDate值是否存在（startDate.value是一个ref或state变量，存储开始日期）
  if (startDate.value) {
    // 如果存在开始日期，则定义一个日期格式选项对象，设置年份为全数字形式、月份为简写和日期为数字形式
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    // 使用toLocaleString方法将日期格式化为指定的语言环境和格式，并返回结果字符串
    return startDate.value.toLocaleString('en-US', options);
  } else {
    // 如果开始日期未定义或者为空，则返回一个表示“自定义”的字符串
    let undef = '自定义';
    return undef;
  }
}

/**
 *formatEndDate
 *用于格式化结束日期
 */
function formatEndDate() {
  if (endDate.value) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return endDate.value.toLocaleString('en-US', options);
  } else {
    let undef = '自定义';
    return undef;
  }
}

// 定义一个watcher，用于监听selected值的变化
watch(selected, () => {
  // 当selected值发生变化时，将搜索框（search）的值清空为''
  search.value = '';
});

watch([
  inputTodoTitle,
  inputTodoDescription,
  toggleStart,
  startDate,
  toggleEnd,
  endDate,
  // sheet,
  // inputText,

  importancePriorityTitle,
  emergencyPriorityTitle
], (...params) => {
  console.log('request', params)

  // current work
  exportToDoWork({})
});


function buttonClicked(buttonName: string) {
  console.log(`${buttonName} clicked`);
}

function next() {
  loading.value = true;

  setTimeout(() => {
    search.value = '';
    selected.value = [];
    loading.value = false;
  }, 2000)
}

//切换面板状态
function togglePanel() {
  panelOpen.value = !panelOpen.value;
}

function togglePanel_over() {
  panelOpen.value = !panelOpen.value;
}

/**
 * 根据传入的标题字符串更改buttonText的值
 * @param title
 */
function changeContent(title: string) {
  if (title == '不选择文件夹') {
    buttonText.value = '未分类';
  } else {
    buttonText.value = title;
  }
}

/**
 * 用于根据传入的颜色字符串更改buttonColor的值
 * @param color
 */
function changeColor(color: string) {
  if (color == '不选择文件夹') {
    buttonColor.value = 'primary';
  } else {
    buttonColor.value = color;
  }
}

/**
 * 用于根据传入的标题字符串控制fileDialog（对话框）是否打开
 * @param title
 */
function openDialog(title: string) {
  if (title == '自定义') {
    fileDialog.value = true;
  } else {
    fileDialog.value = false;
  }
}

/**
 * 用于根据传入的标题字符串控制底部弹出面板（sheet）的显示与隐藏状态
 * @param title
 */
function changeSheet(title: string) {
  if (title === '自定义') {
    sheet.value = true;
  } else {
    sheet.value = false;
  }
}

/**
 * 根据传入的标题字符串创建并添加一个新的文件夹到tiles数组中
 * @param title
 */
function addFolder(title: string) {
  tiles.value.push({
    color: getRandomColor(),
    title: title
  })
}

/**
 * 生成一个随机的十六进制颜色码
 */
function getRandomColor() {
  // 生成随机颜色的函数
  return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
}

/**
 * 从响应式数组tiles.value中删除对应于给定index位置的元素
 * @param index
 */
function removeFolder(index: number) {
  tiles.value.splice(index, 1);
}

/**
 * 当点击某个列表项时，会触发此方法，将当前选中项的文字更新至importancePriorityTitle变量
 * @param text
 */
function changeTitle(text: string) {
  importancePriorityTitle.value = text;
}

/**
 * 当点击某个列表项时，会触发此方法，将当前选中项的文字更新至emergencyPriorityTitle变量
 * @param text
 */
function changeTitle_1(text: string) {
  emergencyPriorityTitle.value = text;
}

/**
 * 在点击列表项时触发，用于更改importancePriorityColor变量，进而改变整个面板的颜色样式
 * @param color
 */
function changeExColor(color: string) {
  importancePriorityColor.value = color;
}

/**
 * 在点击列表项时触发，用于更改emergencyPriorityColor变量，进而改变整个面板的颜色样式
 * @param color
 */
function changeExColor_1(color: string) {
  emergencyPriorityColor.value = color;
}

/**
 * 当前实现是 选项修改以后就保存，点击返回图标就调回原来的列表
 */
async function backToToDoList() {
  await navigateTo("/TodoPage/Todo");
}

/**
 * 组件挂载完成后执行一个回调函数
 */
onMounted(() => {
  // 使用Vue的计算属性computed来获取并转换路由查询参数（query）中的itemId为数字类型
  itemId.value = computed(() => Number(route.query.itemId)).value;
  // 检查是否成功获取到itemId，即itemId值是否存在且不为NaN
  if (itemId) {
    // console.log('received:', itemId);
    // 调用QueryToDoWork函数并将解析后的itemId作为参数传递
    console.log('receivedInnerId:', itemId.value);
    QueryToDoWork(itemId.value);
  } else {
    // 如果无法获取到有效的itemId，则输出错误信息至控制台
    console.error('error:真蚌埠住了');
  }
});

/**
 * 定义一个异步函数QueryToDoWork，用于根据传入的内部ID（innerId）查询对应的工作项数据
 * @param innerId
 * @constructor
 */
async function QueryToDoWork(innerId: number) {
  let currentToDoWork = await ToDoWorkRequest.query(innerId).data.value;
  currentToDoWork = await ToDoWorkRequest.query(innerId).data.value;
  console.log(currentToDoWork);
  let currentToDoWorkObject = JSON.parse(currentToDoWork as string).content;
  console.log(currentToDoWorkObject);
  let currentToDoWorkObjectContent = JSON.parse(currentToDoWorkObject);
  console.log(currentToDoWorkObjectContent)
  importToDoWork(currentToDoWorkObjectContent);
}

/**
 * 定义一个名为importToDoWork的函数，该函数用于将传入的工作项数据对象（currentToDoWork）中的属性值导入到各个响应式状态变量中
 * @param currentToDoWork
 */
function importToDoWork(currentToDoWork: any) {
  inputTodoTitle.value = currentToDoWork.title;
  inputTodoSubtitle.value = currentToDoWork.subtitle;
  inputTodoDescription.value = currentToDoWork.description;
  startTime.value = currentToDoWork.startTime;
  deadLine.value = currentToDoWork.deadLine;
  importancePriority.value = currentToDoWork.importancePriority;
  emergencyPriority.value = currentToDoWork.emergencyPriority;
  createTime.value = currentToDoWork.createTime;
  endTime.value = currentToDoWork.endTime;
  status.value = currentToDoWork.status;
}

/**
 * 定义一个异步函数exportToDoWork，该函数负责将当前待办事项（currentToDoWork）对象转换为JSON格式字符串，并发送到服务器进行编辑操作
 * @param currentToDoWork
 */
async function exportToDoWork(currentToDoWork: any) {
  const editToDoWork = JSON.stringify({
    '@class': 'shared.ToDoWorkItem',
    layer: 1,
    innerId: 0,
    importancePriority: importancePriority,
    emergencyPriority: emergencyPriority,
    title: inputTodoTitle,
    subtitle: inputTodoSubtitle,
    description: inputTodoDescription,
    createTime: createTime,
    startTime: startTime,
    deadLine: deadLine,
    status: status,
    subToDoWorkItemInnerIdList: [],
    pomodoroRecordInnerIdList: [],
  });
  const { data } = await ToDoWorkRequest.edit(editToDoWork);
}
//*
</script>

<style scoped>
.no-hover {
  transition: none !important;
  box-shadow: none !important;
  cursor: auto !important;
}
</style>


