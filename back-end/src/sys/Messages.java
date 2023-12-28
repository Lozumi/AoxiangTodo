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
        }
    };
    public String SUCCESS,HTTP_WRONG_METHOD_TYPE,HTTP_IO_EXCEPTION;
}
