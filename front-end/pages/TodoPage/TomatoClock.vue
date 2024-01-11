<template>
  <div>
    <header>
      <h1>番茄专注</h1>
    </header>
    <aside>

      <button :class="{ whiteButton: !handleModifyTimeClickActive, active: handleModifyTimeClickActive }"
              @click="handleModifyTimeClick" :disabled="focusButtonActive">设立时间
      </button>

      <button
          :class="{ whiteButton: !focusButtonActive, active: focusButtonActive, redButton: focusButtonActive }"
          @mouseover="hoverFocusButton = true"
          @mouseleave="hoverFocusButton = false"
          @click="handleFocus"
      >
        {{ focusButtonActive ? '放弃专注' : '开始专注' }}
      </button>

      <button :class="{ whiteButton: !recordButtonActive, active: recordButtonActive }"
              @click="handleRecord">专注记录
      </button>

    </aside>

    <main>
      <div class="countdown-container">
        <div class="countdown">
          <v-img
              class="align-end text-white pulse-animation"
              height="400"
              width="400"
              src="/static/tomato_2.png"
              opacity="0.9"
              cover
          >
            <v-card-title v-if="focusButtonActive">专注中</v-card-title>
          </v-img>
          <div class="timer">
            {{ countdownTime }}
          </div>
        </div>

        <v-dialog v-model="dialog1" max-width="290">
          <v-card
              elevation="12"
              class="rounded-lg"
              color="#ffffff"
              hover>
            <v-card-title class="headline">
              {{ focusButtonActive ? '番茄专注' : '放弃专注' }}
            </v-card-title>
            <v-card-text>
              {{ focusButtonActive ? '开始啦' : '再坚持一下吧' }}
            </v-card-text>
            <v-card-actions v-if="!modifyTimeClicked && !focusButtonActive">
              <v-btn color="red" @click="confirmGiveUpFocus">确认放弃</v-btn>
              <v-btn color="green" @click="continueCountdown">继续计时</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog2" max-width="490">
          <v-card
              elevation="12"
              class="rounded-lg"
              color="#ffffff"
              hover>
            <v-card-title class=" custom-card-text headline">设立时间</v-card-title>
            <v-card-text class="custom-card2-text">
              番茄专注建议：专注时间25分钟，休息5分钟。
            </v-card-text>
            <v-card-text>
              <v-slider
                  color="#4cafa0"
                  v-model="sliderTimeValue"
                  min="0"
                  max="3600"
                  step="60"
                  label="专注时间"
                  thumb-color="#fff"
                  track-color="#e0f2f1"
                  background-color="#B2DFDB"
                  @change="onSliderChange"
                  class="custom-slider"
              >
                <template #thumb="{ value }">
                  <div class="slider-thumb-label">{{ formatTime(value / 60) }}</div>
                </template>
              </v-slider>
              <!-- 显示已选择的专注时间 -->
              <div class="selected-time text-center">专注时间: {{ formattedSelectedTime }}</div>
            </v-card-text>

            <v-card-text>
              <!-- 添加新的休息时间滑块 -->
              <v-slider
                  color="#4cafa0"
                  v-model="restTimeValue"
                  min="0"
                  max="1200"
                  step="60"
                  label="休息时间"
                  thumb-color="#fff"
                  track-color="#e0f2f1"
                  background-color="#B2DFDB"
                  @change="onRestTimeChange"
                  class="custom-slider"
              >
                <template #thumb="{ value }">
                  <div class="slider-thumb-label">{{ formatTime(value / 60) }}</div>
                </template>
              </v-slider>

              <!-- 显示已选择的休息时间 -->
              <div class="selected-time mt-2 text-center">休息时间: {{ formattedRestTime }}</div>
            </v-card-text>

            <v-card-actions class="justify-end">
              <v-btn color="blue darken-1" @click="saveAndCloseModifyTimeDialog">
                确认并关闭
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog3" max-width="290">
          <v-card
              elevation="12"
              class="rounded-lg"
              color="#EAECCC"
              hover
          >
            <v-card-title class="headline">专注记录</v-card-title>
            <v-card-text>
              <div>
                <v-sheet elevation="2" class="my-4">
                  <v-btn outlined
                         color="#4cafa0"
                         block
                         @click="showTodayPanel">今天</v-btn>
                  <v-btn outlined color="#4cafa0"
                         block
                         @click="showYesterdayPanel">昨天</v-btn>
                  <v-btn outlined color="#4cafa0"
                         block
                         @click="showBeforeYesterdayPanel">前天</v-btn>
                </v-sheet>

                <div v-if="currentDate === 'today'">
                  <v-dialog v-model="todayDialog" width="500" max-width="90%" persistent>
                    <v-card elevation="12"
                            class="rounded-lg">
                      <v-card-title class="d-flex align-center">
                        <span class="text-subtitle-2 mt-4 mb-2 custom-title">今天</span>
                        <v-spacer></v-spacer>
                        <v-icon @click.stop="todayDialog = false"
                                transition="scale-transition">mdi-close</v-icon>
                      </v-card-title>
                      <v-card-text>
                        <div>
                          <div class="text-center d-flex pb-4">
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="todayAll"> All </v-btn>
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="todayNone"> None </v-btn>
                          </div>
                          <v-expansion-panels v-model="todayPanel" multiple>
                            <v-expansion-panel
                                title="Foo"
                                text="od tla."
                                value="foo"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Bar"
                                text="1"
                                value="bar"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Baz"
                                text="1."
                                value="baz"
                            ></v-expansion-panel>
                          </v-expansion-panels>
                        </div>

                      </v-card-text>
                    </v-card>
                  </v-dialog>
                </div>

                <div v-if="currentDate === 'yesterday'">
                  <v-dialog v-model="yesterdayDialog" width="500" max-width="90%" persistent>
                    <v-card elevation="12" class="rounded-lg">
                      <v-card-title class="d-flex align-center">
                        <span class="text-subtitle-2 mt-4 mb-2 custom-title">昨天</span>
                        <v-spacer></v-spacer>
                        <v-icon @click.stop="yesterdayDialog = false">mdi-close</v-icon>
                      </v-card-title>
                      <v-card-text>
                        <div>
                          <div class="text-center d-flex pb-4">
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="yesterdayAll"> All </v-btn>
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="yesterdayNone"> None </v-btn>
                          </div>

                          <v-expansion-panels v-model="yesterdayPanel" multiple>
                            <v-expansion-panel
                                title="Foo"
                                text="od tla."
                                value="foo"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Bar"
                                text="1"
                                value="bar"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Baz"
                                text="1."
                                value="baz"
                            ></v-expansion-panel>
                          </v-expansion-panels>
                        </div>
                      </v-card-text>
                    </v-card>
                  </v-dialog>
                </div>

                <div v-if="currentDate === 'beforeYesterday'">
                  <v-dialog v-model="beforeYesterdayDialog" width="500" max-width="90%" persistent>
                    <v-card elevation="12" class="rounded-lg">
                      <v-card-title class="d-flex align-center">
                        <span class="text-subtitle-2 mt-4 mb-2 custom-title">前天</span>
                        <v-spacer></v-spacer>
                        <v-icon @click.stop="beforeYesterdayDialog = false">mdi-close</v-icon>
                      </v-card-title>
                      <v-card-text>
                        <div>
                          <div class="text-center d-flex pb-4">
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="beforeYesterdayAll"> All </v-btn>
                            <v-btn class="rounded-lg"
                                   color="#C3E2C2"
                                   @click="beforeYesterdayNone"> None </v-btn>
                          </div>
                          <v-expansion-panels v-model="beforeYesterdayPanel" multiple>
                            <v-expansion-panel
                                title="Foo"
                                text="od tla."
                                value="foo"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Bar"
                                text="1"
                                value="bar"
                            ></v-expansion-panel>

                            <v-expansion-panel
                                title="Baz"
                                text="1."
                                value="baz"
                            ></v-expansion-panel>
                          </v-expansion-panels>
                        </div>
                      </v-card-text>
                    </v-card>
                  </v-dialog>
                </div>
              </div>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" @click="dialog3 = false">
                Close
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <v-dialog v-model="dialog4" max-width="390">
          <v-card>
            <v-card-title class="headline">专注成功</v-card-title>
            <v-card-text>
              优秀的人总是自律。<br>
              你的目标已达成，进入下一段旅程吧!
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn color="blue darken-1" @click="closeDialog4AndEndPomodoro">
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
import { ref, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import pomodoroService from '@/composables/PomodoroRequest'

const sliderTimeValue = ref<number>(25*60);
const formattedSelectedTime = computed(() => formatTime(sliderTimeValue.value));
const countdownTime = ref('0:00');
const focusButtonActive = ref(false);
const recordButtonActive = ref(false);
const modifyTimeClicked = ref(false);
const dialog1 = ref(false);
const dialog2 = ref(false);
const dialog3 = ref(false);
const dialog4 = ref(false);
const hoverFocusButton = ref(false);
const todayPanel = ref<string[]>([]);
const yesterdayPanel = ref<string[]>([]);
const beforeYesterdayPanel = ref<string[]>([]);
const currentDate = ref<string | null>(null);
const todayDialog = ref(false);
const yesterdayDialog = ref(false);
const beforeYesterdayDialog = ref(false);
const handleModifyTimeClickActive = ref(true); // 初始化为可点击状态
const restTimeValue = ref<number>(5 * 60); // 默认休息时间为 5 分钟
const formattedRestTime = computed(() => formatTime(restTimeValue.value));
const route = useRoute();

let innerId = ref<string | null>(null);
let countdownTimer: NodeJS.Timeout | undefined;
let remainingTotalSeconds = ref<number | null>(null);
let countdownTimeRef = ref('25:00');
let itemId: Ref<number | undefined>;


onMounted(() => {
  itemId = computed(() => Number(route.query.itemId));
  if(itemId){
    console.log('received',itemId);
    console.log('receivedInnerId:',itemId.value);
  }
});

function saveAndCloseModifyTimeDialog() {
  const workTimeInMinutes = formatTimeToMinutes(sliderTimeValue.value);
  const restTimeInMinutes = formatTimeToMinutes(restTimeValue.value);

  pomodoroService.edit(workTimeInMinutes, restTimeInMinutes);
  dialog2.value = false;
}

function formatTimeToMinutes(seconds: number): number {
  return Math.floor(seconds / 60);
}

function onRestTimeChange(value: number) {
  restTimeValue.value = value;
}


watch(focusButtonActive, (newValue) => {
  if (newValue === true) {
    handleModifyTimeClickActive.value = false; // 开始专注时禁用修改时间按钮
  } else {
    handleModifyTimeClickActive.value = true; // 退出专注时启用修改时间按钮
  }
});

watch(sliderTimeValue, (newValue) => {
  countdownTime.value = formatTime(newValue);
});

function onSliderChange(value: number) {
  sliderTimeValue.value = value;
}

function formatTime(minutes: number): string {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;
  return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}`;
}

function showTodayPanel() {
  currentDate.value = 'today';
  todayDialog.value = true;
}

function showYesterdayPanel() {
  currentDate.value = 'yesterday';
  yesterdayDialog.value = true;
}

function showBeforeYesterdayPanel() {
  currentDate.value = 'beforeYesterday';
  beforeYesterdayDialog.value = true;
}

function todayAll() {
  todayPanel.value = ['foo', 'bar', 'baz'];
}

function todayNone() {
  todayPanel.value = [];
}

function yesterdayAll() {
  yesterdayPanel.value = ['foo', 'bar', 'baz'];
}

function yesterdayNone() {
  yesterdayPanel.value = [];
}

function beforeYesterdayAll() {
  beforeYesterdayPanel.value = ['foo', 'bar', 'baz'];
}

function beforeYesterdayNone() {
  beforeYesterdayPanel.value = [];
}

const handleFocus = () => {
  focusButtonActive.value = !focusButtonActive.value;
  modifyTimeClicked.value = false;

  if (!focusButtonActive.value) {
    // 当处于专注状态时（点击“放弃专注”）
    dialog1.value = true;
  } else if (remainingTotalSeconds.value === null) {
    // 当倒计时尚未启动时（点击“开始专注”）
    // 确保 innerId 已经从路由参数中正确获取到，并转换为数字类型
    const innerIdNumber = Number(itemId.value);
    if (!isNaN(innerIdNumber)) {
      pomodoroService.start(innerIdNumber); // 使用捕获到的 innerId 参数（已转换为 number 类型）
    }
    startCountdown();
  } else {
    focusButtonActive.value = true;
  }
};
async function closeDialog4AndEndPomodoro() {
  dialog4.value = false;
  dialog1.value = false;
  const {data} = await pomodoroService.end();
}

const handleRecord = () => {
  recordButtonActive.value = !recordButtonActive.value;
  dialog3.value = true; // 弹出卡片
};

const handleModifyTimeClick = () => {
  modifyTimeClicked.value = true;
  dialog1.value = false;
  dialog2.value = true; // 弹出卡片
};

const confirmGiveUpFocus = () => {
  focusButtonActive.value = false;
  countdownTime.value = formatTime(sliderTimeValue.value);
  dialog1.value = false;
  if (countdownTimer !== undefined) {
    clearInterval(countdownTimer);
    countdownTimer = undefined;
  }
};

const continueCountdown = () => {
  focusButtonActive.value = true;
  dialog1.value = false;

  if (countdownTimer === undefined && remainingTotalSeconds.value !== null) {
    countdownTimer = setInterval(() => {
      if (remainingTotalSeconds.value !== null) {
        remainingTotalSeconds.value -= 1;
        const remainingMinutes = Math.floor(remainingTotalSeconds.value / 60);
        const remainingSeconds = remainingTotalSeconds.value % 60;

        countdownTimeRef.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds
            .toString()
            .padStart(2, '0')}`;

        if (remainingTotalSeconds.value <= 0) {
          clearInterval(countdownTimer!);
          countdownTimer = undefined;
          remainingTotalSeconds.value = null;
          focusButtonActive.value = false;
          dialog1.value = true;
        }
      }
    }, 1000);
  } else if (countdownTimer !== undefined) {
    // 如果倒计时已经在进行中，则无需重新启动定时器
    focusButtonActive.value = true;
  }
};

