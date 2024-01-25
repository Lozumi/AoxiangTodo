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
    GetPomodoro,
    StartPomodoro,
    EndPomodoro,
    UserRegister,
    UserLogin,
    UserLogout,
    Synchronize,
    GetCurrentUser,
    ExitApplication,
    ModifyUserInfo,
    RegisterNotificationContext,
    UnregisterNotificationContext,
    GetNotification,
    ClearNotification,
    TrapConnectionForNotifications,
    SaveSystemData
}
