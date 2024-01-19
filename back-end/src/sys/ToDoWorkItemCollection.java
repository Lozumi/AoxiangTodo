package sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import notify.CollectionOperationType;
import notify.ToDoCollectionChangedNotificationArgs;
import shared.ListListener;
import shared.Listenable;
import shared.ToDoWorkItem;
import trans.NotificationPacket;
import trans.NotificationType;
import util.JsonUtility;

import javax.naming.OperationNotSupportedException;
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
        ToDoCollectionChangedNotificationArgs args = new ToDoCollectionChangedNotificationArgs();
        args.setOperationType(type);
        switch (type) {
            case ItemAdded -> args.setAddedItemInnerId(innerId);
            case ItemRemoved -> args.setRemovedItemInnerId(innerId);
            case ItemPropertyChanged -> args.setModifiedItemInnerId(innerId);
        }

        try {
            packet.setContent(JsonUtility.objectToJsonString(args));
            notifyController.distribute(packet);
        } catch (Exception e) {
            System.err.println("通知对象转换为JSON失败：" + e.getMessage());
        }
    }

    public ToDoWorkCollectionListener() {
        packet.setNotificationType(NotificationType.ToDoWorkCollectionChanged);
    }
}
