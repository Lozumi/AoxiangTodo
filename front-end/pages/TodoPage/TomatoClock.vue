<template>
  <div>
    <!-- 页面头部 -->
    <header>
      <h1>番茄专注</h1>
    </header>

    <!-- 边栏，包含两个功能按钮 -->
    <aside>
      <!-- 设立时间按钮 -->
      <button
          :class="{ whiteButton: !handleModifyTimeClickActive, active: handleModifyTimeClickActive }"
          @click="handleModifyTimeClick"
          :disabled="focusButtonActive">设立时间
      </button>

      <!-- 开始/放弃专注按钮 -->
      <button
          :class="{
              whiteButton: !focusButtonActive,
              active: focusButtonActive,
              redButton: focusButtonActive
            }"
          @mouseover="hoverFocusButton = true"
          @mouseleave="hoverFocusButton = false"
          @click="handleFocus"
      >
        {{ focusButtonActive ? '放弃专注' : '开始专注' }}
      </button>
    </aside>

    <!-- 主要内容区域 -->
    <main>
      <!-- 倒计时容器 -->
      <div class="countdown-container">
        <!-- 倒计时及番茄图片 -->
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
          <!-- 显示倒计时时间 -->
          <div class="timer">{{ countdownTime }}</div>
        </div>

        <!-- 弹出对话框1：确认放弃或继续计时 -->
        <v-dialog v-model="dialog1" max-width="290">
          <v-card
              elevation="12"
              class="rounded-lg"
              color="#ffffff"
              hover>
            <!-- 对话框标题 -->
            <v-card-title class="headline">
              {{ focusButtonActive ? '番茄专注' : '放弃专注' }}
            </v-card-title>
            <!-- 对话框内容 -->
            <v-card-text>
              {{ focusButtonActive ? '开始啦' : '再坚持一下吧' }}
            </v-card-text>
            <!-- 操作按钮区（仅在非修改时间和非专注状态时显示） -->
            <v-card-actions v-if="!modifyTimeClicked && !focusButtonActive">
              <v-btn color="red" @click="confirmGiveUpFocus">确认放弃</v-btn>
              <v-btn color="green" @click="continueCountdown">继续计时</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- 弹出对话框2：设置专注和休息时间 -->
        <v-dialog v-model="dialog2" max-width="490">
          <v-card
              elevation="12"
              class="rounded-lg"
              color="#ffffff"
              hover>
            <!-- 对话框标题 -->
            <v-card-title class="custom-card-text headline">设立时间</v-card-title>
            <!-- 提示信息 -->
            <v-card-text class="custom-card2-text">
              番茄专注建议：专注时间25分钟，休息5分钟。
            </v-card-text>
            <!-- 专注时间滑块 -->
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
              <!-- 滑块上的值展示 -->
              <template #thumb="{ value }">
                <div class="slider-thumb-label">{{ formatTime(value / 60) }}</div>
              </template>
            </v-slider>
            <!-- 显示已选择的专注时间 -->
            <div class="selected-time text-center">专注时间: {{ formattedSelectedTime }}</div>

            <!-- 休息时间滑块 -->
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
              <!-- 滑块上的值展示 -->
              <template #thumb="{ value }">
                <div class="slider-thumb-label">{{ formatTime(value / 60) }}</div>
              </template>
            </v-slider>
            <!-- 显示已选择的休息时间 -->
            <div class="selected-time mt-2 text-center">休息时间: {{ formattedRestTime }}</div>

            <!-- 对话框操作按钮 -->
            <v-card-actions class="justify-end">
              <v-btn color="blue darken-1" @click="saveAndCloseModifyTimeDialog">
                确认并关闭
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>

        <!-- 弹出对话框4：专注成功提示 -->
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
import { ref, computed, watch } from 'vue'; // 引入Vue的响应式API，用于创建和管理组件内部状态
import { useRoute } from 'vue-router'; // 引入Vue Router的useRoute钩子，用于获取当前路由信息
import pomodoroService from '@/composables/PomodoroRequest'; // 引入自定义Pomodoro请求服务模块

// 初始化各个响应式变量
const sliderTimeValue = ref<number>(25 * 60); // 工作时间滑块值（默认为25分钟）
const formattedSelectedTime = computed(() => formatTime(sliderTimeValue.value)); // 格式化后的选中工作时间
const countdownTime = ref('0:00'); // 倒计时显示的时间
const focusButtonActive = ref(false); // 专注按钮激活状态
const modifyTimeClicked = ref(false); // 修改时间按钮是否被点击
const dialog1 = ref(false);// 对话框1的显示状态
const dialog2 = ref(false); // 对话框2的显示状态
const dialog4 = ref(false); // 对话框4的显示状态
const hoverFocusButton = ref(false); // 鼠标悬停在专注按钮上的状态
const handleModifyTimeClickActive = ref(true); // 初始化修改时间按钮为可点击状态
const restTimeValue = ref<number>(5 * 60); // 默认休息时间（5分钟）
const formattedRestTime = computed(() => formatTime(restTimeValue.value)); // 格式化后的休息时间
const route = useRoute(); // 获取当前路由对象

