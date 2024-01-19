package shared;

import java.util.Objects;

/**
 * 注册通知回调时使用的信息。
 */
public class NotificationRegisterInfo {
    private NetworkProtocol protocol;
    private int port;
    public NetworkProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(NetworkProtocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NotificationRegisterInfo that = (NotificationRegisterInfo) object;
        return port == that.port;
    }

    @Override
    public int hashCode() {
        return Objects.hash(port);
    }
}
