package trans;

import java.net.InetSocketAddress;

/**
 * 后端服务器初始化信息。
 * @author 贾聪毅
 * @param address 服务器绑定的地址。
 * @param port 服务器绑定的端口。
 * @param maxConnection 允许的最大连接数（即backlog）。
 */
public record BackEndServerStartupInfo(String address, int port, int maxConnection) {
    /**
     * 获取该实例包含的InetSocketAddress对象。
     * @return InetSocketAddress对象。
     */
    InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(address, port);
    }
}
