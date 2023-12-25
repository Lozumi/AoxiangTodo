<template>
  <div>
    <header>
      <h1>番茄专注</h1>
    </header>
    <aside>
      <button
          :class="{ whiteButton: !focusButtonActive,active: focusButtonActive, redButton: focusButtonActive  }"
          @click="handleFocus"
      >
        {{ focusButtonActive ? '放弃专注' : '开始专注' }}
      </button>
      <button :class="{  whiteButton: !recordButtonActive,active: recordButtonActive }" @click="handleRecord">专注记录</button>
    </aside>
    <main>
      <div class="countdown-container">
        <div class="countdown">
          <v-img
              class="align-end text-white pulse-animation"
              height="400"
              width="400"
              src="https://cdn.vuetifyjs.com/images/cards/docks.jpg"
              cover
          >
            <v-card-title>专注中</v-card-title>
          </v-img>
          <div class="timer">
            {{ countdownTime }}
          </div>
        </div>
        <v-dialog v-model="dialog1"
                  max-width="290">
          <v-card>
            <v-card-title class="headline">
              {{ focusButtonActive ?
                '番茄专注' :
                '放弃专注' }}
            </v-card-title>
            <v-card-text>
              {{focusButtonActive ?
                '专注时长25分钟，建议休息5分钟' :
                '再坚持一下吧'}}
            </v-card-text>
            <v-card-actions v-if="!modifyTimeClicked && focusButtonActive">
              <v-btn color="blue darken-1"
                     @click="dialog1 = false">默认</v-btn>
              <v-btn color="blue darken-1"
                     @click="handleModifyTimeClick">
                修改时间
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog2" max-width="290">
          <v-card>
            <v-card-title class="headline">
              修改时间
            </v-card-title>
            <v-card-text>
              <v-combobox
                  v-model="selectedTime"
                  :items="timeOptions"
                  label="选择时间"
                  outlined
              ></v-combobox>
            </v-card-text>
            <v-card-actions>
              <v-btn color="blue darken-1" @click="dialog2 = false; dialog1 = false">
                Close
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog3" max-width="290">
          <v-card>
            <v-card-title class="headline">
              专注记录
            </v-card-title>
            <v-card-text>
              <div>
                <div class="text-subtitle-2 mb-2">今天</div>
                <v-expansion-panels>
                  <v-expansion-panel
                      v-for="i in 3"
                      :key="i"
                      title=" "
                      text="  "
                      ></v-expansion-panel>
                </v-expansion-panels>

                <div class="text-subtitle-2 mt-4 mb-2">昨天</div>

                <v-expansion-panels variant="accordion">
                  <v-expansion-panel
                      v-for="i in 3"
                      :key="i"
                      title="  "
                      text="  ."
                  ></v-expansion-panel>
                </v-expansion-panels>

                <div class="text-subtitle-2 mt-4 mb-2">前天</div>

                <v-expansion-panels variant="inset" class="my-4">
                  <v-expansion-panel
                      v-for="i in 3"
                      :key="i"
                      title=" "
                      text=""
                  ></v-expansion-panel>
                </v-expansion-panels>

              </div>
            </v-card-text>
            <v-card-actions>
              <v-btn color="blue darken-1"
                     @click="dialog3 = false; dialog3 = false">
                Close
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const countdownTime = ref('25:00');
const focusButtonActive = ref(false);
const recordButtonActive = ref(false);
const modifyTimeClicked = ref(false);
const dialog1 = ref(false);
const dialog2 = ref(false);
const dialog3 = ref(false);
const selectedTime = ref(null);
const timeOptions = ['10：00', '15：00', '25：00','40：00', '60：00'];

watch(selectedTime, (newValue) => {
  if (newValue) {
    countdownTime.value = newValue; // 更新 countdownTime 的值为选择的时间
  }
});

const handleTimeSelection = () => {
  // 处理时间选择逻辑
  console.log('选择的时间：', selectedTime.value);
  // 这里可以根据选择的时间进行相应的逻辑处理
};

const handleFocus = () => {
  focusButtonActive.value = !focusButtonActive.value;
  dialog1.value = focusButtonActive.value;
  if (!focusButtonActive.value) {
    dialog1.value = true;
  }
  // 处理开始/放弃专注逻辑
};

const handleRecord = () => {
  recordButtonActive.value = !recordButtonActive.value;
  dialog3.value = true; // 弹出卡片
  // 处理专注记录逻辑
};

const handleModifyTimeClick = () => {
  modifyTimeClicked.value = true;
  dialog1.value = false;
  dialog2.value = true; // 弹出卡片
};
</script>

<style scoped>
header {
  text-align: center;
  background-color: #4CAF50; /* 修改背景颜色 */
  color: white; /* 修改文字颜色 */
  height: 10vh;
  padding: 20px;
}

aside{
  text-align: center;
  background-color: #4cafa0; /* 修改背景颜色 */
  height: 3vh;
  padding: 1px;
}

button {
  margin: 5px;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  transition: all 0.3s ease; /* 添加过渡动画效果 */
  font-size: 1.2rem; /* 增加字体大小 */
}

button.active {
  background-color: #00b2ff;
  color: white;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加阴影效果 */
}

button.redButton {
  background-color: #ff3c00;
  color: white;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加阴影效果 */
}
button.whiteButton{
  background-color: #faf7f6;
  color: #090808;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加阴影效果 */
}

.countdown-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  background-color: rgba(215, 209, 165, 0.98);
}

.countdown {
  position: relative;
}

.timer {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 3rem;
  font-family: 'Arial', sans-serif; /* 更改字体样式 */
}

.pulse-animation {
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0.7);
  }
  50% {
    transform: scale(1.1);
    box-shadow: 0 0 0 10px rgba(0, 0, 0, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
  }
}
</style>
