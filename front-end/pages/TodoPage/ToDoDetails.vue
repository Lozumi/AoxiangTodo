<template>
  <div>
    <v-app>
      <!--应用栏-->
      <v-app-bar :elevation="0" color="#3A8FB7">
        <v-toolbar-title>待办详情</v-toolbar-title>
        <v-btn icon="mdi-chevron-right" @click="backToToDoList"></v-btn>
      </v-app-bar>

      <v-main>
        <v-container>

          <!--选择分类文件夹-->
          <v-list-item>
            <v-bottom-sheet v-model="sheet">
              <template v-slot:activator="{ props }">
                <div>
                  <v-btn v-bind="props" elevation="0" :color="buttonColor">
                    <v-icon size="24">mdi-folder-star-outline</v-icon>
                    {{ buttonText }}
                  </v-btn>
                </div>
              </template>

              <v-list>
                <v-list-subheader>文件夹</v-list-subheader>
                <v-list-item v-for="(tile, index) in tiles" :key="tile.title" @click="changeSheet(tile.title);
                changeContent(tile.title);
                openDialog(tile.title);
                changeColor(tile.color)">
                  <template v-slot:prepend>
                    <v-avatar :color="tile.color" size="x-small"></v-avatar>
                  </template>

                  <v-list-item-title>
                    <!-- 使用条件语句判断是否要对该item实现特殊处理 -->
                    <span v-if="tile.title === specialTitle" style="color: lightskyblue;">{{ tile.title }}</span>
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
                            <v-btn color="primary" block @click="_dialog = false">关闭
                            </v-btn>
                          </v-card-actions>
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

              <v-dialog v-model="fileDialog" max-width="600">
                <v-card>
                  <v-text-field v-model="inputText" label="文件夹名称" color="#3A8FB7"></v-text-field>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue-darken-1" variant="text" @click="fileDialog = false">
                      关闭
                    </v-btn>

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
            <v-btn-toggle v-model="toggleStart" color="#9796f0" variant="outlined">
              <v-btn>今天</v-btn>
              <v-btn>明天</v-btn>
              <v-btn @click="expand = !expand; togglePanel" elevation="0">
                <v-icon>mdi-calendar</v-icon>
                {{ formattedDate }}
              </v-btn>
            </v-btn-toggle>

            <v-expand-transition>
              <v-card v-show=" expand ">
                <v-date-picker color="#9796f0" v-model=" startDate " @click="expand = !expand"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--待办结束日期-->
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
                <v-date-picker color="#ee9ca7" v-model=" endDate " @click="expand_over = !expand_over"></v-date-picker>
              </v-card>
            </v-expand-transition>

            <!--任务重要程度设置-->
            <p style="font-weight: bold;
                                         color: #3A8FB7">
              任务重要程度
            </p>

            <v-expansion-panels>
              <v-expansion-panel :title=" importancePriorityTitle " :color=" importancePriorityColor ">
                <v-expansion-panel-text>
                  <v-list>
                    <v-list-item v-for="     item      in      items     " :key=" item.text "
                                 @click="changeTitle(item.text); changeExColor(item.color)"
                                 :style=" { 'background-color': item.color } ">
                      <v-list-item-title>{{ item.text }}</v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-expansion-panel-text>
              </v-expansion-panel>
            </v-expansion-panels>

            <!--任务重要程度设置-->
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
const ToDoWorkItems =ref ([] as ToDoWorkItem[]);

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


let itemId = ref<number|null>(null);

let importancePriority = ref<number>(0);

let emergencyPriority = ref<number>(0);

let title = ref<string>('');

let subtitle = ref<string>('');

let description = ref<string>('');


let deadLine = ref<string>('');

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


const start_today = ref<string>('');

const start_tomorrow = ref<string>('');

const start_date = ref<string>('');

const end_today = ref<string>('');

const end_tomorrow = ref<string>('');

const end_date = ref<string>('');

const inputImportanceId = ref<number>(0);

const inputEmergencyId = ref<number>(0);

const route = useRoute();

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

function isEqual() {
  return selected.value.length == items.length;
}

const categories = computed(() => getCategories());

function getCategories() {
  const search1 = search.value.toLocaleLowerCase();

  if (!search1) {
    return items;
  }

  return items.filter(item => {
    const text = item.text.toLocaleLowerCase();

    return text.indexOf(search1) > -1;
  })
}

const selections = computed(() => getSelections())

function getSelections() {
  const selections1 = [];

  for (const selection1 of selected.value) {
    selections1.push(selection1);
  }

  return selections1;
}

const formattedDate = computed(() => formatStartDate());

const formattedDate_over = computed(() => formatEndDate());

function formatStartDate() {
  if (startDate.value) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return startDate.value.toLocaleString('en-US', options);
  } else {
    let undef = '自定义';
    return undef;
  }
}

function formatEndDate() {
  if (endDate.value) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return endDate.value.toLocaleString('en-US', options);
  } else {
    let undef = '自定义';
    return undef;
  }
}

watch(selected, () => {
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

function togglePanel() {
  panelOpen.value = !panelOpen.value;
}

function togglePanel_over() {
  panelOpen.value = !panelOpen.value;
}

function changeContent(title: string) {
  if (title == '不选择文件夹') {
    buttonText.value = '未分类';
  } else {
    buttonText.value = title;
  }
}

function changeColor(color: string) {
  if (color == '不选择文件夹') {
    buttonColor.value = 'primary';
  } else {
    buttonColor.value = color;
  }
}

function openDialog(title: string) {
  if (title == '自定义') {
    fileDialog.value = true;
  } else {
    fileDialog.value = false;
  }
}

function changeSheet(title: string) {
  if (title === '自定义') {
    sheet.value = true;
  } else {
    sheet.value = false;
  }
}

function addFolder(title: string) {
  tiles.value.push({
    color: getRandomColor(),
    title: title
  })
}

function getRandomColor() {
  // 生成随机颜色的函数
  return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
}

function removeFolder(index: number) {
  tiles.value.splice(index, 1);
}

function changeTitle(text: string) {
  importancePriorityTitle.value = text;
}

function changeTitle_1(text: string) {
  emergencyPriorityTitle.value = text;
}

function changeExColor(color: string) {
  importancePriorityColor.value = color;
}

function changeExColor_1(color: string) {
  emergencyPriorityColor.value = color;
}

// 当前实现是 选项修改以后就保存，点击返回图标就调回原来的列表
async function backToToDoList() {
  await navigateTo("/TodoPage/Todo");
}

onMounted(() => {
  itemId = computed(() => Number(route.query.itemId));
  if (itemId.value != null) {
    //receviedInnerId传输成功了17:39
    console.log('receivedInnerId:', itemId.value);
    QueryToDoWork(itemId.value);
  } else {
    console.error('error:真蚌埠住了');
  }
});

async function QueryToDoWork(innerId: number) {
  let currentToDoWork = null;
  currentToDoWork = await ToDoWorkRequest.query(innerId).data.value;

  if (typeof currentToDoWork === 'string') {
    console.log('data.value:',currentToDoWork);

    try {
      const response = JSON.parse(currentToDoWork);


      // 确保 content 是一个字符串化的数组，并将其转换为对象数组
      if (typeof response.content === 'string') {
        const jsonContentArrayString = response.content;
        const responseContentArray = JSON.parse(jsonContentArrayString) as ToDoWorkItem[];

        // 在数组中查找与 innerId 匹配的待办事项对象
        const targetItem = responseContentArray.find(item => item.innerId === innerId);

        if (targetItem) {
          importToDoWork(targetItem);
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

function importToDoWork(currentToDoWork: { [key: string]: any }) {
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


