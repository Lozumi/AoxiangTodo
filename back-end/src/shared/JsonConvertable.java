package shared;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * 实现将对象转换为JSON字符串和字节数组的接口。
 * @author 贾聪毅
 */
@JsonTypeInfo(use= JsonTypeInfo.Id.NONE)
public interface JsonConvertable {
    /**
     * 将对象转换为JSON utf8编码格式的字符串。
     * @return JSON字符串。
     * @throws IOException 当发生IO异常时抛出。
     */
    default String toJsonString() throws IOException {
        ObjectMapper mapper = SharedConfigurations.getDefaultObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer,this);
        return writer.toString();
    }

    /**
     * 将对象转换为JSON utf8编码格式的字节数组。
     * @return JSON字符数组。
     * @throws IOException 发生IO异常时输出。
     */
    default byte[] toJsonBytes() throws IOException {
        return toJsonString().getBytes();
    }
}
