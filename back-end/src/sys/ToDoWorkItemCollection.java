package sys;

import shared.ToDoWorkItem;

import java.util.*;

public class ToDoWorkItemCollection extends Vector<ToDoWorkItem>{
    public int getNextAvailableInnerId()
    {
        if(isEmpty())
            return 0; //序号从0开始
        return this.stream().max(Comparator.comparingInt(ToDoWorkItem::getInnerId)).get().getInnerId() + 1;
    }
}
