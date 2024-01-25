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
        var token = controller.registerRequestHandler(new RequestHandlerInfo(RequestType.CreateToDoWork, RequestController::processToDoWorkCreation));
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

        var notifyController = AoXiangToDoListSystem.getInstance().getNotificationController();


        HttpTest httpTest = new HttpTest("localhost:20220");
        ToDoWorkItem item = new ToDoWorkItem();
        item.setTitle("测试待办事项");
        item.setStartTime(Instant.now().plusSeconds(0));
        item.setDeadLine(Instant.now().plusSeconds(3600 * 48));
        item.setSubtitle("下班下班");
        item.setImportancePriority(5);
        item.setEmergencyPriority(10);
        item.setDescription("总之就是要保证前后端程序不论先后启动均能正常工作，待办事项的更改能及时通知到前端。");
        RequestPacket packet = new RequestPacket();
        packet.setContent(item.toJsonString());
        packet.setRequestType(RequestType.CreateToDoWork);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequest(packet);
        httpTest.tryRequestEnumeration();
        httpTest.tryRequestQueryToDoWorkItem(0);

//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserName("你干嘛");
//        userInfo.setAccount("this");
//        userInfo.setPassword("Rrawgb773@");
//        httpTest.tryRequestUserRegister(userInfo);
//        httpTest.tryRequestUserLogin(userInfo);
        AoXiangToDoListSystem.getInstance().localSaveSystemData("D:/test/1.json");
    }
}
