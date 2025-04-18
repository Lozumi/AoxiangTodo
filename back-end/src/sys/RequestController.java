package sys;

import shared.*;
import trans.*;
import exception.FormatException;
import shared.User;
import util.Encrypt;
import util.JsonUtility;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * 该类用于定义处理请求的默认方法。
 *
 * @author 贾聪毅
 */
public class RequestController {


    public static ResponsePacket processEnumeratePomodoroRecordRequest(RequestPacket request, RequestHandlerData userData){
        ResponsePacket response = makeDefaultResponse();
        try {
            String json = JsonUtility.objectToJsonString(getSystemPomodoroCollection().toArray());
            response.withSuccessResponse().setContent(json);
            return response;
        }catch (Exception exception){
            response.setMessage("查询所有番茄钟记录错误：" + exception.getMessage());
            return response;
        }
    }
    public static ResponsePacket processQueryPomodoroRecord(RequestPacket requestPacket, RequestHandlerData userData) {
        ResponsePacket packet = makeDefaultResponse();
        PomodoroRecord record;
        try {
            record = getPomodoroRecordByInnerId(requestPacket.getContent());
            packet.setContent(JsonUtility.objectToJsonString(record));
            packet.withSuccessResponse();
            return packet;
        } catch (Exception exception) {
            packet.setMessage(String.format("查询番茄钟记录错误：%s", exception.getMessage()));
            return packet;
        }
    }

    public static ResponsePacket processSaveSystemData(RequestPacket request, RequestHandlerData userData) {
        AoXiangToDoListSystem.getInstance().localSaveSystemData(AoXiangToDoListSystem.getInstance().systemDataJsonPath);
        return makeDefaultResponse().withSuccessResponse();
    }

    /**
     * 处理获取指定上下文的下一个通知的请求。
     *
     * @param request  请求包。
     * @param userData 用户数据，未使用。
     * @return 响应包。
     */
    public static ResponsePacket processGetNotification(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = makeDefaultResponse();
        NotificationPacket notification;
        try {
            //此方法会造成堵塞，直到指定上下文的通知队列至少有一个通知可用。
            notification = AoXiangToDoListSystem.getInstance().getNotificationController().pollNotificationForContext(request.getContent());
            packet.setContent(JsonUtility.objectToJsonString(notification));
        } catch (Exception exception) {
            packet.setMessage(exception.getMessage());
            return packet;
        }
        return packet.withSuccessResponse();
    }


    public static ResponsePacket processGetPomodoro(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket response = makeDefaultResponse();
        try {
            response.setContent(JsonUtility.objectToJsonString(AoXiangToDoListSystem.getInstance().getPomodoro()));
        } catch (Exception ex) {
            response.setMessage(ex.getMessage());
            return response;
        }

        return response.withSuccessResponse();
    }

    /**
     * 处理注销通知上下文的请求。
     *
     * @param request  请求包。
     * @param userData 用户数据。
     * @return 响应包。
     */
    public static ResponsePacket processUnregisterNotificationContext(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        try {
            AoXiangToDoListSystem.getInstance().getNotificationController().unregisterContext(request.getContent());
        } catch (Exception ex) {
            packet.setMessage(ex.getMessage());
            return packet;
        }
        packet.setStatus(ResponseStatus.Success);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        return packet;
    }

    public static ResponsePacket processRegisterNotificationContext(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        NotificationContext context;
        try {
            context = JsonUtility.objectFromJsonString(request.getContent(), NotificationContext.class);
        } catch (Exception exception) {
            packet.setMessage(String.format("无法将\"%s\"解析为NotificationContext对象：%s", packet.getContent(), exception.getMessage()));
            return packet;
        }
        try {
            AoXiangToDoListSystem.getInstance().getNotificationController().registerContext(context);
        } catch (Exception exception) {
            packet.setMessage(exception.getMessage());
            return packet;
        }

        packet.setStatus(ResponseStatus.Success);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        return packet;
    }

