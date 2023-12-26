package sys;

import trans.RequestPacket;
import trans.RequestType;
import trans.ResponsePacket;

/**
 * 函数式接口。用于处理前端请求的接口。
 */
public interface RequestHandler {
    public ResponsePacket processRequest(RequestPacket requestPacket, RequestHandlerData userData);
}
