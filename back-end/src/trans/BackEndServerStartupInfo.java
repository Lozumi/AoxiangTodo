package trans;

import java.net.InetSocketAddress;

public record BackEndServerStartupInfo(String address, int port, int maxConnection) {
    InetSocketAddress getSocketAddress() {
        return new InetSocketAddress(address, port);
    }
}
