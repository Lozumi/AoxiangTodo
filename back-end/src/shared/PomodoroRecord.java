package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.time.Instant;

/**
 * 番茄钟记录。
 * @author 贾聪毅
 */
public class PomodoroRecord {
    int innerId;
//    int completedCycleCount;
    Instant startTime,endTime;
    PomodoroStatus pomodoroStatus;

    public int getInnerId() {
        return innerId;
    }

//    public int getCompletedCycleCount() {
//        return completedCycleCount;
//    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public PomodoroStatus getPomodoroStatus() {
        return pomodoroStatus;
    }

    @JsonIgnore
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * 结束番茄钟
     * @param endTime 结束时间
     */
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setPomodoroStatus(PomodoroStatus pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
    }

//    public void setCompletedCycleCount(int completedCycleCount) {
//        this.completedCycleCount = completedCycleCount;
//    }

    public void setInnerId(int innerId) {
        this.innerId = innerId;
    }


    @Override
    public String toString(){
        return innerId+"_"+startTime.toString()+"_"+endTime.toString()+"_"+pomodoroStatus;
    }
}
