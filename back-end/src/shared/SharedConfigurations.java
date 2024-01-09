package shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import util.InstantDeserializer;
import util.InstantSerializer;

import java.time.Instant;

/**
 * 包含公共配置和可共用的工具类。
 * @author 贾聪毅
 */
public class SharedConfigurations {
    protected static ObjectMapper defaultObjectMapper;

    public synchronized static ObjectMapper getDefaultObjectMapper() {
        if (defaultObjectMapper == null) {
             generateObjectMapper();
        }
        return defaultObjectMapper;
    }

    private static void generateObjectMapper()
    {
        defaultObjectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class,new InstantSerializer());
        module.addDeserializer(Instant.class,new InstantDeserializer());
        defaultObjectMapper.registerModule(module);
    }
    private SharedConfigurations() {

    }
}
