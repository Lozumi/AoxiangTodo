package trans;

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

    public static ResponsePacket fromJsonString(String json) {
        return JsonUtility.objectFromJsonString(json, ResponsePacket.class);
    }

    public static ResponsePacket fromJsonBytes(byte[] bytes) {
        return JsonUtility.objectFromJsonBytes(bytes, ResponsePacket.class);
    }

    public static ResponsePacket fromJsonStream(InputStream stream, int expectedLength) {
        return JsonUtility.objectFromInputStream(stream, expectedLength, ResponsePacket.class);
    }
}
