package sys;

import shared.JsonConvertable;
import shared.Pomodoro;
import shared.User;
import util.JsonUtility;

import java.time.Instant;

/**
 * 定义了需要保存或同步的系统数据。
 */
public class SystemData implements JsonConvertable {
    private User currentUser;
    private final ToDoWorkItemCollection toDoWorkItemCollection = new ToDoWorkItemCollection();
    private final PomodoroRecordCollection pomodoroRecordCollection = new PomodoroRecordCollection();
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
    public void updateLastModifiedInstant() {
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

    public PomodoroRecordCollection getPomodoroRecordCollection() {
        return pomodoroRecordCollection;
    }

    public Pomodoro getPomodoro() {
        return pomodoro;
    }

    public void setPomodoro(Pomodoro pomodoro) {
        this.pomodoro = pomodoro;
    }


    private static SystemData instance;
    private SystemData() {

    }

    /**
     * 获取SystemData的实例。
     * @return SystemData实例。
     */
    public synchronized static SystemData getInstance(){
        if(instance == null){
            instance = new SystemData();
        }
        return instance;
    }

    /**
     * 通过SystemData的JSON字符串更新本实例，如果字符串无效，则不进行任何更改。
     *
     * @param systemDataJson JSON。
     */
    public void update(String systemDataJson) {
        try {
            var systemData = JsonUtility.objectFromJsonString(systemDataJson, SystemData.class);
            this.setCurrentUser(systemData.getCurrentUser());
            this.setSynchronized(systemData.isSynchronized());
            this.setPomodoro(systemData.getPomodoro());

            this.getToDoWorkItemCollection().clear();
            this.getToDoWorkItemCollection().addAll(systemData.getToDoWorkItemCollection());

            this.getPomodoroRecordCollection().clear();
            this.getPomodoroRecordCollection().addAll(systemData.getPomodoroRecordCollection());
        } catch (Exception e) {
            System.err.printf("更新SystemData数据失败，原因：%s\n", e.getMessage());
        }
    }

}
