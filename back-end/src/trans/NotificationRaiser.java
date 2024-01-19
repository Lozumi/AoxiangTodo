package trans;

/**
 * 表示通知发送器。
 */
public interface NotificationRaiser {
    default void push(NotificationType type, String content, int port) {
        NotificationPacket packet = new NotificationPacket();
        packet.setContent(content);
        packet.setNotificationType(type);
        push(packet, port);
    }

    /**
     * 向指定端口推送通知。
     * @param packet 通知包。
     * @param port 端口。
     */
    void push(NotificationPacket packet, int port);
}
