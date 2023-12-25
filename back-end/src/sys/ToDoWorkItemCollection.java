package sys;

import shared.ToDoWorkItem;

import java.util.*;

public class ToDoWorkItemCollection extends Vector<ToDoWorkItem>{
    public int getNextAvailableInnerId()
    {
        return this.stream().max(Comparator.comparingInt(ToDoWorkItem::getInnerId)).get().getInnerId() + 1;
    }
}
