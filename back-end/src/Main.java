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
        controller.registerRequestHandler(new RequestHandlerInfo(RequestType.UserLogin, RequestController::processUserLogin));

        System.out.println("test: server begins running");
        SocketTest test = new SocketTest(startupInfo);
        test.connect();
        var request = new RequestPacket();
        request.setRequestType(RequestType.UserLogin);
        var response = test.tryRequest(request);

        System.out.println(response.toJsonString());
    }
}
