package shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import notify.PomodoroStatusChangedNotificationArgs;
import sys.AoXiangToDoListSystem;
import sys.Messages;
import sys.NotificationController;
import trans.NotificationType;
import util.DateHelper;
import util.JsonUtility;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * 番茄钟类
 */
public class Pomodoro implements Observable<Pomodoro> {
    /**
     * 属性
     * 开始时间
     * 结束时间
     * 状态
     * 番茄钟信息
     */
    private int workTime = 25, restTime = 5;
    private Integer boundToDoWorkInnerId;
    private ToDoWorkItem boundToDoWork;
    private PomodoroStatus pomodoroStatus = PomodoroStatus.NotStarted;
    private PomodoroRecord pomodoroRecord;
    private Timer timer;
    private boolean workTimePassed, restTimePassed;
    private Instant expectedWorkEndTime = Instant.now(), expectedRestEndTime = Instant.now();
    private Instant startTime = Instant.now();

    Vector<Observer<Pomodoro>> observers = new Vector<>();

    public Pomodoro() {
        this.observers.add(new PomodoroObserver());//添加属性变化观察者。
    }

    @Override
    public void addObserver(Observer<Pomodoro> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Pomodoro> observer) {
        observers.remove(observer);
    }

    void notifyObservers() {
        observers.forEach(o -> o.propertyChanged(this));
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public PomodoroStatus getPomodoroStatus() {
        return pomodoroStatus;
    }

    public void setPomodoroStatus(PomodoroStatus pomodoroStatus) {
        if (this.pomodoroStatus != pomodoroStatus) {
            this.pomodoroStatus = pomodoroStatus;
            notifyObservers();
        }
    }


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant getExpectedWorkEndTime() {
        return expectedWorkEndTime;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant getExpectedRestEndTime() {
        return expectedRestEndTime;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Instant getStartTime() {
        return startTime;
    }
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer getBoundToDoWorkInnerId() {
        return boundToDoWorkInnerId;
    }

    void initialize() {
        pomodoroRecord = new PomodoroRecord();
        pomodoroRecord.setInnerId(AoXiangToDoListSystem.getInstance().getPomodoroRecordsCollection().getAvailableID());
        pomodoroRecord.setStartTime(Instant.now());
        pomodoroRecord.setPomodoroRecordStatus(PomodoroRecordStatus.Calculating);
        workTimePassed = false;
        restTimePassed = false;
        startTime = Instant.now();
        expectedWorkEndTime = Instant.now().plusSeconds(getWorkTime() * 60L);
        expectedRestEndTime = Instant.now().plusSeconds((getWorkTime() + getRestTime()) * 60L);
        this.timer = new Timer("pomodoro", false);
    }

    public Duration timeSpanBeforeRest() {
        Instant now = Instant.now();
        if (now.isBefore(expectedRestEndTime)) {
            return Duration.between(Instant.now(), expectedRestEndTime);
        }
        return Duration.ZERO;
    }

    public Duration timeSpanBeforeEnd() {
        Instant now = Instant.now();    
        if (now.isBefore(expectedWorkEndTime)) {
            return Duration.between(Instant.now(), expectedWorkEndTime);
        }
        return Duration.ZERO;
    }

    public void stop() throws IllegalStateException {
        if (getPomodoroStatus() != PomodoroStatus.Working && getPomodoroStatus() != PomodoroStatus.Resting)
            throw new IllegalStateException(Messages.ZH_CN.POMODORO_NOT_WORKING);

        onEnded();
    }

    public void start(int todoInnerId) {
        if (getPomodoroStatus() == PomodoroStatus.Working || getPomodoroStatus() == PomodoroStatus.Resting)
            throw new IllegalStateException(Messages.ZH_CN.POMODORO_ALREADY_WORKING);

        boundToDoWork = AoXiangToDoListSystem.getInstance().getToDoWorkItemCollection().findFirst(todoInnerId);
        if (boundToDoWork == null)
            throw new IllegalArgumentException(String.format("[Pomodoro.start] innerId为%s的待办事项不存在。", todoInnerId));
        boundToDoWorkInnerId = boundToDoWork.getInnerId();

        initialize();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 1000, 1000);
        setPomodoroStatus(PomodoroStatus.Working);
        System.out.printf("[Pomodoro.start] 番茄钟现在开始运行，工作时长 = %s，休息时长 = %s。\n", getWorkTime(), getRestTime());
    }

    void update() {
        Instant now = Instant.now();
        if (now.isAfter(expectedWorkEndTime)) {
            if (now.isAfter(expectedRestEndTime)) {
                if (!restTimePassed)
                    onRestTimeEnded();
            } else {
                if (!workTimePassed)
                    onWorkTimeEnded();
            }
        }
    }

    void onWorkTimeEnded() {
        workTimePassed = true;
        setPomodoroStatus(PomodoroStatus.Resting);
    }

    void onRestTimeEnded() {
        restTimePassed = true;
        onEnded();
    }

    void onEnded() {
        timer.cancel();
        var status = Instant.now().isAfter(expectedWorkEndTime) ? PomodoroStatus.Finished : PomodoroStatus.Interrupted;
        var recordStatus = Instant.now().isAfter(expectedWorkEndTime) ? PomodoroRecordStatus.Finished : PomodoroRecordStatus.Interrupted;
        setPomodoroStatus(status);
        if(recordStatus == PomodoroRecordStatus.Finished) {
            pomodoroRecord.setEndTime(expectedRestEndTime);
        }else{
            pomodoroRecord.setEndTime(Instant.now());
        }
        pomodoroRecord.setPomodoroRecordStatus(recordStatus);
        AoXiangToDoListSystem.getInstance().getPomodoroRecordsCollection().add(pomodoroRecord);
        boundToDoWork.getPomodoroRecordInnerIdList().add(pomodoroRecord.getInnerId());
    }
}

/**
 * 实现对番茄钟属性变化的监听和通知转发。
 */
class PomodoroObserver implements Observer<Pomodoro> {
    private PomodoroStatus status = PomodoroStatus.NotStarted;
    private final NotificationController controller = AoXiangToDoListSystem.getInstance().getNotificationController();

    @Override
    public void propertyChanged(Pomodoro obj) {
        if (obj.getPomodoroStatus() != status) {
            status = obj.getPomodoroStatus();
            try {
                controller.distributePomodoroStatusChanged(status);
            } catch (Exception exception) {
                System.err.printf("[PomodoroObserver.propertyChanged] 发送番茄钟状态变化通知时发生错误：%s\n", exception.getMessage());
            }
        }
    }
}
