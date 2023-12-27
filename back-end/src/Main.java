import shared.SharedConfigurations;
import shared.ToDoWorkItem;
import sys.AoXiangToDoListSystem;
import sys.RequestController;
import sys.RequestHandlerInfo;
import trans.*;
import unittest.SocketTest;
import util.JsonUtility;

import java.io.IOException;
import java.time.Instant;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
        ToDoWorkItem item = new ToDoWorkItem(0,Instant.now());
        System.out.println(item.toJsonString());
    }
}