const startCountdown = () => {
  const [minutes, seconds] = countdownTime.value.split(':');
  let totalSeconds = parseInt(minutes!) * 60 + parseInt(seconds!);

  countdownTimer = setInterval(() => {
    totalSeconds -= 1;
    const remainingMinutes = Math.floor(totalSeconds / 60);
    const remainingSeconds = totalSeconds % 60;

    countdownTime.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;

    if (totalSeconds <= 0) {
      clearInterval(countdownTimer!);
      countdownTimer = undefined;

      // 倒计时结束后，开始休息时间倒计时
      countdownTime.value = formattedRestTime.value;
      startRestCountdown();
    }
  }, 1000);
};

const startRestCountdown = () => {
  const [minutes, seconds] = countdownTime.value.split(':');
  let totalSeconds = parseInt(minutes!) * 60 + parseInt(seconds!);

  countdownTimer = setInterval(() => {
    totalSeconds -= 1;
    const remainingMinutes = Math.floor(totalSeconds / 60);
    const remainingSeconds = totalSeconds % 60;

    countdownTime.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;

    if (totalSeconds <= 0) {
      clearInterval(countdownTimer!);
      countdownTimer = undefined;
      focusButtonActive.value = false;
      dialog1.value = true;
      dialog4.value = true;
    }
  }, 1000);
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
  background-color: #ffffff;
  color: black;
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
  height: 70vh;
  z-index: 0;

}

