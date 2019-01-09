package oj.utilities;

import javafx.event.EventType;
import oj.events.ChangeStatusEvent;
import oj.events.ChangeStatusHandler;
import org.controlsfx.control.StatusBar;

public class StatusBarCustom extends StatusBar {

    private ChangeStatusHandler changeStatusHandler;

    public void addSetChangeStatusListener(ChangeStatusHandler listener)
    {
        changeStatusHandler = listener;
        this.setEventHandler(ChangeStatusEvent.CHANGE_STATUS_EVENT, listener);
    }
}
