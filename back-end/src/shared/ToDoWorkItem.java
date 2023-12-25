package shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.JsonUtility;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 贾聪毅
 * 待办事项。
 */
public class ToDoWorkItem implements JsonConvertable {
    //属性定义
    int innerId, importancePriority, emergencyPriority;
    String title, subtitle, description;
    Instant createTime, startTime, deadLine;
    WorkItemStatus Status;
    List<ToDoWorkItem> subToDoWorkItems;
    List<PomodoroRecord> pomodoroRecords;

    /**
     * 获取子待办事项列表。
     *
     * @return 子待办事项列表，保证该列表始终不为 null。
     */
    public List<ToDoWorkItem> getSubToDoWorkItems() {
        return subToDoWorkItems;
    }

    /**
     * 获取与该事件关联的番茄钟记录。
     *
     * @return 番茄钟记录。保证该列表始终不为 null。
     */

    public List<PomodoroRecord> getPomodoroRecords() {
        return pomodoroRecords;
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
    }

    /**
     * 获取事项当前的状态。
     *
     * @return 事项的状态（未开始、激活、已超时......）。
     */
    public WorkItemStatus getStatus() {
        return Status;
    }

    /**
     * 设置事项的状态。
     *
     * @param status 事项状态的枚举值之一。
     */
    public void setStatus(WorkItemStatus status) {
        Status = status;
    }

    /**
     * 供类内部和JSON反序列化使用的构造器。
     *
     * @param innerId          内部Id。
     * @param createTime       事项创建时间。
     * @param subToDoWorkItems 子待办列表。
     * @param pomodoroRecords  番茄钟记录列表。
     */
    @JsonCreator
    private ToDoWorkItem(@JsonProperty("innerId") int innerId,
                         @JsonProperty("createTime") Instant createTime,
                         @JsonProperty("subToDoWorkItems") List<ToDoWorkItem> subToDoWorkItems,
                         @JsonProperty("pomodoroRecords") List<PomodoroRecord> pomodoroRecords) {
        this.innerId = innerId;
        this.createTime = createTime;
        this.subToDoWorkItems = subToDoWorkItems;
        this.pomodoroRecords = pomodoroRecords;
    }

    /**
     * 构造一个待办事项对象。
     *
     * @param innerId    事项内部Id，之后不可变。
     * @param createTime 事项创建时间，之后不可变。
     */
    public ToDoWorkItem(int innerId, Instant createTime) {
        this(innerId, createTime, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 从Json字符串构造一个ToDoWorkItem对象。
     *
     * @param json json字符串。
     * @return 新构造的ToDoWorkItem对象，失败返回null。
     */
    public static ToDoWorkItem fromJsonString(String json) {
        return JsonUtility.objectFromJsonString(json, ToDoWorkItem.class);
    }

    /**
     * 从Json字节数组构造一个ToDoWorkItem对象。
     *
     * @param bytes json字节数组。
     * @return 新构造的ToDoWorkItem对象，失败返回null。
     */
    public static ToDoWorkItem fromJsonBytes(byte[] bytes) {
        return fromJsonString(new String(bytes));
    }


    /**
     * 从Json字节流中读取指定数量的字节，然后从字节数组构造一个ToDoWorkItem对象。
     *
     * @param stream  Json字节输入流。
     * @param expectedLength 预期的字节长度。如果该值小于等于0，则读取流的所有字节。
     * @return 新构造的ToDoWorkItem对象。失败返回null，并尝试回溯流数据到未调用此方法前的状态。
     */
    public static ToDoWorkItem fromJsonStream(InputStream stream, int expectedLength) {
        return JsonUtility.objectFromInputStream(stream,expectedLength, ToDoWorkItem.class);
    }
}
