package sys;

import com.fasterxml.jackson.annotation.JsonIgnore;
import shared.Pomodoro;
import shared.PomodoroRecord;
import shared.ToDoWorkItem;

import java.util.*;

/**
 * 保存番茄钟记录的集合类。
 */
public class PomodoroRecordCollection extends Vector<PomodoroRecord> {


    /**
     * 获取下一个可用的番茄钟记录ID。
     * @return ID。
     */
    @JsonIgnore
    public int getAvailableID(){
        if(this.isEmpty()){
            return 0;
        }
        return this.stream().max(Comparator.comparingInt(PomodoroRecord::getInnerId)).get().getInnerId()+1;
    }
}
