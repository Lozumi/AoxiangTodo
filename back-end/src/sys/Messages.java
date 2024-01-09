package sys;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * 定义系统各种提示信息。
 */
public class Messages {

    public static Messages ZH_CN = new Messages(){
        {
            SUCCESS = "操作成功完成";
            HTTP_WRONG_METHOD_TYPE = "请求方法类型应为POST";
            HTTP_IO_EXCEPTION = "HTTP处理时发生IO错误。";
            USER_WRONG_ACCOUNT_FORMAT ="账户格式错误！应为可显示的ASCII字符，且长度在8-32个字符之间";
            USER_WRONG_PASSWORD_FORMAT = "密码格式错误！应为可见的ASCII字符，长度在8-32个字符之间，必须同时包含字母、数字、符号三个种类的字符应为可显示的ASCII字符";
            IO_WRONG_REQUEST_PACKET = "解析请求包发生错误，请检查格式。";
            POMODORO_ALREADY_WORKING = "无法启动番茄钟，因为番茄钟已经在工作。";
            POMODORO_NOT_WORKING = "无法结束番茄钟，因为番茄钟还未开始工作。";
            POMODORO_INNER_ID_MISSING = "无法启动番茄钟，因为没有提供待办事项的innerId";
        }
    };
    public String SUCCESS,HTTP_WRONG_METHOD_TYPE,HTTP_IO_EXCEPTION,USER_WRONG_ACCOUNT_FORMAT,USER_WRONG_PASSWORD_FORMAT,IO_WRONG_REQUEST_PACKET,
    POMODORO_ALREADY_WORKING,POMODORO_NOT_WORKING, POMODORO_INNER_ID_MISSING;
}
