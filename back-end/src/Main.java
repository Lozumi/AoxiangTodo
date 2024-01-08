import shared.Pomodoro;
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
    public static void main(String[] args) throws IOException, InterruptedException {
        Pomodoro pomodoro = new Pomodoro();
        pomodoro.setWorkTime(1);
        pomodoro.setRestTime(1);
        pomodoro.startPomodoro();
        Thread.sleep(2*1000*60);
        System.out.println(pomodoro.getPomodoroRecord());
    }

    static ResponsePacket enumerateToDoWorkItems(SocketTest test) {
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.EnumerateToDoWorkList);
        var queryResponse = test.tryRequest(packet);
        return queryResponse;
    }
}