let innerId = ref<string | null>(null); // 内部ID变量
let countdownTimer: NodeJS.Timeout | undefined; // 倒计时定时器引用
let remainingTotalSeconds = ref<number | null>(null); // 剩余总秒数
let countdownTimeRef = ref('25:00'); // 倒计时初始值
let itemId: Ref<number | undefined>; // 从URL查询参数中获取的itemId（通过计算属性赋值）

onMounted(() => {
  // 组件挂载后执行的操作
  itemId = computed(() => Number(route.query.itemId)); // 创建一个计算属性来从路由查询参数中提取itemId并转换为数字
  if (itemId) {
    console.log('received', itemId);
    console.log('receivedInnerId:', itemId.value);
  }
});

// 保存并关闭修改时间对话框的函数
function saveAndCloseModifyTimeDialog() {
  // 将工作时间转为分钟
  const workTimeInMinutes = formatTimeToMinutes(sliderTimeValue.value);
  // 将休息时间转为分钟
  const restTimeInMinutes = formatTimeToMinutes(restTimeValue.value);

  pomodoroService.edit(workTimeInMinutes, restTimeInMinutes); // 调用Pomodoro服务模块中的编辑方法，更新工作与休息时间
  dialog2.value = false; // 关闭修改时间对话框
}

// 将秒数格式化为分钟的辅助函数
function formatTimeToMinutes(seconds: number): number {
  return Math.floor(seconds / 60);
}

// 监听休息时间变化的回调函数
function onRestTimeChange(value: number) {
  restTimeValue.value = value; // 更新休息时间的值
}

// 监听focusButtonActive状态变化，根据专注状态切换修改时间按钮的激活状态
watch(focusButtonActive, (newValue) => {
  if (newValue === true) {
    handleModifyTimeClickActive.value = false; // 当开始专注时，禁用修改时间按钮
  } else {
    handleModifyTimeClickActive.value = true; // 当退出专注时，启用修改时间按钮
  }
});

// 监听sliderTimeValue的变化，实时更新倒计时显示的时间
watch(sliderTimeValue, (newValue) => {
  countdownTime.value = formatTime(newValue); // 格式化后赋值给倒计时组件显示
});

// 滑块改变时触发的事件处理函数
function onSliderChange(value: number) {
  sliderTimeValue.value = value; // 更新滑块对应的数值
}

// 将分钟数格式化为标准时间字符串（HH:mm）的辅助函数
function formatTime(minutes: number): string {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;
  return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}`;
}

// 处理“开始/放弃专注”按钮点击事件的方法
const handleFocus = () => {
  focusButtonActive.value = !focusButtonActive.value; // 切换专注状态
  modifyTimeClicked.value = false;

  if (!focusButtonActive.value) { // 当处于专注状态（点击“放弃专注”）
    dialog1.value = true; // 显示确认放弃专注的对话框
  } else if (remainingTotalSeconds.value === null) { // 当倒计时尚未启动（点击“开始专注”）
    // 确保从路由参数中正确获取到innerId并转换为数字类型
    const innerIdNumber = Number(itemId.value);

    if (!isNaN(innerIdNumber)) { // 如果转换成功
      pomodoroService.start(innerIdNumber); // 调用服务接口开始专注，传入innerId
    }

    startCountdown(); // 启动倒计时
  } else { // 若已处于非专注状态，恢复为专注状态
    focusButtonActive.value = true;
  }
};

// 关闭专注成功对话框，并结束当前专注任务的方法
async function closeDialog4AndEndPomodoro() {
  dialog4.value = false; // 关闭专注成功对话框
  dialog1.value = false; // 关闭可能存在的其他对话框
  const { data } = await pomodoroService.end(); // 调用服务接口结束专注任务
};

// 处理点击修改时间按钮的事件方法
const handleModifyTimeClick = () => {
  modifyTimeClicked.value = true; // 设置修改时间按钮已点击标识
  dialog1.value = false; // 隐藏当前可能显示的对话框
  dialog2.value = true; // 显示设置时间的对话框
};

// 确认放弃专注事件处理方法
const confirmGiveUpFocus = () => {
  focusButtonActive.value = false; // 取消专注状态
  countdownTime.value = formatTime(sliderTimeValue.value); // 更新倒计时显示为初始工作时间
  dialog1.value = false; // 隐藏确认放弃专注对话框

  if (countdownTimer !== undefined) {
    clearInterval(countdownTimer); // 清除倒计时定时器
    countdownTimer = undefined;
  }
};

// 继续专注事件处理方法
const continueCountdown = () => {
  focusButtonActive.value = true; // 恢复专注状态
  dialog1.value = false; // 隐藏对话框

  if (countdownTimer === undefined && remainingTotalSeconds.value !== null) { // 当倒计时未启动且剩余时间有效时
    countdownTimer = setInterval(() => {
      if (remainingTotalSeconds.value !== null) {
        remainingTotalSeconds.value -= 1; // 减少剩余秒数

        const remainingMinutes = Math.floor(remainingTotalSeconds.value / 60);
        const remainingSeconds = remainingTotalSeconds.value % 60;

        countdownTimeRef.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds
            .toString()
            .padStart(2, '0')}`; // 更新倒计时显示

        if (remainingTotalSeconds.value <= 0) { // 倒计时结束
          clearInterval(countdownTimer!); // 清除定时器
          countdownTimer = undefined;
          remainingTotalSeconds.value = null;
          focusButtonActive.value = false; // 结束专注状态
          dialog1.value = true; // 显示提示对话框
        }
      }
    }, 1000); // 每秒执行一次更新倒计时操作
  } else if (countdownTimer !== undefined) { // 如果倒计时已经在进行中，则无需重新启动定时器
    focusButtonActive.value = true; // 保持专注状态
  }
};

