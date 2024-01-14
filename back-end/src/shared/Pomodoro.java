package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import sys.AoXiangToDoListSystem;
import sys.Messages;
import util.DateHelper;
import util.JsonUtility;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 番茄钟类
 */
public class Pomodoro {
    /**
     * 属性
     * 开始时间
     * 结束时间
     * 状态
     * 番茄钟信息
     */
    int workTime = 25, restTime = 5;
    @JsonIgnore
    PomodoroRecord pomodoroRecord;
    @JsonIgnore
    Timer pomodoroTimer;

    /**
     * 获取一个值，指示了当前番茄钟是否正在工作。
     *
     * @return true表示正在工作，false表示未工作。
     */
    @JsonIgnore
    public boolean isWorking() {
        return working;
    }

    @JsonIgnore
    boolean working;

    public Pomodoro() {
        this.pomodoroTimer = new Timer("pomodoro", false);
    }

    /**
     * 设置休息时间
     *
     * @param restTime 休息时间
     */
    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    /**
     * 设置工作时间
     *
     * @param workTime 工作时间
     */
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    /**
     * 返回休息时间
     *
     * @return 休息时间
     */
    public int getRestTime() {
        return restTime;
    }

    /**
     * 返回工作时间
     *
     * @return 工作时间
     */
    public int getWorkTime() {
        return workTime;
    }

    @JsonIgnore
    public PomodoroRecord getPomodoroRecord() {
        return pomodoroRecord;
    }


    public static Pomodoro fromJsonString(String json) throws Exception {
        return JsonUtility.objectFromJsonString(json, Pomodoro.class);
    }

    public static Pomodoro fromJsonBytes(byte[] bytes) throws Exception {
        return JsonUtility.objectFromJsonBytes(bytes, Pomodoro.class);
    }

    public static Pomodoro fromJsonStream(InputStream stream, int expectedLength) throws Exception {
        return JsonUtility.objectFromInputStream(stream, expectedLength, Pomodoro.class);
    }

    /**
     * 开始番茄钟。如果番茄钟正在工作，则抛出异常。
     */
    public synchronized void startPomodoro() {
        if (working)
            throw new IllegalStateException(Messages.ZH_CN.POMODORO_ALREADY_WORKING);
        this.pomodoroRecord = new PomodoroRecord();
        this.pomodoroRecord.setStartTime(Instant.now());
        //开始主动计时
        startTimer();
    }

    /**
     * 结束番茄钟，同时计算番茄记录。如果番茄钟尚未开始工作，则抛出异常。
     */
    public synchronized void endPomodoro() {
        if (!working)
            throw new IllegalStateException(Messages.ZH_CN.POMODORO_NOT_WORKING);
        endTimer();
        working = false;
        this.pomodoroRecord.setEndTime(Instant.now());
        calculateRecord();
        pomodoroRecord.setInnerId(AoXiangToDoListSystem.getInstance().getPomodoroRecordsCollection().getAvailableID());
        AoXiangToDoListSystem.getInstance().getPomodoroRecordsCollection().add(pomodoroRecord);//添加番茄钟记录到集合。
        AoXiangToDoListSystem.getInstance().updateSystemDataLastModifiedInstant();
    }

    /**
     * 计算pomodoroRecord
     * 完成时计算
     */
    public void calculateRecord() {
        Instant startTime = this.getPomodoroRecord().startTime;
        Instant endTime = this.getPomodoroRecord().endTime;

        // 计算状态
        int durationTime = (int) Duration.between(startTime, endTime).toMinutes();
        if (durationTime >= workTime) {
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Finished);
        } else {
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Interrupted);
        }
        System.out.printf("计算番茄钟记录完成，duration = %s\n",pomodoroRecord.getDuration());
//        // 循环计算逻辑：完成了多少个工作时间
//        int completedCycleCount = durationTime/(workTime+restTime);
//        if (durationTime%(workTime+restTime)>=workTime){
//            completedCycleCount += 1;
//        }
//        pomodoroRecord.setCompletedCycleCount(completedCycleCount);
    }

    // 计时模块
    private void startTimer() {
        int delay = (workTime + restTime) * 60000;
        // 开始计时
        pomodoroTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 正常结束番茄钟
                endPomodoro();
            }
        }, delay);
        working = true;
        System.out.printf("[startTimer]番茄钟已经启动 Instant.now = \"%s\"\n", DateHelper.instantToStandardTime(Instant.now()));
    }

    /**
     * 打断番茄钟计时
     */
    private void endTimer() {
        pomodoroTimer.cancel();
        System.out.printf("[endTimer]番茄钟已经终止 Instant.now = \"%s\"\n", DateHelper.instantToStandardTime(Instant.now()));
    }
}
