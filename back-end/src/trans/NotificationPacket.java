package trans;

/**
 * 通知传输包。
 */
public class NotificationPacket {
    private NotificationType notificationType;
    private String content;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
