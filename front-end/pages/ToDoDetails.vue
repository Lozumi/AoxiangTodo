<template>
  <div>
    <v-app>
      <!--应用栏-->
      <v-app-bar :elevation="0"
                 color="#3A8FB7">
        <v-toolbar-title>待办清单</v-toolbar-title>
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
              <v-list-icon>
                <v-icon
                    size = "36"
                    color="#3A8FB7">
                  mdi-folder-star-outline
                </v-icon>
              </v-list-icon>
              <v-list-item-content
                  style="font-weight: bold;
                      font-size: 28px;
                      color: #3A8FB7">
                文件夹
              </v-list-item-content>
              <v-select
                  clearable
                  label="选择"
                  :items="[]"
                  variant="solo-filled"
              ></v-select>
            </v-list-item>
            <v-divider></v-divider>  <!--下划线-->
            <!--任务名-->
            <v-list-item>
              <v-text-field
                  label="任务名"
                  variant="underlined"
                  color="#3A8FB7"></v-text-field>
              <!--任务描述-->
              <v-text-field
                  v-model="inputValue"
                  label="再来一点描述"
                  variant="underlined"></v-text-field>
            </v-list-item>

            <!--任务时间-->
            <v-list-item>
              <v-btn-toggle
                  v-model="toggle_exclusive"
                  color="#3A8FB7">
                <v-btn>
                  今天
                </v-btn>

                <v-btn>
                  明天
                </v-btn>
              </v-btn-toggle>

              <v-btn class="ma-2"
                     color="#3A8FB7"
                     @click="expand = !expand">
                <v-icon>mdi-calendar</v-icon>
                <v-btn-title>自定义</v-btn-title>
              </v-btn>

              <v-expand-transition>
                <v-card
                    v-show="expand"
                >
                  <v-date-picker
                      color="#3A8FB7"></v-date-picker>
                </v-card>
              </v-expand-transition>

            </v-list-item>

            <!--任务重要程度设置-->
            <v-list-item>
              <v-card
                  class="mx-auto"
              >
                <v-card-title style="font-weight: bold; color: #3A8FB7">任务重要程度</v-card-title>
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
                        <v-btn color="primary" block @click="dialog = false">关闭</v-btn>
                      </v-card-actions>
                      <v-card-actions>
                        <v-btn color="primary" block @click="dialog = false">确定</v-btn>
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
      toggle_multiple: [0, 1, 2],
      expand: false,
      dialog: false,

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