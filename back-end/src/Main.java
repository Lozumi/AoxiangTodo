import shared.NetworkProtocol;
import shared.ToDoWorkItem;
import sys.AoXiangToDoListSystem;
import sys.RequestController;
import sys.RequestHandlerInfo;
import trans.*;
import unittest.HttpTest;

import java.io.IOException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        AoXiangToDoListSystem.getInstance().runHttpServer(new BackEndHttpServerStartupInfo(20220, 12));
        AoXiangToDoListSystem.getInstance().runSocketServer(new BackEndServerStartupInfo("127.0.0.1", 20221, 12));

        var controller = AoXiangToDoListSystem.getInstance().getSystemController();
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.CreateToDoWork, RequestController::processToDoWorkCreation));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.QueryToDoWork, RequestController::processQueryToDoWorkRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EnumerateToDoWorkList, RequestController::processEnumerateToDoWorkItemListRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.DeleteToDoWork, RequestController::processDeleteToDoWorkRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EditToDoWork, RequestController::processEditToDoWork));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EditPomodoro, RequestController::processEditPomodoro));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.StartPomodoro, RequestController::processStartPomodoro));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EndPomodoro, RequestController::processEndPomodoro));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UserRegister, RequestController::processUserRegister));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UserLogin, RequestController::processUserLogin));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UserLogout, RequestController::processUserLogout));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.GetCurrentUser, RequestController::processGetCurrentUser_FullInfo));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.Synchronize, RequestController::processSynchronization));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.ExitApplication, RequestController::processExitApplication));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.ModifyUserInfo, RequestController::processUserModifyInformation));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.RegisterNotificationContext, RequestController::processRegisterNotificationContext));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UnregisterNotificationContext, RequestController::processUnregisterNotificationContext));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.GetNotification, RequestController::processGetNotification));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.GetPomodoro, RequestController::processGetPomodoro));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.SaveSystemData, RequestController::processSaveSystemData));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EnumeratePomodoroRecords, RequestController::processEnumeratePomodoroRecordRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.QueryPomodoroRecord, RequestController::processQueryPomodoroRecord));

        var notifyController = AoXiangToDoListSystem.getInstance().getNotificationController();
    }
}