// 定义开始专注倒计时的方法
const startCountdown = () => {
  // 分解当前倒计时时间（例如 '25:00'）为分钟和秒数
  const [minutes, seconds] = countdownTime.value.split(':');

  // 将分钟和秒数转换为总秒数
  let totalSeconds = parseInt(minutes!) * 60 + parseInt(seconds!);

  // 创建一个定时器，每秒执行一次回调函数以递减倒计时
  countdownTimer = setInterval(() => {
    // 减少一秒
    totalSeconds -= 1;

    // 计算剩余的分钟和秒数
    const remainingMinutes = Math.floor(totalSeconds / 60);
    const remainingSeconds = totalSeconds % 60;

    // 更新显示的倒计时时间
    countdownTime.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;

    // 当倒计时结束时
    if (totalSeconds <= 0) {
      // 清除当前倒计时定时器
      clearInterval(countdownTimer!);
      countdownTimer = undefined;

      // 开始休息时间倒计时
      countdownTime.value = formattedRestTime.value;
      startRestCountdown();
    }
  }, 1000); // 每隔1000毫秒（即1秒）执行一次递减操作
};

// 定义开始休息倒计时的方法
const startRestCountdown = () => {
  // 同样从倒计时时间中分解出分钟和秒数
  const [minutes, seconds] = countdownTime.value.split(':');

  // 转换为总秒数
  let totalSeconds = parseInt(minutes!) * 60 + parseInt(seconds!);

  // 创建一个新的定时器用于休息倒计时
  countdownTimer = setInterval(() => {
    // 每秒递减休息时间
    totalSeconds -= 1;

    // 计算并更新剩余的休息时间
    const remainingMinutes = Math.floor(totalSeconds / 60);
    const remainingSeconds = totalSeconds % 60;
    countdownTime.value = `${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`;

    // 当休息时间结束时
    if (totalSeconds <= 0) {
      // 清除休息倒计时定时器
      clearInterval(countdownTimer!);
      countdownTimer = undefined;

      // 退出专注状态，并打开提示对话框
      focusButtonActive.value = false;
      dialog1.value = true;
      dialog4.value = true; // 显示专注成功对话框
    }
  }, 1000); // 每隔1秒执行一次休息时间递减操作
};

</script>

<!-- 样式文件，包含对各种元素的样式定义 -->

<style scoped>
/* 定义头部区域样式 */
header {
  text-align: center; /* 文本居中对齐 */
  background-color: #4CAF50; /* 设置头部背景颜色为绿色 */
  color: white; /* 设置文字颜色为白色 */
  height: 10vh; /* 头部高度为视口高度的10% */
  padding: 20px; /* 内边距设置为20像素 */
}

/* 定义侧边栏样式 */
aside {
  text-align: center; /* 文本居中对齐 */
  background-color: #4cafa0; /* 设置背景颜色为浅绿色 */
  height: 3vh; /* 高度为视口高度的3% */
  padding: 1px; /* 微小内边距 */
}

/* 定义按钮样式 */
button {
  margin: 5px; /* 周围间距为5像素 */
  padding: 10px 20px; /* 内边距为上下10像素、左右20像素 */
  border: none; /* 没有边框 */
  border-radius: 5px; /* 圆角半径为5像素 */
  transition: all 0.3s ease; /* 添加过渡动画效果，所有属性在0.3秒内平滑变化 */
  font-size: 1.2rem; /* 字体大小为1.2倍根元素字体大小 */
}

