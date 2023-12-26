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

    Instant startTime;
    Instant endTime;
    PomodoroStatus pomodoroStatus;
    PomodoroRecord pomodoroRecord;
    static int inner;

    /**
     * 构造函数
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public Pomodoro(Instant startTime,Instant endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 设置 状态
     * @param pomodoroStatus 状态
     */
    public void setPomodoroStatus(PomodoroStatus pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
    }

    /**
     * 设置开始时间
     * @param startTime
     */
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    /**
     * 设置截止时间
     * @param endTime 结束时间
     */
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
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
}
