import shared.ToDoWorkItem;
import shared.UserInfo;
import shared.WorkItemStatus;
import sys.AoXiangToDoListSystem;
import sys.RequestController;
import sys.RequestHandlerInfo;
import trans.*;
import unittest.HttpTest;
import unittest.SocketTest;
import user.User;
import util.Encrypt;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        AoXiangToDoListSystem.getInstance().runHttpServer(new BackEndHttpServerStartupInfo(20220,12));

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
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.GetCurrentUser, RequestController::processGetCurrentUser));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.Synchronize, RequestController::processSynchronization));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.ExitApplication, RequestController::processExitApplication));
        //controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UserRegister))

        HttpTest httpTest = new HttpTest("localhost:20220");

        RequestPacket addItemRequestPacket = new RequestPacket();
        ToDoWorkItem item = new ToDoWorkItem();
        item.setStartTime(Instant.now().plusSeconds(60));
        item.setDeadLine(Instant.now().plusSeconds(6000));
        item.setTitle("玩原神");
        item.setSubtitle("玩（）玩的");
        item.setStatus(WorkItemStatus.Activated);
        item.setImportancePriority(10);
        item.setEmergencyPriority(10);
        String itemJson = item.toJsonString();
        addItemRequestPacket.setContent(itemJson);
        addItemRequestPacket.setRequestType(RequestType.CreateToDoWork);
        String st = addItemRequestPacket.toJsonString();
        System.err.println(item.toJsonString());
        System.out.printf("添加待办事项结果：%s\n", httpTest.tryRequest(addItemRequestPacket).toJsonString());
         System.out.printf("添加待办事项结果：%s\n", httpTest.tryRequest(addItemRequestPacket).toJsonString());

        RequestPacket enumerateRequestPacket = new RequestPacket();
        enumerateRequestPacket.setRequestType(RequestType.EnumerateToDoWorkList);
        System.out.printf("查询待办事项结果：%s\n", httpTest.tryRequest(enumerateRequestPacket).getContent());
        UserInfo info = new UserInfo();
        info.setAccount("uzi");
        info.setPassword("uzi94yyds!");
        info.setUserName("澡子哥");
        httpTest.tryRequestUserRegister(info);
        httpTest.tryRequestUserLogin(info);

        httpTest.tryRequestSynchronize();
//        httpTest.tryRequestQueryToDoWorkItem(0);
//        httpTest.tryRequestStartPomodoro(0);
//
//        Thread.sleep(5000);
//        httpTest.tryRequestExit();
    }

    static ResponsePacket enumerateToDoWorkItems(SocketTest test) {
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.EnumerateToDoWorkList);
        var queryResponse = test.tryRequest(packet);
        return queryResponse;
    }
}
