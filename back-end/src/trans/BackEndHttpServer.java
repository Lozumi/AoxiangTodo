package trans;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackEndHttpServer {
    BackEndHttpServerStartupInfo startupInfo;
    HttpServer httpServer;
    ExecutorService executorService;
    public void run() {
        try {
            initializeHttpServer();
        } catch (IOException ex) {
            System.err.println("创建后端Http服务器错误：" + ex.getMessage());
        }
    }

    private void initializeHttpServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(startupInfo.port), 0);
        executorService = Executors.newFixedThreadPool(startupInfo.maxThreadCount);
        httpServer.setExecutor(executorService);
        httpServer.createContext("/",new BackEndHttpHandler()); //注册根处理器
        httpServer.start();
    }
    public BackEndHttpServer(BackEndHttpServerStartupInfo startupInfo)
    {
        this.startupInfo = startupInfo;
    }
}