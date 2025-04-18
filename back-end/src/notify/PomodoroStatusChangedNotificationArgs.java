package notify;

import shared.PomodoroRecordStatus;
import shared.PomodoroStatus;

public class PomodoroStatusChangedNotificationArgs {
    private PomodoroStatus newStatus = PomodoroStatus.None;

    public PomodoroStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(PomodoroStatus newStatus) {
        this.newStatus = newStatus;
    }
}
