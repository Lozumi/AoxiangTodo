package util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateHelper {

    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());

    /**
     * 将Instant对象转换为本项目标准时间字符串。
     * 标准时间格式 yyyy-MM-dd'T'HH:mm:ss
     * 例如 2022-01-01T09:00:00
     * @return 标准时间字符串。
     */
    public static String instantToStandardTime(Instant instant)
    {
        return timeFormatter.format(instant);
    }

    /**
     * 将本项目标准时间字符串转化为Instant对象。
     * @param time 时间字符串。
     * @return Instant对象。
     */
    public static Instant instantFromStandardTime(String time)
    {
        return Instant.from(timeFormatter.parse(time));
    }
}
