package shared;

/**
 * 定义了番茄钟的关键信息，用于传输。
 */
public class PomodoroInfo implements JsonConvertable{
    int workTime, restTime;

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }
}
