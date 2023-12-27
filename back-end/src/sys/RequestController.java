package sys;

import shared.ToDoWorkItem;
import trans.RequestPacket;
import trans.ResponsePacket;
import trans.ResponseStatus;
import util.JsonUtility;

import java.io.IOException;
import java.time.Instant;

/**
 * 该类用于定义处理请求的默认方法。
 *
 * @author 贾聪毅
 */
public class RequestController {
    public static ResponsePacket processUserLogin(RequestPacket request, RequestHandlerData userData) {
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
     * @param request  请求对象。
     * @param userData 用户数据。
     * @return 响应对象。
     */
    public static ResponsePacket processToDoWorkCreation(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        ToDoWorkItem requestItem = ToDoWorkItem.fromJsonString(request.getContent());
        if (requestItem == null) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为ToDoWorkItem", request.getContent()));
            return packet;
        }
        if (requestItem.getTitle().isBlank()) {
            packet.setMessage("非法参数格式：ToDoWorkItem的主标题不能为空。");
            return packet;
        }


        //重新设置事项的createTime和innerId
        var toDoWorkItemList = getSystemToDoWorkItemCollection();
        requestItem.setCreateTime(Instant.now());
        requestItem.setInnerId(toDoWorkItemList.getNextAvailableInnerId());
        toDoWorkItemList.add(requestItem);

        //操作成功响应
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        packet.setContent(String.valueOf(requestItem.getInnerId()));
        return packet;
    }

    public static ResponsePacket processQueryToDoWorkRequest(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        int innerId;
        try {
            innerId = Integer.parseInt(request.getContent());
        } catch (NumberFormatException numberFormatException) {
            packet.setMessage(String.format("参数错误：%s", numberFormatException.getMessage()));
            return packet;
        }

        var collection = getSystemToDoWorkItemCollection();
        var optionalItem = collection.stream().filter(i -> innerId == i.getInnerId()).findAny();
        if (optionalItem.isEmpty()) {
            packet.setMessage(String.format("内部错误：系统中不存在innerId为%s的待办事项。", innerId));
            return packet;
        }
        String jsonString;
        try {
            jsonString = optionalItem.get().toJsonString();
        } catch (IOException ioException) {
            var errString = String.format("内部错误：找到待办事项，但发生内部JSON转换错误。%s", ioException.getMessage());
            packet.setMessage(errString);
            return packet;
        }

        packet.setContent(jsonString);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    public static ResponsePacket processEnumerateToDoWorkItemListRequest(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();

        var collection = getSystemToDoWorkItemCollection();
        var itemsArray = collection.toArray();
        String json = JsonUtility.objectToJsonString(itemsArray);
        packet.setContent(json);
        packet.setStatus(ResponseStatus.Success);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        return packet;
    }

    public static ResponsePacket processDeleteToDoWorkRequest(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        int innerId;
        try {
            innerId = Integer.parseInt(request.getContent());
        } catch (NumberFormatException numberFormatException) {
            packet.setMessage(String.format("参数错误：%s", numberFormatException.getMessage()));
            return packet;
        }
        var collection = getSystemToDoWorkItemCollection();
        boolean bo = collection.removeIf(item -> item.getInnerId() == innerId);
        if (!bo) {
            packet.setMessage(String.format("参数错误：系统中找不到innerId为%s的待办事项。", innerId));
            return packet;
        }
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    private static ToDoWorkItemCollection getSystemToDoWorkItemCollection() {
        return AoXiangToDoListSystem.getInstance().getToDoWorkItemCollection();
    }
}
