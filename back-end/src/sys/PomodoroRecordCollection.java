package sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import notify.CollectionOperationType;
import shared.*;
import trans.NotificationType;

import java.util.*;

/**
 * 保存番茄钟记录的集合类。
 */
public class PomodoroRecordCollection extends Vector<PomodoroRecord> implements Listenable<PomodoroRecord> {
      private final Vector<ListListener<PomodoroRecord>> listeners = new Vector<>();

    /**
     * 获取下一个可用的番茄钟记录ID。
     *
     * @return ID。
     */
    @JsonIgnore
    public int getAvailableID() {
        if (this.isEmpty()) {
            return 0;
        }
        return this.stream().max(Comparator.comparingInt(PomodoroRecord::getInnerId)).get().getInnerId() + 1;
    }

    @Override
    public void addListener(ListListener<PomodoroRecord> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ListListener<PomodoroRecord> listener) {
        listeners.remove(listener);
    }
    @Override
    public boolean add(PomodoroRecord item) {
        listeners.forEach(l -> l.beforeAdd(item));
        boolean baseFlag = super.add(item);
        listeners.forEach(l -> l.afterAdd(item));
        System.out.println("添加了ID为" + item.getInnerId() + "的番茄钟记录");
        //注册属性变化监听器
        if (baseFlag)
            item.addObserver(this::onPomodoroRecordPropertyChanged);
        return baseFlag;
    }

    @Override
    public boolean remove(Object obj) {
        if (obj instanceof PomodoroRecord item) {
            listeners.forEach(l -> l.beforeRemove(item));
            boolean baseFlag = super.remove(obj);
            listeners.forEach(l -> l.afterRemove(item));
            System.out.println("移除了ID为" + item.getInnerId() + "的番茄钟记录");
            if (baseFlag)
                item.removeObserver(this::onPomodoroRecordPropertyChanged);
            return baseFlag;
        }
        return false;
    }

    void onPomodoroRecordPropertyChanged(PomodoroRecord item){
        listeners.forEach(l -> l.afterElementPropertyChange(item));
    }

    @Override
    public void add(int index, PomodoroRecord item) {
        listeners.forEach(l -> l.beforeRemove(item));
        super.add(index, item);
        listeners.forEach(l -> l.afterRemove(item));
    }

    @Override
    public boolean addAll(Collection<? extends PomodoroRecord> c) {
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
    public synchronized PomodoroRecord remove(int index) {
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

    public PomodoroRecordCollection(){
        this.listeners.add(new PomodoroRecordCollectionListener());
    }
}

/**
 * 内部类，提供了检测番茄钟记录集合变更的功能，并对接系统通知接口。
 */
class PomodoroRecordCollectionListener implements ListListener<PomodoroRecord> {
    private final NotificationController notifyController = AoXiangToDoListSystem.getInstance().getNotificationController();

    @Override
    public void beforeAdd(PomodoroRecord item) {
    }

    @Override
    public void afterAdd(PomodoroRecord item) {
        post(CollectionOperationType.ItemAdded, item.getInnerId());
    }

    @Override
    public void beforeRemove(PomodoroRecord item) {
    }

    @Override
    public void afterRemove(PomodoroRecord item) {
        post(CollectionOperationType.ItemRemoved, item.getInnerId());
    }

    @Override
    public void afterElementPropertyChange(PomodoroRecord item) {
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
            notifyController.distributeCollectionChanged(NotificationType.PomodoroRecordCollectionChanged,type,innerId);
        } catch (Exception e) {
            System.err.printf("推送番茄钟记录集合变更通知(type=%s, innerId=%s) 时发生错误：%s\n", type, innerId, e.getMessage());
        }
    }

    public PomodoroRecordCollectionListener() {
    }
}
