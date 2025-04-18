package sys;

import shared.Pomodoro;
import trans.*;
import shared.User;
import util.FileHelper;
import util.JsonUtility;

import java.io.IOException;

public class AoXiangToDoListSystem {
    static AoXiangToDoListSystem system;
    BackEndHttpServer httpServer;
    BackEndSocketServer socketServer;
    String systemDataJsonPath = "D:/test/1.json";

    public static synchronized AoXiangToDoListSystem getInstance() {
        if (system == null) {
            system = new AoXiangToDoListSystem();
            system.reloadFromFile(system.systemDataJsonPath);
        }
        return system;
    }

    /**
     * 初始化系统实例，自动从文件中读取数据。
     */
    private AoXiangToDoListSystem() {
    }

    private final SystemController systemController = new SystemController();
    private final NotificationController notificationController = new NotificationController();

    public NotificationController getNotificationController() {
        return notificationController;
    }

    public SystemData getSystemData() {
        return SystemData.getInstance();
    }

    public PomodoroRecordCollection getPomodoroRecordsCollection() {
        return getSystemData().getPomodoroRecordCollection();
    }

    public SystemController getSystemController() {
        return systemController;
    }

    public ToDoWorkItemCollection getToDoWorkItemCollection() {
        return getSystemData().getToDoWorkItemCollection();
    }

    public User getCurrentUser() {
        return getSystemData().getCurrentUser();
    }

    public Pomodoro getPomodoro() {
        return getSystemData().getPomodoro();
    }

    public void userLogout() throws IllegalStateException {
        if (getCurrentUser() == null)
            throw new IllegalStateException("当前没有登录任何用户");
        getSystemData().setCurrentUser(null);
    }


    public void runHttpServer(BackEndHttpServerStartupInfo startupInfo) {
        if (httpServer == null)
            httpServer = new BackEndHttpServer(startupInfo);
        httpServer.run();
        System.err.println("http服务器正在运行......");
    }

    public void runSocketServer(BackEndServerStartupInfo startupInfo) {
        if (socketServer == null)
            socketServer = new BackEndSocketServer(startupInfo);
        socketServer.run();
        System.err.println("socket服务器正在运行......");
    }

    /**
     * 禁用新的http连接，终止任何正在进行的番茄钟，保存系统数据。
     */
    public void exit() {
        if (httpServer != null) {
            new Thread(() -> {
                httpServer.stop(3);
                System.exit(0); //退出本程序。
            }).start();
            Pomodoro pomodoro = getPomodoro();
            pomodoro.stop();
            AoXiangToDoListSystem.getInstance().localSaveSystemData("D:/test/1.json");
        }
    }

    /**
     * 将系统数据保存到本地系统。
     *
     * @param filePath 文件路径。
     */
    public void localSaveSystemData(String filePath) {
        try {
            var systemDataJsonString = getSystemData().toJsonString();
            FileHelper.saveStringToFile(filePath, systemDataJsonString);
            System.out.printf("系统数据保存至\" %s \"。\n", filePath);
        } catch (IOException exception) {
            System.err.printf("将系统数据保存至\"%s\"时发生错误：%s", filePath, exception.getMessage());
        }
    }

    /**
     * 从本地文件重新加载系统数据。
     *
     * @param filePath 文件路径。
     */
    public void reloadFromFile(String filePath) {
        try {
            var jsonStr = FileHelper.readStringFromFile(filePath);
            getSystemData().update(jsonStr);
        } catch (Exception ex) {
            System.err.printf("从\"%s\"重新加载系统数据时发生错误：%s，创建默认实例\n", filePath, ex.getMessage());
        }
    }

    /**
     * 同步
     * 1.试图从服务端拉取系统数据
     * 2.如果HTTP请求失败，中断
     * 3.如果HTTP请求成功，但拉取为空，则上传本地数据
     * 4.如果HTTP请求成功，拉取不为空，判断本地数据是否同步过，若同步过则对比最后修改时间戳，采用较新的数据。若本地数据没有同步过，则直接使用服务端数据。
     *
     * @throws Exception 当数据同步失败时抛出，包含失败说明文本。
     */
    public synchronized void synchronizeSystemData() throws Exception {
        User currentUser = getSystemData().getCurrentUser();
        if (currentUser == null || currentUser.getToken() == null)
            throw new Exception("用户未登录。");
        String cloudSystemDataJsonStr;
        try {
            cloudSystemDataJsonStr = CloudServer.sendPullRequest(currentUser.getToken()); //尝试拉取字符串，如果这一步失败，说明无法连接到服务器，抛出异常中断操作。
        } catch (Exception exception) {
            throw new Exception("拉取用户数据时发生HTTP请求异常：" + exception.getMessage());
        }
        if (cloudSystemDataJsonStr.trim().equals("Failure"))
            throw new IllegalAccessException("服务器拒绝连接，这可能是因为当前用户的token无效。");

        SystemData cloudSystemData;
        try {
            cloudSystemData = JsonUtility.objectFromJsonString(cloudSystemDataJsonStr, SystemData.class);
        } catch (Exception e) {
            //服务器数据转换失败，以本地数据为最新数据，将本地数据直接上传到服务端
            System.err.println("服务器请求成功，但没有数据或数据无效，正在尝试推送本地有效数据。");
            try {
                saveAndPush();
                return;
            } catch (Exception exception) {
                throw new Exception("服务端数据无效，尝试推送本地数据时发生异常：" + exception.getMessage());
            }
        }

        if (getSystemData().isSynchronized() && getSystemData().lastModifiedInstant.isAfter(cloudSystemData.lastModifiedInstant)) {
            //本地的数据比云端数据更新，推送
            try {
                saveAndPush();
                return;
            } catch (Exception exception) {
                throw new IOException("本地数据比服务端数据更新，但尝试推送本地数据时发生异常：" + exception.getMessage());
            }
        }

        try {
            saveAndReload(cloudSystemDataJsonStr);//本地没有同步过，而云端数据可用，直接使用。
        } catch (Exception exception) {
            throw new IOException("服务端数据比本地更新，且拉取成功，但保存到本地并读取数据时发生异常。");
        }
    }


    /**
     * 将当前系统数据保存到本地，然后推送给服务器。
     * 该方法仅内部使用，不应该直接调用。
     *
     * @throws IOException 当保存或推送失败时抛出。
     */
    void saveAndPush() throws IOException {
        localSaveSystemData(systemDataJsonPath);
        boolean oldSynchronized = getSystemData().isSynchronized();
        try {
            getSystemData().setSynchronized(true);
            CloudServer.sendPushRequest(getCurrentUser().getToken(), getSystemData().toJsonString());
        } catch (Exception exception) {
            getSystemData().setSynchronized(oldSynchronized);
            throw exception;
        }
    }

    /**
     * 从服务器拉取数据保存到本地，然后从本地加载该数据。
     * 该方法仅内部使用，不应该直接调用。
     *
     * @throws IOException 当拉取或加载失败时抛出。
     */
    void saveAndReload(String json) throws IOException {
        FileHelper.saveStringToFile(systemDataJsonPath, json);
        reloadFromFile(systemDataJsonPath);
    }

}
