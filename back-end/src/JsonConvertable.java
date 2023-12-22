import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS)
public interface JsonConvertable {
    default String toJsonString() throws IOException {
        ObjectMapper mapper = SharedConfigurations.getDefaultObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer,this);
        return writer.toString();
    }
    default byte[] toJsonBytes() throws IOException {
        return toJsonString().getBytes();
    }
}
