package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import util.JsonUtility;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

/**
 * 番茄中钟类
 */
public class Pomodoro {
    /**
     * 属性
     * 开始时间
     * 结束时间
     * 状态
     * 番茄钟信息
     */
    int workTime,restTime;
    @JsonIgnore
    PomodoroRecord pomodoroRecord;

    public Pomodoro(){
        this.pomodoroRecord = new PomodoroRecord();
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
        this.pomodoroRecord.setStartTime(Instant.now());
    }

    /**
     * 结束番茄钟，同时计算番茄记录
     */
    public void endPomodoro(){
        this.pomodoroRecord.setEndTime(Instant.now());
        calculateRecord();
    }
    /**
     * 计算pomodoroRecord
     * 完成时计算
     *
     */
    public void calculateRecord(){
        Instant startTime = this.getPomodoroRecord().startTime;
        Instant endTime = this.getPomodoroRecord().endTime;
        int durationTime = (int) Duration.between(startTime, endTime).toMinutes();
        if (durationTime >= workTime){
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Finished);
        }else {
            pomodoroRecord.setPomodoroStatus(PomodoroStatus.Interrupted);
        }
        // 计算逻辑：完成了多少个工作时间
        int completedCycleCount = durationTime/(workTime+restTime);
        if (durationTime%(workTime+restTime)>=workTime){
            completedCycleCount += 1;
        }
        pomodoroRecord.setCompletedCycleCount(completedCycleCount);
    }
}
