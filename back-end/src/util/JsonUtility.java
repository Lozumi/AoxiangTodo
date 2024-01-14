package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.SharedConfigurations;
import shared.ToDoWorkItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.Period;

public class JsonUtility {
    public static <T> T objectFromJsonString(String json, Class<T> type) throws Exception {
        ObjectMapper mapper = SharedConfigurations.getDefaultObjectMapper();
        T obj = null;

        try {
            obj = mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new Exception(String.format("[JSONUtility.objectFromJsonString]从Json构造对象错误：%s\n", e.getMessage()));
        }
        return obj;
    }

    public static <T> T objectFromJsonBytes(byte[] bytes, Class<T> type) throws Exception {
        return objectFromJsonString(new String(bytes), type);
    }

    public static <T> T objectFromInputStream(InputStream stream, int expectedLength, Class<T> type) throws Exception {
        try {
            if (expectedLength <= 0) expectedLength = stream.available(); //如果预期字节长度小于等于0，则读取所有字节。
            stream.mark(expectedLength); //标记流当前位置，在失败时尝试回溯。
        } catch (IOException e) {
            throw new Exception("[JSONUtility.objectFromInputStream]尝试读取流时发生错误：" + e.getMessage());
        }

        try {
            byte[] bytes = stream.readNBytes(expectedLength);
            return objectFromJsonBytes(bytes, type);      //尝试从字节流构造对象并返回。
        } catch (IOException e) {
            try {
                stream.reset(); //读取字节流发生异常，尝试回溯。
            } catch (IOException ex) {
                throw new Exception(String.format("[JSONUtility.objectFromInputStream]从流构造 %s 对象时发生异常：%s\n尝试回溯时发生异常：%s\n", type.getName(), e.getMessage(), ex.getMessage())); //回溯失败，打印异常信息。
            }
        }
        return null;
    }

    public static String objectToJsonString(Object object) throws Exception {
        var objectMapper = SharedConfigurations.getDefaultObjectMapper();
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, object);
        } catch (Exception exception) {
            throw new Exception("[JSONUtility.objectToJsonString]将对象转换为JSON字符串时发生错误：" + exception.getMessage());
        }
        return writer.toString();
    }

    public static byte[] objectToJsonBytes(Object object) throws Exception{
        return objectToJsonString(object).getBytes();
    }

    private JsonUtility() {

    }
}