    public static ResponsePacket processExitApplication(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Success);
        AoXiangToDoListSystem.getInstance().exit();
        return packet;
    }

    public static ResponsePacket processGetCurrentUser_FullInfo(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        User currentUser = AoXiangToDoListSystem.getInstance().getCurrentUser();
        if (currentUser == null) {
            packet.setStatus(ResponseStatus.Success);
            packet.setMessage("当前没有登录任何用户。");
            packet.setContent(null);
            return packet;
        }
        String userStr;
        try {
            userStr = currentUser.toJsonString();
            packet.setMessage(Messages.ZH_CN.SUCCESS);
            packet.setStatus(ResponseStatus.Success);
            packet.setContent(userStr);
            return packet;
        } catch (Exception ex) {
            packet.setMessage(String.format("[内部错误]尝试将当前用户转换为JSON字符串时发生异常：%s\n", ex.getMessage()));
            return packet;
        }
    }

    /**
     * 处理同步请求。
     *
     * @param request  请求数据。
     * @param userData 未使用。
     * @return 向前端发送的请求。
     */
    public static ResponsePacket processSynchronization(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        try {
            AoXiangToDoListSystem.getInstance().synchronizeSystemData();
        } catch (Exception exception) {
            packet.setMessage(String.format("[同步时错误] %s\n", exception.getMessage()));
            return packet;
        }

        packet.setStatus(ResponseStatus.Success);
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        return packet;
    }

    /**
     * 处理编辑番茄钟事项请求。
     *
     * @param request  请求报文。
     * @param userData 未使用。
     * @return 向前端发送的请求。
     */
    public static ResponsePacket processEditPomodoro(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        PomodoroInfo pomodoroInfo;
        try {
            pomodoroInfo = JsonUtility.objectFromJsonString(request.getContent(), PomodoroInfo.class);
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为PomodoroInfo\n%s", request.getContent(), exception.getMessage()));
            return packet;
        }

        if (pomodoroInfo.getWorkTime() < 0 || pomodoroInfo.getWorkTime() > 90) {
            packet.setMessage(String.format("参数错误：番茄钟工作时间应当大于10分钟且小于90分钟，当前尝试将工作时间设为 %s 分钟", pomodoroInfo.getWorkTime()));
            return packet;
        }

        if (pomodoroInfo.getRestTime() < 0 || pomodoroInfo.getRestTime() > 90) {
            packet.setMessage(String.format("参数错误：番茄钟休息时间应当大于10分钟且小于90分钟，当前尝试将休息时间设为 %s 分钟", pomodoroInfo.getWorkTime()));
            return packet;
        }

        var pomodoro = AoXiangToDoListSystem.getInstance().getPomodoro();
        pomodoro.setWorkTime(pomodoroInfo.getWorkTime());
        pomodoro.setRestTime(pomodoroInfo.getRestTime());
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

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
        UserInfo userInfo;
        try {
            userInfo = JsonUtility.objectFromJsonString(request.getContent(), UserInfo.class);
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为UserInfo对象\n%s", request.getContent(), exception.getMessage()));
            return packet;
        }

        if (!User.isValidAccount(userInfo.getAccount())) {
            packet.setMessage(Messages.ZH_CN.USER_WRONG_ACCOUNT_FORMAT);
            return packet;
        }
        if (!User.isValidPassword(userInfo.getPassword())) {
            packet.setMessage(Messages.ZH_CN.USER_WRONG_PASSWORD_FORMAT);
            return packet;
        }

        try {
            boolean success = CloudServer.sendRegisterRequest(userInfo.getUserName(), userInfo.getAccount(), Encrypt.md5Hash(userInfo.getPassword()));
            if (!success) {
                packet.setMessage("注册失败。用户可能已存在。");
                return packet;
            }
        } catch (Exception exception) {
            var errString = String.format("发送注册请求时错误：%s", exception);
            packet.setMessage(errString);
            return packet;
        }
        packet.setContent("");
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
        UserInfo userInfo;

        try {
            userInfo = JsonUtility.objectFromJsonString(request.getContent(), UserInfo.class);
        } catch (Exception e) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为UserInfo\n%s", request.getContent(), e.getMessage()));
            return packet;
        }

        User user = new User();
        user.setEncryptedPassword(Encrypt.md5Hash(userInfo.getPassword()));
        user.setAccount(userInfo.getAccount());

        if (!User.isValidAccount(userInfo.getAccount())) {
            packet.setMessage(Messages.ZH_CN.USER_WRONG_ACCOUNT_FORMAT);
            return packet;
        }
        if (!User.isValidPassword(userInfo.getPassword())) {
            packet.setMessage(Messages.ZH_CN.USER_WRONG_PASSWORD_FORMAT);
            return packet;
        }
        ;
        try {
            String token = CloudServer.sendLoginRequest(user.getAccount(), user.getEncryptedPassword());
            if (token.isEmpty()) //判断token是否获取成功
            {
                throw new Exception("登录失败，请检查账号与密码是否正确。");
            } else { //获取到token，登录成功
                user.setToken(token);
                AoXiangToDoListSystem.getInstance().getSystemData().setCurrentUser(user);
                AoXiangToDoListSystem.getInstance().synchronizeSystemData();
                //todo:在登录用户后，自动进行一次数据同步。
            }
        } catch (Exception exception) {
            packet.setMessage(String.format("%s", exception.getMessage()));
            return packet;
        }
        packet.setContent("");
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
            var errString = String.format("用户登出错误：%s", exception.getMessage());
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
        UserInfo userInfo;
        try {
            userInfo = JsonUtility.objectFromJsonString(request.getContent(), UserInfo.class);
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为UserInfo\n%s", request.getContent(), exception.getMessage()));
            return packet;
        }

        User currentUser = AoXiangToDoListSystem.getInstance().getCurrentUser();
        if (currentUser == null) {
            packet.setMessage("不能修改用户信息，因为当前没有登录任何用户。");
            return packet;
        }

        String newHashedPwd = Encrypt.md5Hash(userInfo.getNewPassword());
        //旧密码验证成功
        boolean success;
        try {
            success = CloudServer.sendChangeUserInfoRequest(currentUser.getToken(), userInfo.getUserName(), currentUser.getEncryptedPassword(), newHashedPwd);
        } catch (IOException e) {
            packet.setMessage(String.format("连接服务器发生错误：%s", e.getMessage()));
            return packet;
        }
        if (!success) {
            packet.setMessage("修改失败，旧密码可能错误。");
            return packet;
        }

        //修改成功
        currentUser.setEncryptedPassword(newHashedPwd);
        currentUser.setUserName(userInfo.getUserName());
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

        ToDoWorkItem requestItem;
        try {
            requestItem = ToDoWorkItem.fromJsonString(request.getContent());
        } catch (Exception e) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为ToDoWorkItem\n%s", request.getContent(), e.getMessage()));
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

        //添加该事项
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
        ResponsePacket packet = makeDefaultResponse();

        var collection = getSystemToDoWorkItemCollection();
        var itemsArray = collection.toArray();
        String json;
        try {
            json = JsonUtility.objectToJsonString(itemsArray);
        } catch (Exception e) {
            packet.setMessage("[RequestController.processEnumerateToDoWorkItemListRequest] 在转换待办事项列表为JSON字符串时发生错误：" + e.getMessage());
            return packet;
        }
        packet.setContent(json);
        packet.withSuccessResponse();
        return packet;
    }

    public static synchronized ResponsePacket processDeleteToDoWorkRequest(RequestPacket request, RequestHandlerData userData) {
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
     *
     * @param request  请求对象。
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
            targetItem.suppressNotify();
            targetItem.setTitle(toDoWorkItem.getTitle());
            targetItem.setSubtitle(toDoWorkItem.getSubtitle());
            targetItem.setLayer(toDoWorkItem.getLayer());
            targetItem.setDescription(toDoWorkItem.getDescription());
            targetItem.setDeadLine(toDoWorkItem.getDeadLine());
            targetItem.setStatus(toDoWorkItem.getStatus());
            targetItem.setEmergencyPriority(toDoWorkItem.getEmergencyPriority());
            targetItem.setImportancePriority(toDoWorkItem.getImportancePriority());
            targetItem.setStartTime(toDoWorkItem.getStartTime());
            targetItem.resumeAndNotifyOnceOnChanged();

            packet.setMessage(Messages.ZH_CN.SUCCESS);
            packet.setStatus(ResponseStatus.Success);
        } catch (Exception exception) {
            packet.setMessage(String.format("参数错误：%s", exception.getMessage()));
        }
        return packet;
    }


    /**
     * 处理编辑待办事项请求
     *
     * @param request  请求
     * @param userData 扩展
     * @return 反馈包
     */
    public static ResponsePacket processEditToDoWorkRequest(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        ToDoWorkItem requestItem;
        try {
            requestItem = ToDoWorkItem.fromJsonString(request.getContent());
        } catch (Exception e) {
            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为ToDoWorkItem\n%s", request.getContent(), e.getMessage()));
            return packet;
        }

        if (requestItem.getTitle().isBlank()) {
            packet.setMessage("非法参数格式：ToDoWorkItem的主标题不能为空。");
            return packet;
        }

        int innerId = requestItem.getInnerId();
        //根据innerID来查找列表中待办事项，进行修改
        var toDoWorkItemList = getSystemToDoWorkItemCollection();
        Optional<ToDoWorkItem> oldToDoWorkItem = toDoWorkItemList.stream()
                .filter(obj -> (innerId == (obj.getInnerId())))
                .findFirst();
        if (oldToDoWorkItem.isEmpty()) {
            packet.setMessage((String.format("内部错误：系统中不存在innerId为%s的待办事项。", innerId)));
            return packet;
        }
        toDoWorkItemList.remove(oldToDoWorkItem.get());
        toDoWorkItemList.add(requestItem);

        //操作成功响应
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

//    /**
//     * 处理编辑番茄钟请求
//     * @param request 编辑番茄钟请求
//     * @param userData 额外信息
//     * @return 成功响应
//     */
//    public static ResponsePacket processEditPomodoro(RequestPacket request,RequestHandlerData userData){
//        ResponsePacket packet = new ResponsePacket();
//        packet.setStatus(ResponseStatus.Failure);
//
//        Pomodoro pomodoroNew = Pomodoro.fromJsonString(request.getContent());
//        if(pomodoroNew == null)
//        {
//            packet.setMessage(String.format("参数错误：无法将字符串\"%s\" 解析为Pomodoro",request.getContent()));
//            return packet;
//        }
//        Pomodoro pomodoro = AoXiangToDoListSystem.getInstance().getPomodoro();
//        pomodoro.setWorkTime(pomodoroNew.getWorkTime());
//        pomodoro.setRestTime(pomodoroNew.getRestTime());
//
//
//        //操作成功响应
//        packet.setMessage(Messages.ZH_CN.SUCCESS);
//        packet.setStatus(ResponseStatus.Success);
//        return packet;
//    }

    /**
     * 处理开始番茄钟请求
     *
     * @param request  绑定事项的InnerId
     * @param userData 未使用。
     * @return 响应包。
     */
    public static ResponsePacket processStartPomodoro(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        //开始番茄钟（设置番茄钟开始时间）
        try {
            //如果没有提供innerId，直接返回。
            if (request.getContent() == null || request.getContent().isBlank())
                throw new Exception(Messages.ZH_CN.POMODORO_INNER_ID_MISSING);

            Pomodoro pomodoro = AoXiangToDoListSystem.getInstance().getPomodoro();
            // 开始番茄钟
            pomodoro.start(Integer.parseInt(request.getContent()));
        } catch (Exception e) {
            packet.setMessage(e.getMessage());
            return packet;
        }

        //操作成功响应
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    /**
     * 处理主动结束番茄钟请求，添加PomodoroRecord至事件
     *
     * @param request  请求
     * @param userData 附加
     * @return
     */
    public static ResponsePacket processEndPomodoro(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        //结束番茄钟（设置番茄钟结束时间）
        try {
            Pomodoro pomodoro = AoXiangToDoListSystem.getInstance().getPomodoro();
            var pomodoroList = getSystemPomodoroCollection();
            // 打断后台计时
            pomodoro.stop();

        } catch (Exception e) {
            packet.setMessage(e.getMessage());
            return packet;
        }

        //操作成功响应
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
        return packet;
    }

    /**
     * 返回目前用户信息
     *
     * @param request
     * @param userData
     * @return
     */
    public static ResponsePacket processGetCurrentUser(RequestPacket request, RequestHandlerData userData) {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);

        try {
            User currentUser = getSystemCurrentUser();
            String userName = currentUser.getUserName();
            packet.setContent(userName);
        } catch (Exception e) {
            packet.setMessage(e.getMessage());
            return packet;
        }

        //操作成功响应
        packet.setMessage(Messages.ZH_CN.SUCCESS);
        packet.setStatus(ResponseStatus.Success);
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

    /**
     * 通过innerId字符串获取番茄钟记录。
     *
     * @param innerIdStr innerId字符串。
     * @return 获取到的番茄钟记录（若正常返回，保证非空）。
     * @throws IllegalArgumentException 字符串转换失败、找不到待办事项时抛出。
     */
    private static PomodoroRecord getPomodoroRecordByInnerId(String innerIdStr) throws IllegalArgumentException {
        int innerId;
        try {
            innerId = Integer.parseInt(innerIdStr);
            return getPomodoroRecordByInnerId(innerId);
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

    private static PomodoroRecord getPomodoroRecordByInnerId(int innerId) throws IllegalArgumentException {
        var collection = getSystemPomodoroCollection();
        var optionalItem = collection.stream().filter(i -> innerId == i.getInnerId()).findAny();
        if (optionalItem.isEmpty())
            throw new IllegalArgumentException(String.format("系统中找不到innerId为%s的番茄钟记录", innerId));
        return optionalItem.get();
    }

    private static ToDoWorkItemCollection getSystemToDoWorkItemCollection() {
        return AoXiangToDoListSystem.getInstance().getToDoWorkItemCollection();
    }

    private static PomodoroRecordCollection getSystemPomodoroCollection() {
        return AoXiangToDoListSystem.getInstance().getPomodoroRecordsCollection();
    }

    private static ResponsePacket makeDefaultResponse() {
        ResponsePacket packet = new ResponsePacket();
        packet.setStatus(ResponseStatus.Failure);
        packet.setMessage("");
        return packet;
    }


    private static User getSystemCurrentUser() {
        return AoXiangToDoListSystem.getInstance().getCurrentUser();
    }
}
