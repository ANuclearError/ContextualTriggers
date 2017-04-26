package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.util.List;

/**
 * The trigger is for event the calendar indicates that today is empty.
 *
 * @author Aidan O'Grady
 */
public class EmptyCalendarTrigger extends SimpleTrigger {
    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Empty Calendar Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You've nothing planned today, why not go for a walk?";

    /**
     * The name of the trigger.
     */
    private String mName;

    /**
     * Android context for handling intents etc.
     */
    private Context mContext;

    /**
     * The data source holder
     */
    private ContextAPI mContextHolder;


    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param holder  the data source holder for accessing data
     */
    EmptyCalendarTrigger(ContextAPI holder) {
        super(holder);
        this.mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }


    @Override
    public String getNotificationMessage() {
        return NOTIFICATION_TEXT;
    }

    @Override
    public Boolean isTriggered() {
        List<CalendarEvent> events = mContextHolder.getTodaysEvents();
        System.out.printf("Events is null: %s", events == null);
        return events != null && events.isEmpty();
    }
}
