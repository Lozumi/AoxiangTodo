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
        }
    };
    public String SUCCESS;
}
