<template>
  <div>
    <v-app>
      <!--应用栏-->
      <v-app-bar :elevation="0"
                 color="#3A8FB7">
        <v-toolbar-title>待办详情</v-toolbar-title>
        <template
            v-slot:append>
          <v-btn
              icon="mdi-chevron-right"></v-btn>
        </template>
      </v-app-bar>

      <v-main>
        <v-container>
          <v-list>
            <!--选择分类文件夹-->
            <v-list-item>
              <v-bottom-sheet v-model="sheet">
                <template v-slot:activator = "{props}">
                  <div>
                    <v-btn v-bind = "props" elevation="0" :color="buttonColor">
                      <v-icon size="24">mdi-folder-star-outline</v-icon>
                      {{buttonText}}
                    </v-btn>
                  </div>
                </template>

                <v-list>
                  <v-list-subheader>文件夹</v-list-subheader>
                  <v-list-item v-for="(tile, index) in tiles"
                               :key="tile.title"
                               @click="changeSheet(tile.title);
                               changeContent(tile.title);
                               openDialog(tile.title);
                               changeColor(tile.color)"
                  >
                    <template v-slot:prepend>
                      <v-avatar :color="tile.color" size = "x-small"></v-avatar>
                    </template>
                    <v-list-content>
                      <v-list-item-title>
                        <!-- 使用条件语句判断是否要对该item实现特殊处理 -->
                        <span v-if="tile.title === specialTitle"
                              style="color: lightskyblue;">{{ tile.title }}</span>
                        <span v-else>{{ tile.title }}</span>
                      </v-list-item-title>
                    </v-list-content>

                    <template v-slot:append>
                      <v-btn elevation="0">
                        <v-icon>mdi-trash-can-outline</v-icon>
                        <v-dialog v-model = "_dialog" activator="parent" width="auto">
                          <v-card>
                            <v-card-text>
                              确定要删除这个文件夹吗？
                            </v-card-text>
                            <v-card-actions>
                              <v-btn color="primary"
                                     block
                                     @click="_dialog = false">关闭</v-btn>
                            </v-card-actions>
                            <v-card-actions>
                              <v-btn color="primary"
                                     block
                                     @click="_dialog = false;
                                           removeFolder(index)">确定</v-btn>
                            </v-card-actions>
                          </v-card>
                        </v-dialog>
                      </v-btn>
                    </template>
                  </v-list-item>
                </v-list>

                <v-dialog v-model = "fileDialog" max-width="600">
                  <v-card>
                    <v-text-field v-model = "inputText" label = "文件夹名称" color = "#3A8FB7"></v-text-field>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="blue-darken-1"
                             variant="text"
                             @click="fileDialog = false"
                      >
                        关闭
                      </v-btn>

                      <v-btn color="blue-darken-1"
                             variant="text"
                             @click="fileDialog = false;
                                 changeContent(inputText);
                                 addFolder(inputText)"
                      >
                        保存
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-bottom-sheet>
            </v-list-item>
            <v-divider></v-divider>  <!--下划线-->

            <!--任务名-->
            <v-list-item>
              <v-text-field
                  label="任务名"
                  color="#3A8FB7"
              ></v-text-field>

              <!--任务描述-->
              <v-text-field
                  v-model="inputValue"
                  label="再来一点描述"
                  variant="underlined"
                  color="#536976"
              ></v-text-field>
            </v-list-item>

            <!--任务时间-->
            <!--开始日期-->
            <v-list-item>
              <v-list-item-title style="font-weight: bold; color: #9796f0">待办开始时间</v-list-item-title>
              <v-btn-toggle
                  v-model="toggle_exclusive"
                  color="#9796f0"
              >
                <v-btn>
                  今天
                </v-btn>

                <v-btn>
                  明天
                </v-btn>
              </v-btn-toggle>

              <v-btn class="ma-2"
                     @click="expand = !expand; togglePanel"
                     style="color: #9796f0"
                     elevation="0"
              >
                <v-icon>mdi-calendar</v-icon>
                <v-btn-title>{{formattedDate}}</v-btn-title>
              </v-btn>

              <v-expand-transition>
                <v-card
                    v-show="expand"
                >
                  <v-date-picker color="#9796f0"
                                 v-model="selectedDate"></v-date-picker>
                </v-card>
              </v-expand-transition>
            </v-list-item>

            <!--待办结束日期-->
            <v-list-item>
              <v-list-item-title style="font-weight: bold;
                                        color: #ee9ca7">待办结束时间</v-list-item-title>

              <v-btn-toggle
                  v-model="toggle_exclusive_over"
                  color="#ee9ca7"
              >
                <v-btn>
                  今天
                </v-btn>

                <v-btn>
                  明天
                </v-btn>
              </v-btn-toggle>

              <v-btn class="ma-2"
                     @click="expand_over = !expand_over;
                         togglePanel_over"
                     style="color: #ee9ca7"
                     elevation="0"
              >
                <v-icon>mdi-calendar</v-icon>
                <v-btn-title>{{formattedDate_over}}</v-btn-title>
              </v-btn>

              <v-expand-transition>
                <v-card
                    v-show="expand_over"
                >
                  <v-date-picker color="#ee9ca7"
                                 v-model="selectedDateOver"></v-date-picker>
                </v-card>
              </v-expand-transition>

            </v-list-item>

            <!--任务重要程度设置-->
            <v-list-item>
              <v-card class="mx-auto">
                <v-card-title style="font-weight: bold;
                                         color: #3A8FB7">
                  任务重要程度
                </v-card-title>

                <v-container>
                  <v-row
                      align="center"
                      justify="start"
                  >
                    <v-col
                        v-for="(selection, i) in selections"
                        :key="selection.text"
                        cols="auto"
                        class="py-1 pe-0"
                    >
                      <v-chip
                          :disabled="loading"
                          closable
                          @click:close="selected.splice(i, 1)"
                      >
                        {{ selection.text }}
                      </v-chip>
                    </v-col>
                  </v-row>
                </v-container>

                <v-divider v-if="!allSelected"></v-divider>

                <v-list>
                  <template v-for="item in categories">
                    <v-list-item
                        v-if="!selected.includes(item)"
                        :key="item.text"
                        :disabled="loading"
                        @click="selected.push(item)"
                    >
                      <template>
                        <v-icon
                            :disabled="loading"
                        ></v-icon>
                      </template>

                      <v-list-item-title v-text="item.text"></v-list-item-title>
                    </v-list-item>
                  </template>
                </v-list>
              </v-card>
            </v-list-item>
            <v-divider></v-divider>  <!--下划线-->

            <!--上传图片-->
            <v-list-item>
              <v-file-input
                  label="上传图片"
                  variant="underlined"
                  color="#3A8FB7"></v-file-input>
            </v-list-item>
            <v-divider></v-divider>  <!--下划线-->

            <v-toolbar color="white">
              <template v-slot:append>
                <v-btn
                    color = "#3A8FB7">
                  <v-icon>mdi-trash-can-outline</v-icon>
                  <v-dialog
                      v-model="dialog"
                      activator="parent"
                      width="auto"
                  >
                    <v-card>
                      <v-card-text>
                        确定要删除这个任务吗？
                      </v-card-text>
                      <v-card-actions>
                        <v-btn color="primary"
                               block
                               @click="dialog = false">关闭</v-btn>
                      </v-card-actions>
                      <v-card-actions>
                        <v-btn color="primary"
                               block
                               @click="dialog = false">确定</v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-btn>

                <v-btn icon="mdi-dots-horizontal" color="#3A8FB7"></v-btn>
              </template>
            </v-toolbar>

          </v-list>
        </v-container>
      </v-main>
    </v-app>
  </div>
