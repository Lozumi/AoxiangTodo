package trans;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 表示后端服务器。
 */
public class BackEndServer {
    boolean aborting = false;
    boolean disposed = false;
    BackEndServerStartupInfo startupInfo;
    ServerSocket serverSocket;
    Thread mainServerThread;
    ArrayList<BackEndServerThread> runningServerThreads = new ArrayList<>();

    public BackEndServer(BackEndServerStartupInfo startupInfo) {
        this.startupInfo = startupInfo;
    }

    /**
     * 绑定端口、开始监听。
     */
    public void run() throws IllegalStateException{
        if(mainServerThread != null && mainServerThread.isAlive()) {
            throw new IllegalStateException("后端服务器正在运行，调用 abort 来尝试使正在进行的任务退出。");
        }

        mainServerThread = new Thread(
                () -> {
                    try {
                        initialize();
                        listenLoop();
                    }catch (IOException exception)
                    {
                        System.err.printf("服务器主线程发生异常：%s\n",exception.getMessage());
                    }
                }
        );

        mainServerThread.start();
    }

    /**
     * 将所有线程标记为退出状态。
     */
    public void abort(){
        aborting = true;
        for (var backgroundServerThread : runningServerThreads){
            backgroundServerThread.abort();
        }
        disposed = true;
    }

    void initialize() throws IOException {
        serverSocket = new ServerSocket(startupInfo.port(), startupInfo.maxConnection());
        serverSocket.bind(startupInfo.getSocketAddress());
    }

    void listenLoop() throws IOException {
        while (!aborting){
            Socket clientSocket = serverSocket.accept();
            startServerThread(clientSocket);
        }
    }

    void startServerThread(Socket clientSocket) throws IOException{
        //针对clientSocket运行服务线程。
        var backgroundRunnable = new BackEndServerThread(clientSocket);
        Thread serveThread = new Thread(backgroundRunnable);
        runningServerThreads.add(backgroundRunnable);
        serveThread.start();
    }
}
