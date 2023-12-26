package User;

public class UserRegister {
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
        if(!User.isValidPassword(password)) {
            throw new FormatException("密码格式错误");
        }else if(!User.isValidAccount(account)){
            throw new FormatException("账户格式错误");
        }else {
            //return new User(userName,account,password);
            return null;
        }
    }
}
