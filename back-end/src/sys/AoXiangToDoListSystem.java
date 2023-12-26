package sys;

import shared.PomodoroRecord;
import user.User;

public class AoXiangToDoListSystem {
    User currentUser;
    PomodoroRecordCollection pomodoroRecordsCollection = new PomodoroRecordCollection();
    ToDoWorkItemCollection toDoWorkItemCollection = new ToDoWorkItemCollection();

    public PomodoroRecordCollection getPomodoroRecordsCollection() {
        return pomodoroRecordsCollection;
    }

    public ToDoWorkItemCollection getToDoWorkItemCollection() {
        return toDoWorkItemCollection;
    }
}