</template>

<script>
export default {
  name: "Test1.0",
  data () {
    return {
      text: 'center',
      icon: 'justify',
      toggle_none: null,
      toggle_one: 0,
      toggle_exclusive: 2,
      toggle_exclusive_over: 2,
      toggle_multiple: [0, 1, 2],
      expand: false,
      expand_over: false,
      _dialog: false,
      dialog: false,
      fileDialog: false,
      buttonText: '未分类',
      buttonColor: 'primary',
      panelOpen: false,
      sheet: false,
      inputValue: '',
      inputText: '',
      selectedDate: null,
      selectedDateOver: null,

      tiles: [
        { color: '#3A8FB7', title: '自定义'},
        { color: '#dab8ee', title: '不选择文件夹' },
        { color: '#96DEDA', title: '学习'},
        { color: '#ffc500', title: '工作'},
        { color: '#FFB88C', title: '日常'},
        { color: '#49a09d', title: '旅行'},
      ],
      specialTitle: '自定义',

      items: [
        {
          text: '不重要不紧急',
          color: 'blue',
          index: '1',
        },
        {
          text: '重要但不紧急',
          color: 'blue',
          index: '2',
        },
        {
          text: '不重要但紧急',
          color: 'blue',
          index: '3',
        },
        {
          text: '重要且紧急',
          color: 'blue',
          index: '4',
        },
      ],
      loading: false,
      search: '',
      selected: [],
    }
  },

  computed: {
    allSelected () {
      return this.selected.length === this.items.length
    },
    categories () {
      const search = this.search.toLowerCase()

      if (!search) return this.items

      return this.items.filter(item => {
        const text = item.text.toLowerCase()

        return text.indexOf(search) > -1
      })
    },
    selections () {
      const selections = []

      for (const selection of this.selected) {
        selections.push(selection)
      }

      return selections
    },

    formattedDate () {
      // 格式化日期，你可以根据需要选择合适的日期格式
      if (this.selectedDate) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return this.selectedDate.toLocaleDateString('en-US', options);
      } else {
        return '自定义';
      }
    },

    formattedDate_over () {
      // 格式化日期，你可以根据需要选择合适的日期格式
      if (this.selectedDateOver) {
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return this.selectedDateOver.toLocaleDateString('en-US', options);
      } else {
        return '自定义';
      }
    },

  },

  watch: {
    selected () {
      this.search = ''
    },
  },

  methods: {
    buttonClicked(buttonName) {
      // 处理按钮点击事件
      console.log(`${buttonName} clicked`);
    },

    next () {
      this.loading = true

      setTimeout(() => {
        this.search = ''
        this.selected = []
        this.loading = false
      }, 2000)
    },

    togglePanel() {
      this.panelOpen = !this.panelOpen;
    },

    togglePanel_over() {
      this.panelOpen = !this.panelOpen;
    },

    changeContent(parameter) {
      if(parameter == '不选择文件夹'){
        this.buttonText = '未分类';
      }
      else{
        this.buttonText = parameter;
      }
    },

    changeColor(parameter) {
      if(parameter == '不选择文件夹'){
        this.buttonColor = 'primary';
      }
      else{
        this.buttonColor = parameter;
      }
    },

    openDialog(parameter) {
      if(parameter === '自定义'){
        this.fileDialog = true;
      }
      else{
        this.fileDialog = false;
      }
    },

    changeSheet(parameter) {
      if(parameter == '自定义'){
        this.sheet = true;
      }
      else{
        this.sheet = false;
      }
    },

    addFolder(parameter) {
      this.tiles.push({
        color: this.getRandomColor(),
        title: parameter,
        index: this.tiles.length + 1,
      });
    },

    getRandomColor() {
      // 生成随机颜色的函数
      return `#${Math.floor(Math.random()*16777215).toString(16)}`;
    },

    removeFolder(parameter) {
      this.tiles.splice(parameter,1);
    }
  }
}
</script>

<style scoped>

.no-hover {
  transition: none !important;
  box-shadow: none !important;
  cursor: auto !important;
}

</style>