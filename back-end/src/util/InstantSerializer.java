package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * 自定义Instant序列化方法。
 * @author 贾聪毅
 */
public class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DateHelper.instantToStandardTime(instant));
    }
}
