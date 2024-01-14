package sys;

import com.sun.net.httpserver.HttpServer;
import shared.Pomodoro;
import trans.BackEndHttpServer;
import trans.BackEndHttpServerStartupInfo;
import trans.CloudServer;
import user.User;
import util.FileHelper;
import util.JsonUtility;

import java.io.IOException;
import java.util.Objects;

public class AoXiangToDoListSystem {
    static AoXiangToDoListSystem system;
    BackEndHttpServer httpServer;
    String systemDataJsonPath = "D:/test/1.json";

    public static synchronized AoXiangToDoListSystem getInstance() {
        if (system == null) system = new AoXiangToDoListSystem();
        return system;
    }

    /**
     * 初始化系统实例，自动从文件中读取数据。
     */
    private AoXiangToDoListSystem() {
        this.reloadFromFile(systemDataJsonPath);
    }

    SystemData systemData;
    SystemController systemController = new SystemController();

    public PomodoroRecordCollection getPomodoroRecordsCollection() {
        return systemData.getPomodoroRecordCollection();
    }

    public SystemController getSystemController() {
        return systemController;
    }

    public ToDoWorkItemCollection getToDoWorkItemCollection() {
        return systemData.getToDoWorkItemCollection();
    }

    public User getCurrentUser() {
        return systemData.getCurrentUser();
    }

    public Pomodoro getPomodoro() {
        return systemData.getPomodoro();
    }

    /**
     * 注销当前用户的登录。
     * @throws IllegalStateException 没有登录任何用户时抛出。
     */
    public void userLogout() throws IllegalStateException {
        if (getCurrentUser() == null)
            throw new IllegalStateException("当前没有登录任何用户");
        innerUserLogout();
    }

    /**
     * 注销当前用户的登录。该方法不检测当前是否登录用户。
     */
    public void innerUserLogout(){
//        systemData.setCurrentUser(null);
//        systemData.setSynchronized(false);
        systemData = new SystemData();
        localSaveSystemData("D:/test/1.json");
    }


    public void runHttpServer(BackEndHttpServerStartupInfo startupInfo) {
        if (httpServer == null)
            httpServer = new BackEndHttpServer(startupInfo);
        httpServer.run();
        System.err.println("http服务器正在运行......");
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
            pomodoro.endPomodoro();
            AoXiangToDoListSystem.getInstance().localSaveSystemData("D:/test/1.json");
        }
    }

    /**
     * 将系统数据保存到本地系统。
     *
     * @param filePath 文件路径。
     */
    public void localSaveSystemData(String filePath) {
        if (systemData == null)
            return;
        try {
            var systemDataJsonString = systemData.toJsonString();
            FileHelper.saveStringToFile(filePath, systemDataJsonString);
        } catch (IOException exception) {
            System.err.printf("将系统数据保存至\"%s\"时发生错误：%s", filePath, exception.getMessage());
        }
    }

    public void updateSystemDataLastModifiedInstant(){
        systemData.updateLastModifiedInstant();
    }

    /**
     * 从本地文件重新加载系统数据。
     *
     * @param filePath 文件路径。
     */
    public void reloadFromFile(String filePath) {
        try {
            var jsonStr = FileHelper.readStringFromFile(filePath);
            systemData = JsonUtility.objectFromJsonString(jsonStr, SystemData.class);
            if (systemData == null)
                throw new Exception(String.format("文件%s中存储的不是有效的系统数据JSON字符串", filePath));
        } catch (Exception ex) {
            System.err.printf("从\"%s\"重新加载系统数据时发生错误：%s，创建默认实例\n", filePath, ex.getMessage());
            systemData = new SystemData();
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
    public void synchronizeSystemData() throws Exception {
        User currentUser = systemData.getCurrentUser();
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

        if (systemData.isSynchronized && systemData.lastModifiedInstant.isAfter(cloudSystemData.lastModifiedInstant)) {
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
        boolean oldSynchronized = systemData.isSynchronized;
        try {
            systemData.setSynchronized(true);
            CloudServer.sendPushRequest(getCurrentUser().getToken(), systemData.toJsonString());
        } catch (Exception exception) {
            systemData.setSynchronized(oldSynchronized);
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
