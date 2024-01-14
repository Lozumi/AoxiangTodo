package sys;

import trans.RequestType;

/**
 * 前端请求的处理器 的信息。
 * @author 贾聪毅
 */
public class RequestHandlerInfo {
    /**
     * 该项定义了默认处理器的上下文的值。
     * “如果在处理请求时，没有符合上下文要求的处理器，则调用上下文为 该值的处理器（即默认处理器）。”
     */
    public static final String defaultContext = "default";

    RequestType targetType;
    RequestHandler handler;
    String preferredContext;

    /**
     * 创建一个请求处理器信息对象。
     * @param targetType 处理器要关联处理的请求类型。
     * @param handler 处理器（函数式接口，见声明）。
     * @param preferredContext 上下文。如果该值不为 defaultContext，则只会收到拥有相同上下文内容的请求。
     */
    public RequestHandlerInfo(RequestType targetType, RequestHandler handler,String preferredContext)
    {
        this.targetType = targetType;
        this.handler = handler;
        this.preferredContext = preferredContext;
    }

    /**
     * 创建一个默认上下文处理器信息对象。
     * @param targetType 处理器要关联处理的请求类型。
     * @param handler 处理器（函数式接口，见声明）。
     */
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

    /**
     * 判断两个RequestHandlerInfo对象是否等价。判断标准：处理类型相同，处理上下文相同。
     * @param obj 另一个对象。
     * @return 等价返回true，否则返回false。
     */
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
