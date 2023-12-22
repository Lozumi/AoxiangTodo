package trans;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.JsonConvertable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TransmissionPacket implements JsonConvertable {

}
