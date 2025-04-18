package trans;

import sys.Messages;
import util.JsonUtility;

import java.io.InputStream;

public class ResponsePacket extends TransmissionPacket {
    ResponseStatus status;
    String message;
    String content;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static ResponsePacket fromJsonString(String json) throws Exception{
        return JsonUtility.objectFromJsonString(json, ResponsePacket.class);
    }

    public static ResponsePacket fromJsonBytes(byte[] bytes) throws Exception{
        return JsonUtility.objectFromJsonBytes(bytes, ResponsePacket.class);
    }

    public static ResponsePacket fromJsonStream(InputStream stream, int expectedLength) throws Exception{
        return JsonUtility.objectFromInputStream(stream, expectedLength, ResponsePacket.class);
    }
    /**
     * 将指定响应包的相关信息设为成功。 包括status和message
     * @return 传入的响应包。注意返回值是同一个引用。
     */
    public ResponsePacket withSuccessResponse(){
        this.setMessage(Messages.ZH_CN.SUCCESS);
        this.setStatus(ResponseStatus.Success);
        return this;
    }
}
