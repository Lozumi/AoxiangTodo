package User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class User {

    /**
     * 属性定义
     * <p>
     * 用户名（不作为用户区别凭证）
     * 用户账号，作为登陆时区分用户的唯一凭证
     * 用户密码
     * 用户token（凭证）
     * 用户状态
     */
    String userName, account, password, token;
    UserStatus userStatus;

    public User(String userName, String account, String password) {
        this.userName = userName;
        this.account = account;
        this.password = password;
    }

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
     *
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
     * 设置token
     *
     * @param token 登录有效性的验证
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 返回token
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 返回加密过的密码
     *
     * @return 加密过的密码（作为唯一识别，系统中不出现密码的明文对比）
     */
    public String getEncryptedPassword(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(this.password.getBytes());
        byte[] byteData = md.digest();

        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteData) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * 注册用户，并检查注册信息的格式是否正确
     *
     * @param userName 用户名
     * @param account 账号
     * @param password 密码
     * @return 创建新用户
     * @throws FormatException 格式错误
     */
    public static User register(String userName, String account, String password)throws FormatException {
        if(!isValidPassword(password)) {
            throw new FormatException("密码格式错误");
        }else if(!isValidAccount(account)){
            throw new FormatException("账户格式错误");
        }else {return new User(userName,account,password);}
    }

    /**
     * 校验密码格式
     * 格式要求：可见的ASCII字符，长度在8-32个字符之间，必须同时包含字母、数字、符号三个种类的字符
     *
     * @param password 密码
     * @return 格式是否正确
     */
    public static boolean isValidPassword(String password) {
        // 密码长度限制：8到32个字符
        int minLength = 8;
        int maxLength = 32;

        // 密码字符限制：包含至少一个字母、一个数字和一个特殊字符
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{"
                + minLength + "," + maxLength + "}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean isValidAccount(String account){
        // 账号长度限制：8-32个字符
        int minLength = 8;
        int maxLength = 32;

        // 密码字符限制：包含至少一个字母、一个数字和一个特殊字符
        String regex = "^[ -~]+${"
                + minLength + "," + maxLength + "}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(account);

        return matcher.matches();
    }
}