package sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import notify.CollectionOperationType;
import notify.SystemCollectionChangedNotificationArgs;
import shared.ListListener;
import shared.Listenable;
import shared.ToDoWorkItem;
import trans.NotificationPacket;
import trans.NotificationType;
import util.JsonUtility;

import java.util.*;

public class ToDoWorkItemCollection extends Vector<ToDoWorkItem> implements Listenable<ToDoWorkItem> {

    private final Vector<ListListener<ToDoWorkItem>> listeners = new Vector<>();

    @JsonIgnore
    public int getNextAvailableInnerId() {
        if (isEmpty())
            return 0; //序号从0开始
        return this.stream().max(Comparator.comparingInt(ToDoWorkItem::getInnerId)).get().getInnerId() + 1;
    }

    @Override
    public void addListener(ListListener<ToDoWorkItem> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ListListener<ToDoWorkItem> listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean add(ToDoWorkItem item) {
        listeners.forEach(l -> l.beforeAdd(item));
        boolean baseFlag = super.add(item);
        listeners.forEach(l -> l.afterAdd(item));
        System.out.println("添加了ID为" + item.getInnerId() + "的事项");
        //注册属性变化监听器
        if (baseFlag)
            item.addObserver(this::onToDoWorkItemPropertyChanged);
        return baseFlag;
    }

    @Override
    public boolean remove(Object obj) {
        if (obj instanceof ToDoWorkItem item) {
            listeners.forEach(l -> l.beforeRemove(item));
            boolean baseFlag = super.remove(obj);
            listeners.forEach(l -> l.afterRemove(item));
            System.out.println("移除了ID为" + item.getInnerId() + "的事项");
            if (baseFlag)
                item.removeObserver(this::onToDoWorkItemPropertyChanged);
            return baseFlag;
        }
        return false;
    }

    @Override
    public void add(int index, ToDoWorkItem item) {
        listeners.forEach(l -> l.beforeRemove(item));
        super.add(index, item);
        listeners.forEach(l -> l.afterRemove(item));
    }

    @Override
    public boolean addAll(Collection<? extends ToDoWorkItem> c) {
        c.forEach(e ->
                listeners.forEach(l -> l.beforeAdd(e))
        );
        boolean flag = super.addAll(c);
        c.forEach(e ->
                listeners.forEach(l -> l.afterAdd(e))
        );
        return flag;
    }

    @Override
    public synchronized ToDoWorkItem remove(int index) {
        var element = this.elementAt(index);
        listeners.forEach(l -> l.beforeRemove(element));
        super.remove(index);
        listeners.forEach(l -> l.afterRemove(element));
        return element;
    }

    @Override
    public synchronized void removeAllElements() {
        listeners.forEach(ListListener::beforeClear);
        super.removeAllElements();
        listeners.forEach(ListListener::afterClear);
    }

    /**
     * 警告：监听者将无法得知通过此方法导致的集合修改。
     */
    @Override
    @Deprecated(forRemoval = true)
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    /**
     * 通知所有监听者，元素的属性发生改变。
     *
     * @param item 元素。
     */
    void onToDoWorkItemPropertyChanged(ToDoWorkItem item) {
        listeners.forEach(l -> l.afterElementPropertyChange(item));
    }

    /**
     * 寻找innerId为指定值的ToDoWorkItem。
     * @param innerId 参数。
     * @return ToDoWorkItem对象，或null。
     */
    public ToDoWorkItem findFirst(int innerId) {
        var optional = this.stream().filter(t -> t.getInnerId() == innerId).findFirst();
        return optional.orElse(null);
    }

    public ToDoWorkItemCollection() {
        this.addListener(new ToDoWorkCollectionListener());
    }
}

class ToDoWorkCollectionListener implements ListListener<ToDoWorkItem> {

    private final NotificationPacket packet = new NotificationPacket();
    private final NotificationController notifyController = AoXiangToDoListSystem.getInstance().getNotificationController();

    @Override
    public void beforeAdd(ToDoWorkItem item) {
    }

    @Override
    public void afterAdd(ToDoWorkItem item) {
        post(CollectionOperationType.ItemAdded, item.getInnerId());
    }

    @Override
    public void beforeRemove(ToDoWorkItem item) {
    }

    @Override
    public void afterRemove(ToDoWorkItem item) {
        post(CollectionOperationType.ItemRemoved, item.getInnerId());
    }

    @Override
    public void afterElementPropertyChange(ToDoWorkItem item) {
        post(CollectionOperationType.ItemPropertyChanged, item.getInnerId());
    }

    @Override
    public void beforeClear() {

    }

    @Override
    public void afterClear() {
        post(CollectionOperationType.CollectionCleared, null);
    }

    void post(CollectionOperationType type, Integer innerId) {
        try {
            notifyController.distributeCollectionChanged(NotificationType.ToDoWorkCollectionChanged, type, innerId);
        } catch (Exception exception) {
            System.err.printf("推送待办事项集合变更通知(type=%s, innerId=%s) 时发生错误：%s\n", type, innerId, exception.getMessage());
        }
    }

    public ToDoWorkCollectionListener() {
        packet.setNotificationType(NotificationType.ToDoWorkCollectionChanged);
    }
}
