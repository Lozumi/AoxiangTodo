package sys;

import trans.RequestPacket;
import trans.ResponsePacket;
import trans.ResponseStatus;
import user.User;
import user.UserLogin;

/**
 * 该类用于定义处理请求的默认方法。
 * @author 贾聪毅
 */
public class RequestController {
    public static ResponsePacket processUserLogin(RequestPacket request, RequestHandlerData userData)
    {
        var response = new ResponsePacket();
        response.setStatus(ResponseStatus.Warning);
        response.setMessage("方法尚未实现，返回默认值");
        response.setContent("这是默认内容。");
        return response;
    }

    /**
     * 处理创建待办事项请求。
     * content def
     *
     *
     * @param request 请求对象。
     * @param userData 用户数据。
     * @return 响应对象。
     */
    public static ResponsePacket processToDoWorkCreation(RequestPacket request,RequestHandlerData userData)
    {
        return null;
    }
}
