package shared;

import java.time.Instant;

public class Pomodoro {
    Instant startTime;
    Instant endTime;
    PomodoroStatus pomodoroStatus;

    PomodoroRecord pomodoroRecord;

    public Pomodoro(Instant startTime,Instant endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public void setPomodoroStatus(PomodoroStatus pomodoroStatus) {
        this.pomodoroStatus = pomodoroStatus;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }


    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }


}
