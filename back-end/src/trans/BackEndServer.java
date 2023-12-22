package trans;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class BackEndServer {
    boolean aborting = false;
    BackEndServerStartupInfo startupInfo;
    ServerSocket serverSocket;

    public BackEndServer(BackEndServerStartupInfo startupInfo) {
        this.startupInfo = startupInfo;
    }

    public void run(){

    }

    public void abort(){
        aborting = true;
    }

    void initialize() throws IOException {
        serverSocket = new ServerSocket(startupInfo.port(), startupInfo.maxConnection());
        serverSocket.bind(startupInfo.getSocketAddress());
    }

    void listenLoop() throws IOException {
        while (!aborting){
            Socket clientSocket = serverSocket.accept();
        }
    }

    void serveLoop(Socket clientSocket) throws IOException{

    }
}
