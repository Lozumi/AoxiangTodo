package shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 包含公共配置和可共用的工具类。
 * @author 贾聪毅
 */
public class SharedConfigurations {
    protected static ObjectMapper defaultObjectMapper;

    public synchronized static ObjectMapper getDefaultObjectMapper() {
        if (defaultObjectMapper == null) {
            defaultObjectMapper = new ObjectMapper();
            defaultObjectMapper.registerModule(new JavaTimeModule());
        }
        return defaultObjectMapper;
    }

    private SharedConfigurations() {
    }
}
