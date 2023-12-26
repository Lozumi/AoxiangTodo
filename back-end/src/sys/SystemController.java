package sys;

import java.util.*;

public class SystemController {
    final Random random = new Random();
    Vector<RequestHandlerInfo> requestHandlerInfos = new Vector<>();
    Hashtable<Integer,RequestHandlerInfo> requestHandlerIdDictionary = new Hashtable<>();

    public int registerRequestHandler(RequestHandlerInfo requestHandlerInfo)
    {
        if (requestHandlerInfos.contains(requestHandlerInfo))
        {
            throw new IllegalArgumentException(
                    String.format("已经存在目标处理类型为\"%s\"，上下文为\"%s\"的请求处理器。",
                            requestHandlerInfo.getTargetType(),requestHandlerInfo.getPreferredContext()));
        }

        int token = random.nextInt();
        requestHandlerInfos.add(requestHandlerInfo);
        requestHandlerIdDictionary.put(token,requestHandlerInfo);

        return token;
    }

    public boolean unregisterRequestHandler(int token)
    {
        if(!requestHandlerIdDictionary.contains(token))
        {
    //未实现
        }
        return false;
    }

}
