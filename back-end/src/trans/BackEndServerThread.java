package trans;

import shared.SharedConfigurations;

import java.io.*;
import java.net.Socket;

public class BackEndServerThread implements Runnable {

    Socket clientSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    boolean aborting = false;

    public void abort() {
        aborting = true;
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
