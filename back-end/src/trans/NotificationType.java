package trans;

public enum NotificationType {

    /**
     * 这是向每一个通知队列发送的第一个通知。接收到此通知时，前端应当执行 依赖于通知的对象 的初始化操作。
     * 例如，前端的待办事项集合利用后端待办事项更改通知来更新，那么此时就应该重新获取这些项目。
     */
    NotificationContextBuilt,
    /**
     * 表示待办事项集合的元素添加、移除、清空或属性被修改
     */
    ToDoWorkCollectionChanged,
    PomodoroRecordCollectionChanged,
    PomodoroStatusChanged
}
