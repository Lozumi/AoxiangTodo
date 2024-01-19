package sys;

import shared.NetworkProtocol;
import shared.NotificationRegisterInfo;
import trans.NotificationPacket;
import trans.NotificationRaiser;
import trans.NotificationType;

import java.util.Hashtable;
import java.util.Vector;

/**
 * 提供系统的通知控制功能。
 */
public class NotificationController {
    private final Hashtable<NetworkProtocol, NotificationRaiser> protocolRaiserPairs = new Hashtable<>();
    private final Vector<NotificationRegisterInfo> receivers = new Vector<>(); //通知接收方信息。

    public void registerNotificationRaiser(NetworkProtocol protocol, NotificationRaiser notificationRaiser) throws IllegalArgumentException {
        synchronized (protocolRaiserPairs) {
            if (protocolRaiserPairs.contains(protocol))
                throw new IllegalArgumentException(String.format("已经存在通过 %s 协议分发通知的通知发送器，若要注册该协议的新通知发送器，必须先进行注销操作。", protocol));
            protocolRaiserPairs.put(protocol, notificationRaiser);
        }
    }

    public void unregisterNotificationRaiser(NetworkProtocol protocol) {
        protocolRaiserPairs.remove(protocol);
    }

    public void registerReceiver(NotificationRegisterInfo registerInfo) throws Exception {
        if (!receivers.contains(registerInfo))
            receivers.add(registerInfo);
        else throw new Exception("已经在端口" + registerInfo.getPort() + " 上注册过通知回调。");
    }

   public void unregisterReceiver(int port) throws Exception {
        if (receivers.stream().anyMatch(r -> r.getPort() == port))
            receivers.removeIf(r -> r.getPort() == port);
        else throw new Exception("尚未在端口 " + port + " 上注册过通知回调，因此无法移除。");
    }

    /**
     * 向所有通知接收方分发通知。
     * @param type 通知类型
     * @param content 通知内容
     */
    public void distribute(NotificationType type, String content) {
        NotificationPacket packet = new NotificationPacket();
        packet.setNotificationType(type);
        packet.setContent(content);
        distribute(packet);
    }

    public void distribute(NotificationPacket packet) {
        for (var receiver : receivers
        ) {
            int port = receiver.getPort();
            NetworkProtocol protocol = receiver.getProtocol();
            var raiser = protocolRaiserPairs.get(protocol);
            raiser.push(packet, port);
        }
    }
}
