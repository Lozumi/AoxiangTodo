package trans;

import com.fasterxml.jackson.databind.annotation.EnumNaming;


public enum RequestType {
    None,
    EnumerateToDoWorkList,
    QueryToDoWork,
    CreateToDoWork,
    EditToDoWork,
    DeleteToDoWork,
    EditPomodoro,
    StartPomodoro,
    EndPomodoro,
    UserRegister,
    UserLogin,
    UserLogout,
    Synchronize,
    ToDoWorkItemNotification,
    GetCurrentUser,
    ExitApplication
}
