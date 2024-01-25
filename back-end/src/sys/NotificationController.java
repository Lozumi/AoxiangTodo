package sys;

import exception.ContextNotRegisteredException;
import notify.CollectionOperationType;
import notify.PomodoroStatusChangedNotificationArgs;
import notify.SystemCollectionChangedNotificationArgs;
import shared.NotificationContext;
import shared.PomodoroStatus;
import trans.NotificationPacket;
import trans.NotificationType;
import util.JsonUtility;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 提供系统的通知控制功能。
 */
public class NotificationController {
    private final Vector<NotificationContext> contexts = new Vector<>();
    /**
     * 这里存储了上下文和正在出队线程的映射。
     */
    private final Hashtable<String, Thread> contextDequeuingThreadPairs = new Hashtable<>();
    private final Hashtable<String, BlockingQueue<NotificationPacket>> contextNameNotificationQueuePairs = new Hashtable<>();

    public synchronized void registerContext(NotificationContext context) throws Exception {
        if (!contexts.contains(context)) {
            contexts.add(context);
            ArrayBlockingQueue<NotificationPacket> notificationQueue = new ArrayBlockingQueue<>(context.getNotificationQueueMaxLength());
            contextNameNotificationQueuePairs.put(context.getContextName(), notificationQueue);
        } else throw new Exception(String.format("已经注册过名称为 %s 的通知上下文。", context.getContextName()));
    }

    public synchronized void unregisterContext(String contextName) throws Exception {
        if (contextNameNotificationQueuePairs.containsKey(contextName)) {
            contexts.removeIf(c -> c.getContextName().equals(contextName));
            contextNameNotificationQueuePairs.remove(contextName);
        } else throw new Exception("尚未注册过名称为 " + contextName + " 的通知上下文。");
    }

    public boolean isContextRegistered(String context) {
        return contextNameNotificationQueuePairs.containsKey(context);
    }

    /**
     * 从通知队列拉取头部的通知。如果没有可用元素，则阻塞直到可用。
     *
     * @param context 通知上下文。
     * @return 通知包。
     * @throws ContextNotRegisteredException 当指定上下文不存在时抛出。
     * @throws InterruptedException          当等待通知队列过程被中断时抛出。
     */
    public NotificationPacket pollNotificationForContext(String context) throws ContextNotRegisteredException, InterruptedException {
        if (!isContextRegistered(context))
            throw new ContextNotRegisteredException(String.format("尚未注册过名称为 %s 的通知上下文。", context));
        var queue = contextNameNotificationQueuePairs.get(context);
        var currentThread = Thread.currentThread();
        var currentThreadId = currentThread.threadId();
        var dequeuingThread = contextDequeuingThreadPairs.get(context); //获取已经正在申请出队的线程。
        contextDequeuingThreadPairs.put(context, currentThread); //更新上下文和出列线程对。

        if (dequeuingThread != null && dequeuingThread.threadId() != currentThreadId) {
            dequeuingThread.interrupt();         //打断之前的出队线程，使用当前新的线程替代
            System.out.printf("在上下文%s打断了线程[%s]\n", context, dequeuingThread.threadId());
        }
        NotificationPacket notification = queue.take();
        System.out.printf("通知在%s出列，内容为%s\n", context, notification.getContent());
        return notification;
    }

    /**
     * 将指定通知添加到所有已注册的上下文中。
     *
     * @param type    通知类型。
     * @param content 通知内容。
     */
    public void distribute(NotificationType type, String content) {
        NotificationPacket packet = new NotificationPacket();
        packet.setNotificationType(type);
        packet.setContent(content);
        distribute(packet);
    }

    /**
     * 将集合变更通知分配到所有已注册的上下文中。
     *
     * @param type          通知类型。
     * @param operationType 集合操作类型。
     * @param innerId       变更的项目id。
     * @throws Exception 当参数无法转换为JSON时抛出。
     */
    public void distributeCollectionChanged(NotificationType type, CollectionOperationType operationType, Integer innerId) throws Exception {
        SystemCollectionChangedNotificationArgs args = new SystemCollectionChangedNotificationArgs();
        args.setOperationType(operationType);
        switch (operationType) {
            case ItemAdded -> args.setAddedItemInnerId(innerId);
            case ItemRemoved -> args.setRemovedItemInnerId(innerId);
            case ItemPropertyChanged -> args.setModifiedItemInnerId(innerId);
        }
        distribute(type, JsonUtility.objectToJsonString(args));
    }

    public void distributePomodoroStatusChanged(PomodoroStatus newStatus) throws Exception {
        PomodoroStatusChangedNotificationArgs args = new PomodoroStatusChangedNotificationArgs();
        args.setNewStatus(newStatus);
        distribute(NotificationType.PomodoroStatusChanged, JsonUtility.objectToJsonString(args));
    }

    /**
     * 为每个已注册的上下文通知队列推送通知。如果队列已满，则抛弃最早的通知。
     *
     * @param packet 要推送的消息包。
     */
    private void distribute(NotificationPacket packet) {
        contextNameNotificationQueuePairs.values().forEach(
                queue -> {
                    synchronized (queue) {
                        if (queue.remainingCapacity() == 0)
                            queue.poll();
                        boolean addSuccess = queue.offer(packet);
                        if (addSuccess) {
                            System.out.printf("通知入列，内容为%s\n", packet.getContent());
                        }
                    }
                }
        );
    }
}
