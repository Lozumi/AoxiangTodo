package shared;

import java.time.Duration;
import java.time.Instant;

/**
 * 番茄钟记录。
 * @author 贾聪毅
 */
public class PomodoroRecord {
    int innerId;
    int completedCycleCount;
    Instant startTime,endTime;
    PomodoroStatus pomodoroStatus;

    public int getInnerId() {
        return innerId;
    }

    public int getCompletedCycleCount() {
        return completedCycleCount;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public PomodoroStatus getPomodoroStatus() {
        return pomodoroStatus;
    }

    Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public PomodoroRecord(int innerId, Instant startTime,Instant endTime,PomodoroStatus status)
    {
        this.innerId = innerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pomodoroStatus = status;
    }
}
