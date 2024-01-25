package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Vector;

/**
 * 番茄钟记录。
 *
 * @author 贾聪毅
 */
public class PomodoroRecord implements Observable<PomodoroRecord> {
    private int innerId;
    private Instant startTime, endTime;
    private PomodoroRecordStatus pomodoroRecordStatus = PomodoroRecordStatus.Calculating;
    private boolean notifySuppressed = false;
    private boolean notifyRequired = false;
    private final Vector<Observer<PomodoroRecord>> observers = new Vector<>();

    public int getInnerId() {
        return innerId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public PomodoroRecordStatus getPomodoroRecordStatus() {
        return pomodoroRecordStatus;
    }

    @JsonIgnore
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * 结束番茄钟
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Instant endTime) {
        if (!Objects.equals(this.endTime, endTime)) {
            this.endTime = startTime;
            notifyObservers();
        }
    }

    public void setStartTime(Instant startTime) {
        if (!Objects.equals(this.startTime, startTime)) {
            this.startTime = startTime;
            notifyObservers();
        }
    }
    public void setPomodoroRecordStatus(PomodoroRecordStatus pomodoroRecordStatus) {
        if (this.pomodoroRecordStatus != pomodoroRecordStatus) {
            this.pomodoroRecordStatus = pomodoroRecordStatus;
            notifyObservers();
        }
    }


//    public void setCompletedCycleCount(int completedCycleCount) {
//        this.completedCycleCount = completedCycleCount;
//    }

    public void setInnerId(int innerId) {
        if (this.innerId != innerId) {
            this.innerId = innerId;
            notifyObservers();
        }
    }


    @Override
    public String toString() {
        return innerId + "_" + startTime.toString() + "_" + endTime.toString() + "_" + pomodoroRecordStatus;
    }

    @Override
    public void addObserver(Observer<PomodoroRecord> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<PomodoroRecord> observer) {
        observers.remove(observer);
    }

    @Override
    public void suppressNotify() {
        notifySuppressed = true;
    }

    @Override
    public void resumeNotifyAndForget() {
        notifySuppressed = false;
    }

    @Override
    public void resumeAndNotifyOnceOnChanged() {
        if (notifyRequired) notifyObservers();
        notifySuppressed = false;
    }

    /**
     * 通知所有观察者。
     * 需要在任意需要通知的属性发生变化时（即set方法被调用）时调用。
     */
    void notifyObservers() {
        if (!notifySuppressed) {
            notifyRequired = false;
            observers.forEach(o -> o.propertyChanged(this));
        } else notifyRequired = true;
    }
}
