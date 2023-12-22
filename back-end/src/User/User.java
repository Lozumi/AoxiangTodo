package User;

public class User{

    /**
     * 属性定义
     *
     * 用户名（不作为用户区别凭证）
     * 用户账号，作为识别用户的唯一凭证
     * 用户密码
     * 用户状态
     */
    String userName,account,password;
    UserStatus userStatus;

    /**
     * 设置用户账号
     *
     * @param account 用户账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 设置用户密码
     *
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 设置用户状态
     * @param userStatus 用户状态
     */
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 返回用户账号
     *
     * @return 用户账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 返回用户名
     *
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 返回账号状态
     *
     * @return 帐号状态
     */
    public UserStatus getUserStatus() {
        return userStatus;
    }

    /**
     * 返回加密过的密码
     *
     * @return 加密过的密码（作为唯一识别，系统中不出现密码的明文对比）
     */
//    public String getEncryptedPassword(){
//        return
//    }
}
