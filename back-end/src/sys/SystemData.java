package sys;

import shared.JsonConvertable;
import shared.Pomodoro;
import shared.User;

import java.time.Instant;

/**
 * 定义了需要保存或同步的系统数据。
 */
public class SystemData implements JsonConvertable {
    private User currentUser;
    private ToDoWorkItemCollection toDoWorkItemCollection = new ToDoWorkItemCollection();
    private PomodoroRecordCollection pomodoroRecordCollection = new PomodoroRecordCollection();
    private Pomodoro pomodoro = new Pomodoro();

    private boolean isSynchronized;

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public Instant getLastModifiedInstant() {
        return lastModifiedInstant;
    }

    /**
     * 更新系统数据的最后修改时间为当前时间。
     */
    public void updateLastModifiedInstant(){
        lastModifiedInstant = Instant.now();
    }

    Instant lastModifiedInstant = Instant.now();

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ToDoWorkItemCollection getToDoWorkItemCollection() {
        return toDoWorkItemCollection;
    }

    public void setToDoWorkItemCollection(ToDoWorkItemCollection toDoWorkItemCollection) {
        this.toDoWorkItemCollection = toDoWorkItemCollection;
    }

    public PomodoroRecordCollection getPomodoroRecordCollection() {
        return pomodoroRecordCollection;
    }

    public void setPomodoroRecordCollection(PomodoroRecordCollection pomodoroRecordCollection) {
        this.pomodoroRecordCollection = pomodoroRecordCollection;
    }

    public Pomodoro getPomodoro() {
        return pomodoro;
    }

    public void setPomodoro(Pomodoro pomodoro) {
        this.pomodoro = pomodoro;
    }

}
