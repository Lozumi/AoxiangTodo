package trans;

import shared.SharedConfigurations;

import java.io.*;
import java.net.Socket;

public class BackEndServerThread implements Runnable {

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
     * Runs this operation.
     */
    @Override
    public void run(){
        while (true) {
            try {
                int requestLength = inputStream.readInt();
                RequestPacket requestPacket = RequestPacket.fromJsonStream(inputStream,requestLength);
            }catch (IOException ex)
            {
                System.err.printf("与客户端通讯时发生IO异常：%s\n",ex.getMessage());
                break;
            }
        }
    }

    public BackEndServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
    }
}
