package shared;

import sys.ToDoWorkItemCollection;
import trans.RequestPacket;
import trans.RequestType;
import trans.ResponsePacket;

/**
 * @author lzg
 */
public class ToDoWorkItemNotification {
    RequestPacket requestPacket;
    ToDoWorkItemNotificationType notificationType;

    public ToDoWorkItemNotification(ToDoWorkItemNotificationType notificationType, String title, String subtitle, String description){
        this.requestPacket.setRequestType(RequestType.ToDoWorkItemNotification);
        this.requestPacket.setContent(title+"\n"+subtitle+"\n"+description);

    }

    public void setNotificationType(ToDoWorkItemNotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public ToDoWorkItemNotificationType getNotificationType() {
        return notificationType;
    }

    //向前端发送通知
    public void Notice() throws Exception {
        throw new Exception("未实现");
    }
}
