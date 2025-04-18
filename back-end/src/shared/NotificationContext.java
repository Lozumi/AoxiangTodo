package shared;

import java.util.Objects;

/**
 * 注册通知回调时使用的信息。
 */
public class NotificationContext {
    private String contextName;
    private int notificationQueueMaxLength;

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public int getNotificationQueueMaxLength() {
        return notificationQueueMaxLength;
    }

    public void setNotificationQueueMaxLength(int notificationQueueMaxLength) {
        this.notificationQueueMaxLength = notificationQueueMaxLength;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NotificationContext that = (NotificationContext) object;
        return Objects.equals(contextName, that.contextName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextName);
    }
}
