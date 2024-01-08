package sys;

import shared.Pomodoro;
import trans.CloudServer;
import user.User;
import user.UserStatus;
import util.FileHelper;
import util.JsonUtility;

import java.io.IOException;

public class AoXiangToDoListSystem {
    static AoXiangToDoListSystem system;
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

    public void userLogout() {
        systemData.setCurrentUser(null);
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

    /**
     * 从本地文件重新加载系统数据。
     *
     * @param filePath 文件路径。
     */
    public void reloadFromFile(String filePath) {
        try {
            var jsonStr = FileHelper.readStringFromFile(filePath);
            systemData = JsonUtility.objectFromJsonString(jsonStr, SystemData.class);
        } catch (Exception ex) {
            System.err.printf("从\"%s\"重新加载系统数据时发生错误：%s，创建默认实例", filePath, ex.getMessage());
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
        if (currentUser == null)
            throw new Exception("用户未登录。");
        if (currentUser.getUserStatus() == UserStatus.Offline) {
            //此处补充登录逻辑，登录成功继续，登陆失败抛出异常
            throw new Exception("同步失败：用户登录异常。");
        }
        String cloudSystemDataJsonStr;
        try {
            cloudSystemDataJsonStr = CloudServer.sendPullRequest(currentUser.getToken()); //尝试拉取字符串，如果这一步失败，说明无法连接到服务器，抛出异常中断操作。
        } catch (Exception exception) {
            throw new Exception("拉取用户数据时发生HTTP请求异常：" + exception.getMessage());
        }

        var cloudSystemData = JsonUtility.objectFromJsonString(cloudSystemDataJsonStr, SystemData.class); //此处不会抛出异常，如果失败则cloudSystemData为null
        if (cloudSystemData == null) {
            //服务器数据转换失败，以本地数据为最新数据，将本地数据直接上传到服务端
            try {
                saveAndPush();
            } catch (Exception exception) {
                throw new Exception("服务端数据无效，尝试推送本地数据时发生异常：" + exception.getMessage());
            }
        } else {
            if (systemData.isSynchronized) {
                if (systemData.lastModifiedInstant.isAfter(cloudSystemData.lastModifiedInstant)) {
                    //本地的数据比云端数据更新，推送
                    try {
                        saveAndPush();
                    } catch (Exception exception) {
                        throw new Exception("本地数据比服务端数据新，尝试推送本地数据时发生异常：" + exception.getMessage());
                    }
                } else {
                    //云端的数据比本地更新，使用云端数据。
                    try {
                        saveAndReload(cloudSystemDataJsonStr);
                    } catch (Exception exception) {
                        throw new Exception("服务端数据比本地新，但尝试拉取时发生异常：" + exception.getMessage());
                    }
                }
            } else {
                try {
                    saveAndReload(cloudSystemDataJsonStr);//本地没有同步过，而云端数据可用，直接使用。
                } catch (Exception exception) {
                    throw new Exception("找到云端合法数据，但保存并读取数据时发生异常。");
                }
            }
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
        CloudServer.sendPushRequest(getCurrentUser().getToken(), systemData.toJsonString());
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
