package trans;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 提供通知队列模型。
 */
class NotificationQueue {
    private final ConcurrentLinkedQueue<NotificationPacket> notificationPackets = new ConcurrentLinkedQueue<>();

    /**
     * 向队列中添加通知。
     * @param packet 通知。
     */
    public void push(NotificationPacket packet) {
        synchronized (notificationPackets) {
            notificationPackets.add(packet);
            notificationPackets.notify();
        }
    }

    /**
     * 等待下一个可用的通知并返回。
     * @return 可用的通知。
     * @throws InterruptedException 等待中断时抛出。
     */
    public NotificationPacket waitNext() throws InterruptedException {
        synchronized (notificationPackets) {
            if (!notificationPackets.isEmpty()) {
                return notificationPackets.poll();
            }
            notificationPackets.wait();
            return notificationPackets.poll();
        }
    }
}