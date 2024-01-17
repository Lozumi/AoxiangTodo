package sys;

import shared.ListListener;
import shared.Listenable;
import shared.ToDoWorkItem;

import java.util.*;

public class ToDoWorkItemCollection extends Vector<ToDoWorkItem> implements Listenable<ToDoWorkItem> {

    private final Vector<ListListener<ToDoWorkItem>> listeners = new Vector<>();

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

    void onToDoWorkItemPropertyChanged(ToDoWorkItem item) {
        listeners.forEach(l -> l.afterElementPropertyChange(item));
    }
}
