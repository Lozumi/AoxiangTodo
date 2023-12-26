package sys;

import trans.RequestPacket;
import trans.RequestType;
import trans.ResponsePacket;
import util.TokenHelper;

import java.util.*;

/**
 * 系统控制类。该类用于管理相关操作的处理器的注册、注销和调用。
 *
 * @author 贾聪毅
 */
public class SystemController {
    Vector<RequestHandlerInfo> requestHandlerInfos = new Vector<>();     //请求处理器信息集合。
    Hashtable<Integer,RequestHandlerInfo> requestHandlerIdDictionary = new Hashtable<>();       //处理器token（用于管理）和处理器信息的键值对。

    /**
     * 注册请求处理器。
     * @param requestHandlerInfo 处理器的信息。
     * @return 成功时返回该处理器的token，用该token来管理该注册信息。
     */
    public int registerRequestHandler(RequestHandlerInfo requestHandlerInfo)
    {
        if (requestHandlerInfos.contains(requestHandlerInfo))
        {
            throw new IllegalArgumentException(
                    String.format("已经存在目标处理类型为\"%s\"，上下文为\"%s\"的请求处理器。",
                            requestHandlerInfo.getTargetType(),requestHandlerInfo.getPreferredContext()));
        }

        int token = generateNonRepeatedToken();
        requestHandlerInfos.add(requestHandlerInfo);
        requestHandlerIdDictionary.put(token,requestHandlerInfo);

        return token;
    }

    /**
     * 注销指定请求处理器。
     * @param token 注册时返回的token。
     */
    public void unregisterRequestHandler(int token)
    {
        if(!requestHandlerIdDictionary.contains(token))
        {
            throw new IllegalArgumentException(String.format( "不存在token为\"%s\" 的处理器。",token));
        }
        requestHandlerIdDictionary.remove(token);
    }

    /**
     * 调用指定的请求处理器。如果没有符合上下文要求的处理器，将调用默认处理器。如果不存在默认处理器，则抛出异常。
     * @param packet 请求数据包。
     * @param context 上下文。
     * @return 响应数据包。
     */
    public ResponsePacket invokeRequestHandler(RequestPacket packet, String context)
    {
        RequestHandlerInfo handlerInfo = null;

        RequestType type = packet.getRequestType();

        var bestMatch = findRequestHandler(type,context);
        if(bestMatch != null)
            handlerInfo = bestMatch;

        var commonMatch = findRequestHandler(type,RequestHandlerInfo.defaultContext);
        if(commonMatch == null)
            throw new IllegalArgumentException(String.format("尚未注册处理类型为 \"%s\"，上下文为 \"%s\" 的请求处理器，且找不到该类型的默认处理器。",type,context));
        handlerInfo = commonMatch;

        var requestHandlerData = new RequestHandlerData();
        var result = handlerInfo.getHandler().processRequest(packet,requestHandlerData);
        return result;
    }


    int generateNonRepeatedToken()
    {
        var keys = requestHandlerIdDictionary.keys();
        return TokenHelper.generateNonRepeatedRandomInt(keys.asIterator());
    }
    RequestHandlerInfo findRequestHandler(RequestType type, String context)
    {
        var matchStream = requestHandlerInfos.stream().filter(info -> info.getTargetType().equals(type) && info.getPreferredContext().equals(context));
        var match = matchStream.findAny();
        return match.orElse(null);
    }
}
