package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;
import android.provider.CalendarContract;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

/**
 * The EmptyCalendarWeatherTrigger analyses the user's calendar and weather to determine whether
 * or not to suggest ot the user that should go outside and do some activity.
 *
 * @author Aidan O'Grady
 */
public class EmptyCalendarWeatherTrigger extends CompositeTrigger {

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Empty Calendar & Good Weather Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You've nothing planned today, and the weather's nice, why not go for a walk?";

    /**
     * The triggers that this trigger comprises of.
     */
    private List<Trigger> mTriggers;

    /**
     * The data source containing information.
     */
    private ContextAPI mContextHolder;

    /**
     * Constructs a new CompositeTrigger.
     *
     * @param triggers  the triggers that comprise of this trigger
     * @param holder  the data holder
     */
    EmptyCalendarWeatherTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mTriggers = triggers;
        mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public Intent getNotificationIntent() {
        return new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "Walking")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Created by ContextualTriggers");
    }

    @Override
    public String getNotificationMessage() {
        return NOTIFICATION_TEXT;
    }

    @Override
    public Boolean isTriggered() {
        boolean shouldTrigger = true;
        for (Trigger trigger: mTriggers) {
            shouldTrigger = shouldTrigger && trigger.isTriggered();
        }
        return shouldTrigger;
    }
}
