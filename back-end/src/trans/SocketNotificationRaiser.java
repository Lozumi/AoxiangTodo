package trans;

import util.JsonUtility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

public class SocketNotificationRaiser implements NotificationRaiser {
    final HashMap<Integer, Socket> portSocketPairs = new HashMap<>(); //表示一个端口和套接字连接的映射
    final HashMap<Integer, NotificationQueue> portNotificationPairs = new HashMap<>(); //表示一个端口和通知队列的映射。

    @Override

    public void push(NotificationPacket packet, int port) {
        synchronized (portNotificationPairs) {
            if (!portNotificationPairs.containsKey(port))
                portNotificationPairs.put(port, new NotificationQueue()); //向字典中添加通知队列对象。
            portNotificationPairs.get(port).push(packet); //向通知队列中添加通知消息。
        }

        synchronized (portSocketPairs) {
            if (!portSocketPairs.containsKey(port)) {
                Socket socket = new Socket();
                portSocketPairs.put(port, socket);
                tryMakeConnection(socket, port);
            }
        }

    }


    /**
     * 尝试在单独线程将指定套接字连接到指定端口。如果失败，则将套接字从字典中移除。
     *
     * @param socket 套接字。
     * @param port   端口。
     */
    void tryMakeConnection(Socket socket, int port) {
        new Thread(() ->
        {
            try {
                socket.connect(new InetSocketAddress("127.0.0.1", port));
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream()); //获取套接字输出流

                NotificationQueue thisQueue;
                synchronized (portNotificationPairs) {
                    thisQueue = portNotificationPairs.get(port);
                }

                while (true) {
                    var notification = thisQueue.waitNext(); //等待队列中的下一个通知
                    var jsonBytes = JsonUtility.objectToJsonBytes(notification); //将通知对象转换为字节数组
                    outputStream.writeInt(jsonBytes.length); //发送字节数组长度
                    outputStream.write(jsonBytes); //发送字节数组
                }

            } catch (Exception exception) {
                System.err.printf("在与端口 %s 的套接字通讯时发生异常：%s\n", port, exception.getMessage());
            } finally {
                //在这里处理套接字结束时的操作，从字典中移除相关对象
                synchronized (portNotificationPairs) {
                    portNotificationPairs.remove(port);
                }
                synchronized (portSocketPairs) {
                    var socketObj = portSocketPairs.remove(port);
                    try {
                        socketObj.close();
                    } catch (IOException e) {
                        System.err.printf("尝试关闭与端口 %s 的套接字连接时发生异常：%s\n", port, e.getMessage());
                    }
                }
            }
        }
        ).start();
    }
}
