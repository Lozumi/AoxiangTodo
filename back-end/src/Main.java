import shared.ToDoWorkItem;
import shared.WorkItemStatus;
import sys.AoXiangToDoListSystem;
import sys.RequestController;
import sys.RequestHandlerInfo;
import trans.*;
import unittest.HttpTest;
import unittest.SocketTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        BackEndHttpServer httpServer = new BackEndHttpServer(new BackEndHttpServerStartupInfo(20220, 12));
        httpServer.run();
        System.err.println("http服务器正在运行......");

        var controller = AoXiangToDoListSystem.getInstance().getSystemController();
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.CreateToDoWork, RequestController::processToDoWorkCreation));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.QueryToDoWork, RequestController::processQueryToDoWorkRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EnumerateToDoWorkList, RequestController::processEnumerateToDoWorkItemListRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.DeleteToDoWork, RequestController::processDeleteToDoWorkRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EditToDoWork, RequestController::processEditToDoWork));

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
        System.err.println(item.toJsonString());
        System.out.printf("添加待办事项结果：%s\n", httpTest.tryRequest(addItemRequestPacket).toJsonString());
       // System.out.printf("添加待办事项结果：%s\n", httpTest.tryRequest(addItemRequestPacket).toJsonString());

        RequestPacket enumerateRequestPacket = new RequestPacket();
        enumerateRequestPacket.setRequestType(RequestType.EnumerateToDoWorkList);
        System.out.printf("查询待办事项结果：%s\n", httpTest.tryRequest(enumerateRequestPacket).getContent());

        AoXiangToDoListSystem.getInstance().localSaveSystemData("D:/test/1.json");
    }

    static ResponsePacket enumerateToDoWorkItems(SocketTest test) {
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.EnumerateToDoWorkList);
        var queryResponse = test.tryRequest(packet);
        return queryResponse;
    }
}
