package unittest;

import trans.BackEndServerStartupInfo;
import trans.RequestPacket;
import trans.ResponsePacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketTest {

    BackEndServerStartupInfo serverInfo;
    Socket socket;
    DataOutputStream outputStream;
    DataInputStream inputStream;

    public SocketTest(BackEndServerStartupInfo info) {
        serverInfo = info;
    }

    public void connect() {
        socket = new Socket();
        try {
            socket.connect(serverInfo.getSocketAddress());
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.printf("SocketTest测试类连接异常：%s\n", e.getMessage());
        }
    }

    public ResponsePacket tryRequest(RequestPacket packet) {
        try {
            byte[] requestBytes = packet.toJsonBytes();
            outputStream.writeInt(requestBytes.length);
            outputStream.write(requestBytes);

            int responseLength = inputStream.readInt();
            return ResponsePacket.fromJsonStream(inputStream,responseLength);
        } catch (Exception exception) {
            System.err.printf("SocketTest测试类发送异常：%s\n", exception.getMessage());
            return null;
        }
    }
}
