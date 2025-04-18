package shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import util.JsonUtility;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author 贾聪毅
 * 待办事项。
 */
public class ToDoWorkItem implements JsonConvertable, Observable<ToDoWorkItem> {
    //属性定义
    private int layer; //从1开始
    private int innerId, importancePriority, emergencyPriority;
    private String title, subtitle, description;
    private Instant createTime, startTime, deadLine;
    private WorkItemStatus status = WorkItemStatus.None;
    private List<Integer> subToDoWorkItemInnerIdList;
    private List<Integer> pomodoroRecordInnerIdList;

    /**
     * 获取层级（1为顶层，递增）。
     *
     * @return 层级。
     */
    public int getLayer() {
        return layer;
    }

    /**
     * 设置层级。
     *
     * @param layer 层级。
     */
    public void setLayer(int layer) {
        this.layer = layer;
        notifyObservers();
    }

    /**
     * 获取事项的内部Id。
     *
     * @return 内部Id。
     */
    public int getInnerId() {
        return innerId;
    }

    /**
     * @return 事项的重要优先级。
     */
    public int getImportancePriority() {
        return importancePriority;
    }

    /**
     * 设置事项重要性优先级。
     *
     * @param importancePriority 新重要优先级。
     */
    public void setImportancePriority(int importancePriority) {
        this.importancePriority = importancePriority;
        notifyObservers();
    }

    /**
     * @return 事项的紧急性优先级。
     */
    public int getEmergencyPriority() {
        return emergencyPriority;
    }

    /**
     * 设置事项的紧急性优先级。
     *
     * @param emergencyPriority 新紧急性优先级。
     */
    public void setEmergencyPriority(int emergencyPriority) {
        this.emergencyPriority = emergencyPriority;
        notifyObservers();
    }

    /**
     * 获取事项标题。
     *
     * @return 事项标题。
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置事项标题。
     *
     * @param title 新事项标题。
     */
    public void setTitle(String title) {
        this.title = title;
        notifyObservers();
    }

    /**
     * 获取事项子标题。
     *
     * @return 事项子标题。
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置事项子标题。
     *
     * @param subtitle 新事项子标题。
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        notifyObservers();
    }

    /**
     * 获取事项描述。
     *
     * @return 事项的详细描述。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置事项的详细描述。
     *
     * @param description 新的事项的详细描述。
     */
    public void setDescription(String description) {
        this.description = description;
        notifyObservers();
    }

    /**
     * 获取事项的创建时间。
     *
     * @return 事项的创建时间。
     */
    public Instant getCreateTime() {
        return createTime;
    }

    /**
     * 获取事项的开始时间。
     *
     * @return 事项的开始时间。
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * 设置事项的开始时间。
     *
     * @param startTime 新的事项的开始时间。
     */
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
        notifyObservers();
    }

    /**
     * 获取事项的截止时间。
     *
     * @return 事项的截止时间。
     */
    public Instant getDeadLine() {
        return deadLine;
    }

    /**
     * 设置事项的截止时间。
     *
     * @param deadLine 新的截止时间。
     */
    public void setDeadLine(Instant deadLine) {
        this.deadLine = deadLine;
        notifyObservers();
    }

    /**
     * 获取事项当前的状态。
     *
     * @return 事项的状态（未开始、激活、已超时......）。
     */
    public WorkItemStatus getStatus() {
        return status;
    }

    /**
     * 设置事项的状态。
     *
     * @param status 事项状态的枚举值之一。
     */
    public void setStatus(WorkItemStatus status) {
        this.status = status;
        notifyObservers();
    }

    public void setInnerId(int innerId) {
        this.innerId = innerId;
        notifyObservers();
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
        notifyObservers();
    }

    public List<Integer> getSubToDoWorkItemInnerIdList() {
        return subToDoWorkItemInnerIdList;
    }

    public List<Integer> getPomodoroRecordInnerIdList() {
        return pomodoroRecordInnerIdList;
    }

    /**
     * 供类内部和JSON反序列化使用的构造器。
     *
     * @param subToDoWorkItemInnerIdList 子待办引用列表（通过innerId）。
     * @param pomodoroRecordInnerIdList  番茄钟引用列表（通过innerId）。
     */
    @JsonCreator
    private ToDoWorkItem(@JsonProperty("subToDoWorkItemInnerIdList") List<Integer> subToDoWorkItemInnerIdList,
                         @JsonProperty("pomodoroRecordInnerIdList") List<Integer> pomodoroRecordInnerIdList) {
        this.subToDoWorkItemInnerIdList = subToDoWorkItemInnerIdList;
        this.pomodoroRecordInnerIdList = pomodoroRecordInnerIdList;
        //保证列表的非null性。
        if(subToDoWorkItemInnerIdList == null) this.subToDoWorkItemInnerIdList = new ArrayList<>();
        if(pomodoroRecordInnerIdList == null) this.pomodoroRecordInnerIdList = new ArrayList<>();
    }

    /**
     * 构造一个待办事项对象。
     */
    public ToDoWorkItem() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 从Json字符串构造一个ToDoWorkItem对象。
     *
     * @param json json字符串。
     * @return 新构造的ToDoWorkItem对象，失败返回null。
     */
    public static ToDoWorkItem fromJsonString(String json) throws Exception {
        return JsonUtility.objectFromJsonString(json, ToDoWorkItem.class);
    }

    /**
     * 从Json字节数组构造一个ToDoWorkItem对象。
     *
     * @param bytes json字节数组。
     * @return 新构造的ToDoWorkItem对象，失败返回null。
     */
    public static ToDoWorkItem fromJsonBytes(byte[] bytes) throws Exception {
        return fromJsonString(new String(bytes));
    }


    /**
     * 从Json字节流中读取指定数量的字节，然后从字节数组构造一个ToDoWorkItem对象。
     *
     * @param stream         Json字节输入流。
     * @param expectedLength 预期的字节长度。如果该值小于等于0，则读取流的所有字节。
     * @return 新构造的ToDoWorkItem对象。失败返回null，并尝试回溯流数据到未调用此方法前的状态。
     */
    public static ToDoWorkItem fromJsonStream(InputStream stream, int expectedLength) throws Exception {
        return JsonUtility.objectFromInputStream(stream, expectedLength, ToDoWorkItem.class);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ToDoWorkItem item) {
            return this.innerId == item.getInnerId();
        }

        return false;
    }


    private final Vector<Observer<ToDoWorkItem>> observers = new Vector<>();
    private boolean notifySuppressed; //通知观察者是否被暂停。
    private boolean notifyRequired; //在暂停期间，是否有属性被更改。

    @Override
    public void addObserver(Observer<ToDoWorkItem> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ToDoWorkItem> observer) {
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
        notifySuppressed = false;
        if (notifyRequired)
            notifyObservers();
    }

    /**
     * 通知所有观察者属性发生变化。如果在暂停通知期间，则将notifyRequired设为true。
     */
    void notifyObservers() {
        if (!notifySuppressed) {
            notifyRequired = false;
            observers.forEach(o -> o.propertyChanged(this));
        } else notifyRequired = true;
    }
}
