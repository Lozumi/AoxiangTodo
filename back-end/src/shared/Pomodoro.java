package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    int workTime = 25,restTime = 5;
    @JsonIgnore
    PomodoroRecord pomodoroRecord;
    Timer pomodoroTimer;

    public Pomodoro(){
        this.pomodoroTimer = new Timer("pomodoro",false);
    }
    /**
     * 设置休息时间
     * @param restTime 休息时间
     */
    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    /**
     * 设置工作时间
     * @param workTime 工作时间
     */
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    /**
     * 返回休息时间
     * @return 休息时间
     */
    public int getRestTime() {
        return restTime;
    }

    /**
     * 返回工作时间
     * @return 工作时间
     */
    public int getWorkTime() {
        return workTime;
    }

    @JsonIgnore
    public PomodoroRecord getPomodoroRecord() {
        return pomodoroRecord;
    }


    public static Pomodoro fromJsonString(String json) {
        return JsonUtility.objectFromJsonString(json, Pomodoro.class);
    }

    public static Pomodoro fromJsonBytes(byte[] bytes) {
        return JsonUtility.objectFromJsonBytes(bytes, Pomodoro.class);
    }

    public static Pomodoro fromJsonStream(InputStream stream, int expectedLength) {
        return JsonUtility.objectFromInputStream(stream, expectedLength, Pomodoro.class);
    }

    /**
     * 开始番茄钟
     */
    public void startPomodoro(){
        this.pomodoroRecord = new PomodoroRecord();
        this.pomodoroRecord.setStartTime(Instant.now());
        //开始主动计时
        startTimer();
    }

    /**
     * 结束番茄钟，同时计算番茄记录
     */
    public PomodoroRecord endPomodoro(){
        endTimer();
        this.pomodoroRecord.setEndTime(Instant.now());
        calculateRecord();
        return pomodoroRecord;
    }
    /**
     * 计算pomodoroRecord
     * 完成时计算
     *
     */
    public void calculateRecord(){
        Instant startTime = this.getPomodoroRecord().startTime;
        Instant endTime = this.getPomodoroRecord().endTime;
        // 计算状态
        int durationTime = (int) Duration.between(startTime, endTime).toMinutes();
        if (durationTime >= workTime){
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Finished);
        }else {
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Interrupted);
        }
//        // 循环计算逻辑：完成了多少个工作时间
//        int completedCycleCount = durationTime/(workTime+restTime);
//        if (durationTime%(workTime+restTime)>=workTime){
//            completedCycleCount += 1;
//        }
//        pomodoroRecord.setCompletedCycleCount(completedCycleCount);
    }

    // 计时模块
    private void startTimer(){
        int delay = (workTime+restTime)*60000;
        // 开始计时
        pomodoroTimer.schedule(new TimerTask(){
            @Override
            public void run(){
                // 正常结束番茄钟
                endPomodoro();
            }
        },delay);
    }

    /**
     * 打断番茄钟计时
     */
    private void endTimer(){
        pomodoroTimer.cancel();
    }
}
