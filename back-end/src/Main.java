import java.io.IOException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws IOException {
        ToDoWorkItem item = new ToDoWorkItem(100,Instant.now());
        String str = item.toJsonString();

        ToDoWorkItem item2 = ToDoWorkItem.fromJsonString(str);
        String str2 = item2.toJsonString();

        System.out.printf("str %s str2",str.equals(str2)? "等于" : "不等于");
    }
}