/* 当按钮具有.active类时应用的样式 */
button.active {
  background-color: #ffffff; /* 背景颜色设为白色 */
  color: black; /* 文字颜色设为黑色 */
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加水平垂直偏移为0、模糊半径为4像素、扩散半径为0、颜色透明度为0.2的阴影效果 */
}

/* .redButton 类型按钮样式 */
button.redButton {
  background-color: #ff3c00; /* 红色背景 */
  color: white; /* 白色文字 */
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加阴影效果 */
}

/* .whiteButton 类型按钮样式 */
button.whiteButton {
  background-color: #faf7f6; /* 浅灰色背景 */
  color: #090808; /* 深灰色文字 */
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2); /* 添加阴影效果 */
}

/* 计时器容器样式 */
.countdown-container {
  display: flex; /* 使用弹性盒布局 */
  justify-content: center; /* 子元素沿主轴居中 */
  align-items: center; /* 子元素沿交叉轴居中 */
  height: 70vh; /* 高度为视口高度的70% */
  z-index: 0; /* 设置层叠顺序 */
}

/* 自定义标题样式 */
.custom-title {
  font-size: 3rem; /* 调整字体大小至3倍根元素字体大小 */
  font-family: 'Arial', sans-serif; /* 更改字体为 Arial 或系统默认无衬线字体 */
  font-weight: bold; /* 加粗字体（可选） */
}

/* 计时器容器:before 伪元素样式，用于添加背景图片 */
.countdown-container::before {
  content: ""; /* 必须声明内容为空字符串以生成伪元素 */
  position: absolute; /* 设置绝对定位 */
  bottom: 0;
  top: 0;
  left: 0;
  width: 100%; /* 宽度和高度均填满父元素 */
  height: 100%;
  z-index: -1; /* 设置在计时器容器下层 */
  background-position: center; /* 背景图片居中 */
  background-size: cover; /* 背景图片适应容器尺寸 */
  opacity: 0.5; /* 设置背景图片透明度为50%（可选） */
}

/* 计时器主体部分样式 */
.countdown {
  position: relative; /* 设置相对定位以便子元素进行定位 */
}

/* 计时器数字显示样式 */
.timer {
  position: absolute; /* 绝对定位 */
  top: 50%; /* 上边距为父元素高度的50% */
  left: 50%; /* 左边距为父元素宽度的50% */
  transform: translate(-50%, -50%); /* 进行平移以实现居中对齐 */
  font-size: 3rem; /* 字体大小为3倍根元素字体大小 */
  font-family: 'Arial', sans-serif; /* 更改字体样式 */
}

/* 添加脉冲动画样式 */
.pulse-animation {
  animation: pulse 1.5s infinite; /* 应用名为pulse的无限循环动画，每次动画持续时间为1.5秒 */
}

/* 定义名为pulse的关键帧动画 */
@keyframes pulse {
  0% {
    transform: scale(1); /* 开始时缩放比例为1 */
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0.7); /* 初始阴影为半径0且不透明度为0.7的黑色 */
  }
  50% {
    transform: scale(1.1); /* 在50%时放大至1.1倍 */
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0); /* 移除阴影 */
  }
  100% {
    transform: scale(1); /* 结束时恢复至原尺寸 */
    box-shadow: 0 0 0 0 rgba(0, 0, 0, 0); /* 保持无阴影状态 */
  }
}

/* 自定义滑块组件样式 */
.custom-slider {
  margin-top: 16px; /* 设置顶部外边距为16像素 */
}

/* 滑块拇指标签样式 */
.slider-thumb-label {
  position: absolute; /* 绝对定位 */
  top: -24px; /* 上边距为负值，使标签位于滑块上方 */
  width: fit-content; /* 宽度根据内容自适应 */
  height: 24px; /* 高度为24像素 */
  line-height: 24px; /* 行高与高度一致，确保垂直居中 */
  text-align: center; /* 文本居中对齐 */
  border-radius: 50%; /* 圆角半径为50%，创建圆形外观 */
  background-color: white; /* 背景颜色为白色 */
  color: #4cafa0; /* 文字颜色为浅绿色 */
  font-size: 14px; /* 字体大小为14像素 */
  z-index: 1; /* 设置层叠顺序高于滑块本身 */
  pointer-events: none; /* 不响应鼠标事件，避免影响滑动操作 */
}

/* 自定义卡片文本样式 */
.custom-card-text {
  color: black; /* 文字颜色为黑色 */
  background-color: #C3E2C2; /* 背景颜色为浅绿色 */
}

/* 另一个自定义卡片文本样式 */
.custom-card2-text {
  color: black; /* 文字颜色为黑色 */
  background-color: #EAECCC; /* 背景颜色为浅米色 */
}
</style>

