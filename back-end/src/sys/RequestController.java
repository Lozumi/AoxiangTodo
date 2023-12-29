package sys;

import shared.ToDoWorkItem;
import trans.RequestPacket;
import trans.ResponsePacket;
import trans.ResponseStatus;
import user.FormatException;
import user.User;
import util.JsonUtility;

import java.io.IOException;
import java.time.Instant;

/**
 * 该类用于定义处理请求的默认方法。
 *
 * @author 贾聪毅
 */
public class RequestController {
    /**
     * 处理注册请求，向服务器发送请求
     *
     * @param request  登录请求
     * @param userData 可选
     * @return 向服务器发送的请求
     */
    public static ResponsePacket processUserRegister(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        User user = User.fromJsonString(request.getContent());
        if (user == null) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为User", request.getContent()));
            return packet;
        }
        if (!user.isValidAccount()) {
            packet.setMessage("参数错误：账户格式错误！应为可显示的ASCII字符，且长度在8-32个字符之间");
        }
        if (!user.isValidPassword()) {
            packet.setMessage("参数错误：密码格式错误！应为可见的ASCII字符，长度在8-32个字符之间，必须同时包含字母、数字、符号三个种类的字符应为可显示的ASCII字符");
        }
        String jsonString;
        try {
            jsonString = user.toJsonString();
        } catch (IOException ioException) {
            var errString = String.format("内部错误：内部发生转换错误。");
            packet.setMessage(errString);
            return packet;
        }
        packet.setContent(jsonString);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    /**
     * 处理用户登录请求
     *
     * @param request  用户登录请求
     * @param userData 可选数据
     * @return 向服务器发送的登录请求
     */
    public static ResponsePacket processUserLogin(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        User user = User.fromJsonString(request.getContent());
        if (user == null) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为User", request.getContent()));
            return packet;
        }
        if (!user.isValidAccount()) {
            packet.setMessage("参数错误：账户格式错误！应为可显示的ASCII字符，且长度在8-32个字符之间");
        }
        if (!user.isValidPassword()) {
            packet.setMessage("参数错误：密码格式错误！应为可见的ASCII字符，长度在8-32个字符之间，必须同时包含字母、数字、符号三个种类的字符应为可显示的ASCII字符");
        }
        String jsonString;
        try {
            jsonString = user.toJsonString();
        } catch (IOException ioException) {
            var errString = "内部错误：内部发生转换错误。";
            packet.setMessage(errString);
            return packet;
        }
        packet.setContent(jsonString);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    /**
     * 处理用户登出请求
     *
     * @param request  请求
     * @param userData 附加信息
     * @return 响应对象
     */
    public static ResponsePacket processUserLogout(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        try {
            AoXiangToDoListSystem.getInstance().userLogout();
        } catch (Exception exception) {
            var errString = "内部错误：用户登出错误。";
            packet.setMessage(errString);
            return packet;
        }
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    public static ResponsePacket processUserModifyInformation(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        User newUser = User.fromJsonString(request.getContent());
        if (newUser == null) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为User", request.getContent()));
            return packet;
        }

        String jsonString;
        try {
            jsonString = newUser.toJsonString();
        } catch (IOException ioException) {
            var errString = "内部错误：内部发生转换错误。";
            packet.setMessage(errString);
            return packet;
        }
        packet.setContent(jsonString);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
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
        try {
            validateToDoWork(requestItem);
        } catch (FormatException exception) {
            packet.setMessage(String.format("非法参数格式：%s", exception.getMessage()));
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
        ToDoWorkItem item;
        try {
            item = getToDoWorkItemByInnerId(request.getContent());
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：%s", exception.getMessage()));
            return packet;
        }
        String jsonString;
        try {
            jsonString = item.toJsonString();
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
//        int innerId;
//        try {
//            innerId = Integer.parseInt(request.getContent());
//        } catch (NumberFormatException numberFormatException) {
//            packet.setMessage(String.format("参数错误：%s", numberFormatException.getMessage()));
//            return packet;
//        }
//        var collection = getSystemToDoWorkItemCollection();
//        boolean bo = collection.removeIf(item -> item.getInnerId() == innerId);
//        if (!bo) {
//            packet.setMessage(String.format("参数错误：系统中找不到innerId为%s的待办事项。", innerId));
//            return packet;
//        }
        try {
            ToDoWorkItem item = getToDoWorkItemByInnerId(request.getContent());
            getSystemToDoWorkItemCollection().remove(item);
        } catch (IllegalArgumentException exception) {
            packet.setMessage(String.format("参数错误：%s", exception.getMessage()));
            return packet;
        }
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    /**
     * 处理编辑待办事项请求。
     * @param request 请求对象。
     * @param userData 用户数据。
     * @return 响应数据包。
     */
    public static ResponsePacket processEditToDoWork(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        try {
            ToDoWorkItem toDoWorkItem = ToDoWorkItem.fromJsonString(request.getContent());
            validateToDoWork(toDoWorkItem);

            int innerId = toDoWorkItem.getInnerId();
            ToDoWorkItem targetItem = getToDoWorkItemByInnerId(innerId);

            //设置各种属性。注意不能设置innerId和createTime
            targetItem.setTitle(toDoWorkItem.getTitle());
            targetItem.setSubtitle(toDoWorkItem.getSubtitle());
            targetItem.setLayer(toDoWorkItem.getLayer());
            targetItem.setDescription(toDoWorkItem.getDescription());
            targetItem.setDeadLine(toDoWorkItem.getDeadLine());
            targetItem.setStatus(toDoWorkItem.getStatus());
            targetItem.setEmergencyPriority(toDoWorkItem.getEmergencyPriority());
            targetItem.setImportancePriority(toDoWorkItem.getImportancePriority());
            targetItem.setStartTime(toDoWorkItem.getStartTime());

            packet.setMessage(Messages.ZH_CN.SUCCESS);
            packet.setStatus(ResponseStatus.Success);
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：%s", exception.getMessage()));
        }
        return packet;
    }

    /**
     * 通过innerId字符串获取待办事项。
     *
     * @param innerIdStr innerId字符串。
     * @return 获取到的待办事项（若正常返回，保证非空）。
     * @throws IllegalArgumentException 字符串转换失败、找不到待办事项时抛出。
     */
    private static ToDoWorkItem getToDoWorkItemByInnerId(String innerIdStr) throws IllegalArgumentException {
        int innerId;
        try {
            innerId = Integer.parseInt(innerIdStr);
            return getToDoWorkItemByInnerId(innerId);
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    private static void validateToDoWork(ToDoWorkItem item) throws FormatException {
        if (item.getTitle().isBlank()) throw new FormatException("ToDoWorkItem的主标题不能为空。");
    }

    private static ToDoWorkItem getToDoWorkItemByInnerId(int innerId) throws IllegalArgumentException {
        var collection = getSystemToDoWorkItemCollection();
        var optionalItem = collection.stream().filter(i -> innerId == i.getInnerId()).findAny();
        if (optionalItem.isEmpty())
            throw new IllegalArgumentException(String.format("系统中找不到innerId为%s的待办事项。", innerId));
        return optionalItem.get();
    }

    private static ToDoWorkItemCollection getSystemToDoWorkItemCollection() {
        return AoXiangToDoListSystem.getInstance().getToDoWorkItemCollection();
    }

    private static User getSystemCurrentUser() {
        return AoXiangToDoListSystem.getInstance().getCurrentUser();
    }
}