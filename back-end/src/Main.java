import shared.ToDoWorkItem;
import trans.BackEndServer;
import trans.BackEndServerStartupInfo;
import trans.RequestPacket;
import unittest.SocketTest;

import java.io.IOException;
import java.time.Instant;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        BackEndServerStartupInfo startupInfo = new BackEndServerStartupInfo("127.0.0.1",8080,20);
        BackEndServer server = new BackEndServer(startupInfo);
        server.run();

        System.out.println("test: server begins running");
        SocketTest test = new SocketTest(startupInfo);
        test.connect();
        var response = test.tryRequest(new RequestPacket());

    }
}