.custom-title {
  font-size: 3rem; /* 调整字体大小 */
  font-family: 'Arial', sans-serif; /* 更换字体，这里以 Arial 为例，你可以替换为你喜欢的字体 */
  font-weight: bold; /* 添加加粗效果（可选） */
}

.countdown-container::before{
  content: "";
  position: absolute;
  bottom: 0;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  background-image: url(https://cdn.vuetifyjs.com/images/cards/docks.jpg);
  background-position: center;
  background-size: cover;
  opacity: 0.5; /*可选，设置图片的透明度*/
}

.countdown {
  position: relative;
}

.v-expansion-panels  {
  background-color: #4cafa0;
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
    box-shadow: 0 0 0 0  rgba(0, 0, 0, 0);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
  }
}

.custom-slider {
  margin-top: 16px;
}

.slider-thumb-label {
  position: absolute;
  top: -24px;
  width: fit-content;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  background-color: white;
  color: #4cafa0;
  font-size: 14px;
  z-index: 1;
  pointer-events: none;
}

.custom-card-text {
  color: black; /* 文字颜色 */
  background-color: #C3E2C2; /* 背景颜色 */
}

.custom-card2-text {
  color: black; /* 文字颜色 */
  background-color: #EAECCC; /* 背景颜色 */
}
</style>

