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
        BackEndHttpServer httpServer = new BackEndHttpServer(new BackEndHttpServerStartupInfo(20220,12));
        httpServer.run();

        var controller = AoXiangToDoListSystem.getInstance().getSystemController();
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.CreateToDoWork, RequestController::processToDoWorkCreation));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.QueryToDoWork,RequestController::processQueryToDoWorkRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.EnumerateToDoWorkList,RequestController::processEnumerateToDoWorkItemListRequest));
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.DeleteToDoWork,RequestController::processDeleteToDoWorkRequest));

//        System.out.println("test: server begins running");
//        SocketTest test = new SocketTest(startupInfo);
//        test.connect();
//
//        var request = new RequestPacket();
//        request.setRequestType(RequestType.CreateToDoWork);
//        //创建测试待办事项
//        ToDoWorkItem item = new ToDoWorkItem();
//        item.setTitle("测试标题");
//        item.setLayer(1);
//        item.setSubtitle("测试副标题");
//        item.setDescription("测试描述");
//        request.setContent(item.toJsonString());
//        System.out.printf("请求内容为：%s\n,长度为%s\n",request.toJsonString(),request.toJsonString().length());
//        //添加三个待办事项
//        var response = test.tryRequest(request);
//        test.tryRequest(request);
//        test.tryRequest(request);
//
//        //删除前的所有项目
//        var queryResponse = enumerateToDoWorkItems(test);
//        System.out.println(queryResponse.toJsonString());
//
//        //删除第一个事项
//        var deleteRequest = new RequestPacket();
//        deleteRequest.setRequestType(RequestType.DeleteToDoWork);
//        deleteRequest.setContent(response.getContent()); //使用第一个事项的innerId
//        test.tryRequest(deleteRequest);
//
//        //删除后的所有项目
//        queryResponse = enumerateToDoWorkItems(test);
//        System.out.println(queryResponse.toJsonString());
    }

    static ResponsePacket enumerateToDoWorkItems(SocketTest test)
    {
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.EnumerateToDoWorkList);
        var queryResponse = test.tryRequest(packet);
        return queryResponse;
    }
}
