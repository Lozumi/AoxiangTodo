package sys;

import trans.RequestType;

/**
 * 前端请求的处理器 的信息。
 * @author 贾聪毅
 */
public class RequestHandlerInfo {
    static final String defaultContext = "default";

    RequestType targetType;
    RequestHandler handler;
    String preferredContext;
    public RequestHandlerInfo(RequestType targetType, RequestHandler handler,String preferredContext)
    {

    }

    public RequestHandlerInfo(RequestType targetType, RequestHandler handler)
    {
        this(targetType,handler,defaultContext);
    }

    public RequestType getTargetType() {
        return targetType;
    }
    public RequestHandler getHandler() {
        return handler;
    }
    public String getPreferredContext() {
        return preferredContext;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RequestHandlerInfo info)
        {
            return info.getPreferredContext().equals(preferredContext)
                    && info.getTargetType().equals(targetType);
        }
        return false;
    }
}
