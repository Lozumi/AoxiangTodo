import shared.Pomodoro;
import shared.PomodoroRecord;
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
        Pomodoro pomodoro = new Pomodoro();
        pomodoro.setWorkTime(12);
        pomodoro.setRestTime(2);
        pomodoro.startPomodoro();
        pomodoro.endPomodoro();
        System.out.println(pomodoro.getPomodoroRecord().getCompletedCycleCount());
    }

    static ResponsePacket enumerateToDoWorkItems(SocketTest test)
    {
        RequestPacket packet = new RequestPacket();
        packet.setRequestType(RequestType.EnumerateToDoWorkList);
        var queryResponse = test.tryRequest(packet);
        return queryResponse;
    }
}
