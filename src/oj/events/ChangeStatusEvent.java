package oj.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ChangeStatusEvent extends Event
{
    public static final EventType<ChangeStatusEvent> CHANGE_STATUS_EVENT =
            new EventType<>(Event.ANY, "STATUS BAR MESSAGE");

    private final String message;

    public ChangeStatusEvent(String message)
    {
        super(CHANGE_STATUS_EVENT);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
