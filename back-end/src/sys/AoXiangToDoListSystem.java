package sys;

import shared.PomodoroRecord;
import user.User;

public class AoXiangToDoListSystem {
    User currentUser;
    static AoXiangToDoListSystem system;
    PomodoroRecordCollection pomodoroRecordsCollection = new PomodoroRecordCollection();
    ToDoWorkItemCollection toDoWorkItemCollection = new ToDoWorkItemCollection();

    public static synchronized AoXiangToDoListSystem getInstance(){
        if(system == null) system = new AoXiangToDoListSystem();
        return system;
    }

    SystemController systemController = new SystemController();

    public PomodoroRecordCollection getPomodoroRecordsCollection() {
        return pomodoroRecordsCollection;
    }

    public  SystemController getSystemController(){return systemController;}
    public ToDoWorkItemCollection getToDoWorkItemCollection() {
        return toDoWorkItemCollection;
    }
}
