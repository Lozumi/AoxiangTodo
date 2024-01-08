package statistics;

import shared.PomodoroRecord;
import shared.PomodoroStatus;
import shared.ToDoWorkItem;
import shared.WorkItemStatus;
import sys.AoXiangToDoListSystem;
import sys.PomodoroRecordCollection;
import sys.ToDoWorkItemCollection;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzg
 */
public class statistics {
    PomodoroRecordCollection pomodoroRecords;
    ToDoWorkItemCollection toDoWorkItems;
    List<ToDoWorkItem> yesterdayTodaWorkItems = new ArrayList<>();
    List<PomodoroRecord> yesterdayPomodoros = new ArrayList<>();
    int yesterdayFinishedToDoItemNumber,yesterdayPomodoroFinishedNumber;
    long yesterdayPomodoroMinutes,yesterdayPomodoroHours;

    /**
     * 构造函数
     *
     * 初始化参数
     * @param sys 系统
     */
    public statistics(AoXiangToDoListSystem sys){
        this.pomodoroRecords = sys.getPomodoroRecordsCollection();
        this.toDoWorkItems = sys.getToDoWorkItemCollection();
        yesterdayTodaWorkItems = setYesterdayTodaWorkItems();
        yesterdayPomodoros = setYesterdayPomodoros();

        yesterdayFinishedToDoItemNumber = setYesterdayFinishedToDoItemNumber();
        yesterdayPomodoroFinishedNumber = setYesterdayPomodoroFinishedNumber();

        yesterdayPomodoroMinutes = yesterdayPomodoroTime().toMinutes();
        yesterdayPomodoroHours = yesterdayPomodoroTime().toHours();

    }

    private int setYesterdayFinishedToDoItemNumber() {
        int temp = 0;
        for(ToDoWorkItem toDoWorkItem:this.yesterdayTodaWorkItems){
            if(toDoWorkItem.getStatus().equals(WorkItemStatus.Finished)){temp+=1;}
        }
        return temp;
    }

    /**
     * 获取昨日待办事项
     * @return 昨日待办事项
     */
    private List<ToDoWorkItem> setYesterdayTodaWorkItems(){
        List<ToDoWorkItem> temp = new ArrayList<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Instant startOfYesterday = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfYesterday = yesterday.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        for (ToDoWorkItem toDoWorkItem:this.toDoWorkItems){
            boolean isWithinYesterday = toDoWorkItem.getDeadLine().isAfter(startOfYesterday) && toDoWorkItem.getDeadLine().isBefore(endOfYesterday);
            if (isWithinYesterday){
                temp.add(toDoWorkItem);
            }
        }
        return temp;
    }


    /**
     * 返回昨日完成的番茄钟
     * @return 昨日完成的番茄钟列表
     */
    private List<PomodoroRecord> setYesterdayPomodoros() {
        List<PomodoroRecord> temp = new ArrayList<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Instant startOfYesterday = yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfYesterday = yesterday.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        for (PomodoroRecord pomodoroRecord:pomodoroRecords){
            boolean isWithinYesterday = pomodoroRecord.getEndTime().isAfter(startOfYesterday) && pomodoroRecord.getEndTime().isBefore(endOfYesterday);
            if (isWithinYesterday){
                temp.add(pomodoroRecord);
            }
        }
        return temp;
    }

    /**
     * 返回昨日完成番茄钟总时长
     * @return 完成番茄钟总时长
     */
    public Duration yesterdayPomodoroTime(){
        Duration ans = Duration.ZERO;
        for (PomodoroRecord pomodoroRecord:this.yesterdayPomodoros){
            ans = ans.plus(pomodoroRecord.getDuration());
        }
        return ans;
    }

    /**
     * 获取昨日完成的番茄钟数量
     * @return
     */
    private int setYesterdayPomodoroFinishedNumber(){
        int temp = 0;
        for(PomodoroRecord pomodoroRecord:this.yesterdayPomodoros){
            if(pomodoroRecord.getPomodoroStatus().equals(PomodoroStatus.Finished)){temp+=1;}
        }
        return temp;
    }

    /**
     * 返回昨日完成待办事项数量
     * @return 昨日完成待办事项数量
     */
    public int getYesterdayFinishedToDoItemNumber() {
        return yesterdayFinishedToDoItemNumber;
    }

    /**
     * 返回昨日完成番茄钟时间（单位小时）
     * @return 昨日完成番茄钟时间
     */

    public long getYesterdayPomodoroHours() {
        return yesterdayPomodoroHours;
    }

    /**
     * 返回昨日完成番茄钟时间（单位分钟）
     * @return 昨日完成番茄钟时间
     */
    public long getYesterdayPomodoroMinutes() {
        return yesterdayPomodoroMinutes;
    }
}
