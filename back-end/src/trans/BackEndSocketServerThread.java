package trans;

import sys.AoXiangToDoListSystem;

import java.io.*;
import java.net.Socket;

public class BackEndSocketServerThread implements Runnable {

    Socket clientSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    boolean aborting = false;

    /**
     * 将线程标记为退出状态并关闭所有套接字输入输出流。
     */
    public void abort() {
        aborting = true;
        inputStream = null;
        outputStream = null;
        try {
            clientSocket.shutdownInput();
            clientSocket.shutdownOutput();
        }
        catch (IOException exception)
        {
            System.err.printf("尝试关闭服务器时发生下述异常：%s\n",exception.getMessage());
        }
    }

    /**
     * 运行该后端服务线程。
     */
    @Override
    public void run(){
        System.out.println("[BackEndServerThread.run] 正在新的线程上建立套接字连接，线程ID为" + Thread.currentThread().threadId());
        while (true) {
            try {
                int requestLength = inputStream.readInt();
                RequestPacket requestPacket = RequestPacket.fromJsonStream(inputStream,requestLength);

                ResponsePacket responsePacket;
                responsePacket = AoXiangToDoListSystem.getInstance().getSystemController().invokeRequestHandler(requestPacket,"default");
                byte[] responseBytes = responsePacket.toJsonBytes();

                //发送响应
                int responseLength = responseBytes.length;
                outputStream.writeInt(responseLength);
                outputStream.write(responseBytes);
            }catch (Exception ex)
            {
                System.err.printf("[BackEndServerThread.run]与客户端通讯时发生异常：%s\n",ex.getMessage());
                break;
            }
        }
    }

    public BackEndSocketServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
    }
}
