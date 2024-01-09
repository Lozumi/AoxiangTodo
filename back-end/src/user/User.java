package user;

import shared.JsonConvertable;
import util.Encrypt;
import util.JsonUtility;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class User implements JsonConvertable {

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
    public String getEncryptedPassword() throws NoSuchAlgorithmException {
        return Encrypt.md5Hash(password);
    }


    public boolean isValidPassword() {
        return isValidPassword(this.password);
    }

    public boolean isValidAccount() {
        return isValidAccount(this.account);
    }

    /**
     * 校验密码格式
     * 格式要求：可见的ASCII字符，长度在8-32个字符之间，必须同时包含字母、数字、符号三个种类的字符
     *
     * @return 格式是否正确
     */
    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;
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

    public static boolean isValidAccount(String account) {
        if (account == null)
            return false;
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


    /**
     * 从Json字符串构造一个User对象。
     *
     * @param json json字符串。
     * @return 新构造的User对象，失败返回null。
     */
    public static User fromJsonString(String json) throws Exception{
        return JsonUtility.objectFromJsonString(json, User.class);
    }

    /**
     * 从Json字节数组构造一个User对象。
     *
     * @param bytes json字节数组。
     * @return 新构造的User对象，失败返回null。
     */
    public static User fromJsonBytes(byte[] bytes) throws Exception {
        return fromJsonString(new String(bytes));
    }


    /**
     * 从Json字节流中读取指定数量的字节，然后从字节数组构造一个User对象。
     *
     * @param stream         Json字节输入流。
     * @param expectedLength 预期的字节长度。如果该值小于等于0，则读取流的所有字节。
     * @return 新构造的User对象。失败返回null，并尝试回溯流数据到未调用此方法前的状态。
     */
    public static User fromJsonStream(InputStream stream, int expectedLength) throws Exception {
        return JsonUtility.objectFromInputStream(stream, expectedLength, User.class);
    }

}