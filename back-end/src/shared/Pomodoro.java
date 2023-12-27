package shared;

import java.time.Instant;

/**
 * 番茄中钟类
 */
public class Pomodoro {
    /**
     * 属性
     *
     * 开始时间
     * 结束时间
     * 状态
     * 番茄钟信息
     */
    int workTime,restTime;
    PomodoroStatus pomodoroStatus;
    PomodoroRecord pomodoroRecord;


    /**
     * 设置 状态
     * @param pomodoroStatus 状态
     */
    public void setPomodoroStatus(PomodoroStatus pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
    }


    /**
     * 返回番茄钟信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pomodoroStatus 番茄状态
     * @return 番茄时间
     */
    public PomodoroRecord getPomodoroRecord(int innerId,Instant startTime,Instant endTime,PomodoroStatus pomodoroStatus) {
        this.pomodoroRecord = new PomodoroRecord(innerId,startTime,endTime,pomodoroStatus);
        return pomodoroRecord;
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
}
