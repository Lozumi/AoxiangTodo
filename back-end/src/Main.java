import shared.ToDoWorkItem;
import sys.AoXiangToDoListSystem;
import sys.RequestController;
import sys.RequestHandlerInfo;
import trans.*;
import unittest.SocketTest;

import java.io.IOException;
import java.time.Instant;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        BackEndServerStartupInfo startupInfo = new BackEndServerStartupInfo("127.0.0.1",20220,20);
        BackEndServer server = new BackEndServer(startupInfo);
        server.run();

        var controller = AoXiangToDoListSystem.getInstance().getSystemController();
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.CreateToDoWork, RequestController::processToDoWorkCreation));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.QueryToDoWork,RequestController::processQueryToDoWorkRequest));

        System.out.println("test: server begins running");
        SocketTest test = new SocketTest(startupInfo);
        test.connect();
        var request = new RequestPacket();
        request.setRequestType(RequestType.CreateToDoWork);
        //创建测试待办事项
        ToDoWorkItem item = new ToDoWorkItem();
        item.setTitle("测试标题");
        item.setLayer(1);
        item.setSubtitle("测试副标题");
        item.setDescription("测试描述");
        request.setContent(item.toJsonString());
        var response = test.tryRequest(request);

        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.QueryToDoWork);
        packet.setContent(response.getContent());
        var queryResponse = test.tryRequest(packet);
        System.out.println(response.toJsonString());
        System.out.println(queryResponse.toJsonString());
    }
}
