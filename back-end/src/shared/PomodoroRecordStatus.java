package shared;

/**
 * @author 贾聪毅
 */
public enum PomodoroRecordStatus {
    None,
    /**
     * 表示番茄钟记录正在生成。即当前番茄钟尚未结束。
     */
    Calculating,
    Finished,
    Interrupted
}
