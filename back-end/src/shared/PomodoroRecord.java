package shared;

import java.time.Duration;
import java.time.Instant;

/**
 * 番茄钟记录。
 * @author 贾聪毅
 * @param startTime 番茄钟开始时间。
 * @param endTime 番茄钟结束时间。
 * @param status 番茄钟状态。
 */
public record PomodoroRecord(Instant startTime,Instant endTime, PomodoroStatus status) {
    Duration getDuration() {
        return Duration.between(startTime, endTime);
    }
}
