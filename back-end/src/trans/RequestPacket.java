package trans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.SharedConfigurations;
import util.JsonUtility;

import java.io.InputStream;

public class RequestPacket extends TransmissionPacket {
    RequestType requestType;
    String content;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public static RequestPacket fromJsonString(String json) throws Exception{
        return JsonUtility.objectFromJsonString(json, RequestPacket.class);
    }

    public static RequestPacket fromJsonBytes(byte[] bytes) throws Exception{
        return JsonUtility.objectFromJsonBytes(bytes, RequestPacket.class);
    }

    public static RequestPacket fromJsonStream(InputStream stream, int expectedLength) throws Exception{
        return JsonUtility.objectFromInputStream(stream, expectedLength, RequestPacket.class);
    }
}
